package com.me.smart_troubleshooting_docs.repository;

import com.me.smart_troubleshooting_docs.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
}