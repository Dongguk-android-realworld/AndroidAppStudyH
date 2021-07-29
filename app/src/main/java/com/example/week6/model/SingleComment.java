package com.example.week6.model;

public class SingleComment {
    private Comment comment;

    public SingleComment(Comment comment) {
        this.comment = comment;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
