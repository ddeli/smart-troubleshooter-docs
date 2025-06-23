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
        String additionalPrompt = reader.readTextFileAsString("additionalPrompt.txt");
        System.out.println(additionalPrompt);

        String inputText = body.getOrDefault("text", "");

        String prompt = """
                You are a professional technical writer.
                
                    Your task is to transform the given text into a formal, structured technical documentation article using the following structure:
                
                    ---
                    title:\s
                    symptom:\s
                    problem:\s
                    solution:
                    ---
                
                    Before writing, follow these steps **exactly**:
                
                    1. Detect the primary language of the input text.
                    2. Use that same language for your entire output.
                    3. If the input is written in German (even partially), respond completely in German.
                    4. Do not use any other language than the one used in the input. 
                    
                    INPUT TEXT STARTS HERE:
                    %s
                    INPUT TEXT ENDS HERE:
                    
                    5. Keep all English technical terms in their original form, regardless of the input language.
                    6. Write professionally, without casual or conversational tone.
                    7. If no solution is provided in the input, infer a logical one based on the problem.
                    8. Avoid redundancy across sections.
                    
                    %s
                    
                    IMPORTANT: Under no circumstances include or copy any content, phrases, or sentences from the example section (if any available) above in your output. Your response must be fully original and based solely on the input text.
                    Only return the documentation, nothing else.
                
                    
                
                    Generate the structured documentation now:
            """.formatted(inputText,additionalPrompt);

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
                "temperature", 0.3,      // creativity (0.1 = precise, 1.0 = creative)
                "num_ctx", 8192          // context (max. tokens)
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