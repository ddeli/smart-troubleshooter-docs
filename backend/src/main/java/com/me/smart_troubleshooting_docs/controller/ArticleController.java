package com.me.smart_troubleshooting_docs.controller;

import com.me.smart_troubleshooting_docs.model.Article;
import com.me.smart_troubleshooting_docs.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/save-article")
    public ResponseEntity<Map<String, Object>> saveArticle(@RequestBody Map<String, String> articleData) {
        Article article = new Article();
        article.setTitle(articleData.getOrDefault("title", ""));
        article.setSymptom(articleData.getOrDefault("symptom", ""));
        article.setProblem(articleData.getOrDefault("problem", ""));
        article.setSolution(articleData.getOrDefault("solution", ""));

        articleRepository.save(article);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Article successfully saved!");
        response.put("id", article.getId());

        return ResponseEntity.ok(response);
    }
}