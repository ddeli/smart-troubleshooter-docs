package com.me.smart_troubleshooting_docs.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/generate-doc-adjust")
public class DocControllerAdjust {

    public class TextFileReader {

        public String readTextFileAsString(String filename) throws IOException {
            ClassPathResource resource = new ClassPathResource(filename);
            try (var inputStream = resource.getInputStream()) {
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
        }
    }

    @PostMapping
    public ResponseEntity<?> generateDocumentation(@RequestBody Map<String, String> body) throws IOException {

        TextFileReader reader = new TextFileReader();
        String content = reader.readTextFileAsString("additionalPrompt.txt");
        System.out.println(content);

        String inputText = body.getOrDefault("text", "");

        String prompt = """
            Wandle den folgenden Text in ausführliche strukturierte technische Dokumentation für eine Wissensbasis.
            Der inhalt der einzelnen teile soll immer in der selben sprache wie der Eingabetext sein.
            Achte darauf, dass der Text professionell und nicht umgangssprachlich wird.
            Wenn in dem eingabetext keine solution beschrieben ist dann suche selber eine und achte auf die details damit es keine unpassende antwort wird.
            halte unter allen umständen die vorgegebene  Beispiel Grundstruktur mit den teilen: title, symptom, problem und solution ein.
            Recherchiere so viele informationen wie möglich aber Vermeide redundante teile zwischen den einzelnen teilen.
            
            Text: 'Eigabetext'
            
             ---Beispiel Grundstruktur response beginnt hier---
            title: 
            symptom: 
            problem:
            solution:
            ---Beispiel Grundstruktur response endet hier---
            
            
            Jetzt dein Text:
            %s

            documentation:
            """.formatted(inputText);

        // Call Ollama API
        RestTemplate restTemplate = new RestTemplate();
        String ollamaUrl = "http://localhost:11434/api/generate";

        //// MISTRAL
        Map<String, Object> request = new HashMap<>();
        request.put("model", "mistral");
//        request.put("model", "mistral:7b-instruct-q4_K_M");
//        request.put("model", "llama2:13b");
//        request.put("model", "phi3:medium");
        request.put("prompt", prompt);
        request.put("stream", false);
                request.put("options", Map.of(
                "temperature", 0.3,      // Kreativität (0.1 = präzise, 1.0 = kreativ)
                "num_ctx", 4096          // Kontextlänge (max. Tokens)
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(ollamaUrl, entity, Map.class);

        // Parse the response
        String result = (String) response.getBody().get("response");

        System.out.println(result);
        // Extract structured parts from the response
        Map<String, String> documentation = parseOllamaResponse(result);

        // Return as structured JSON
        Map<String, Object> orderedDoc = new LinkedHashMap<>();
        orderedDoc.put("title", documentation.getOrDefault("title", ""));
        orderedDoc.put("symptom", documentation.getOrDefault("symptom", ""));
        orderedDoc.put("problem", documentation.getOrDefault("problem", ""));
        orderedDoc.put("solution", documentation.getOrDefault("solution", ""));

        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("documentation", orderedDoc);

        return ResponseEntity.ok(responseBody);
    }

    private Map<String, String> parseOllamaResponse(String response) {
        Map<String, String> result = new LinkedHashMap<>();

        // Normalize line endings and clean up the response
        String normalized = response.replace("\r\n", "\n")
                .replaceAll("(?m)^\\s+", "")  // Remove leading whitespace
                .trim();

        // Define the section headers we're looking for
        String[] sections = {"title:", "symptom:", "problem:", "solution:"};

        // Initialize variables to track current section
        String currentSection = null;
        StringBuilder currentContent = new StringBuilder();

        // Process each line
        for (String line : normalized.split("\n")) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Check if this line starts a new section
            boolean isNewSection = false;
            for (String section : sections) {
                if (line.toLowerCase().startsWith(section)) {
                    // Save previous section if exists
                    if (currentSection != null) {
                        result.put(currentSection, currentContent.toString().trim());
                    }

                    // Start new section
                    currentSection = section.replace(":", "");
                    currentContent = new StringBuilder(line.substring(section.length()).trim());
                    isNewSection = true;
                    break;
                }
            }

            // If not a new section, append to current content
            if (!isNewSection && currentSection != null) {
                if (currentContent.length() > 0) {
                    currentContent.append("\n");
                }
                currentContent.append(line);
            }
        }

        // Add the last section
        if (currentSection != null) {
            result.put(currentSection, currentContent.toString().trim());
        }

        // Clean up solution formatting (convert numbered lists to bullets)
        if (result.containsKey("solution")) {
            String solution = result.get("solution");
            solution = solution.replaceAll("(?m)^\\s*\\d+\\.\\s*", "• ");
            result.put("solution", solution);
        }

        return result;
    }
}