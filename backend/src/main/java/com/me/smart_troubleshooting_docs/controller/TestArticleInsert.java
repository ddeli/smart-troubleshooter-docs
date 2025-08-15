package com.me.smart_troubleshooting_docs.controller;

import com.me.smart_troubleshooting_docs.model.Article;
import com.me.smart_troubleshooting_docs.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class TestArticleInsert {

    @Autowired
    private ArticleRepository articleRepository;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/test-insert")
    public ResponseEntity<String> testInsert() {
        Article testArticle = new Article();
        testArticle.setTitle("Test Article");
        testArticle.setSymptom("Test Symptom");
        testArticle.setProblem("Test Problem");
        testArticle.setSolution("Test Solution");

        articleRepository.save(testArticle);

        return ResponseEntity.ok("Insert Testarticle: " + testArticle.getId());
    }
}