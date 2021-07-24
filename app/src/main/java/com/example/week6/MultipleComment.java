package com.example.week6;

import java.util.List;

public class MultipleComment {
    private List<Comment> comments;

    public MultipleComment(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
