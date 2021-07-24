package com.example.week6.model;

import com.example.week6.model.Article;

import java.util.List;

public class MultipleArticle {
    private List<Article> articles;

    public MultipleArticle(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
