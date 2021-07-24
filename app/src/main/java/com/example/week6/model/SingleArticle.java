package com.example.week6.model;

public class SingleArticle {
    private Article article;

    public SingleArticle(Article article) {
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
