package com.example.week6.model;

import java.io.Serializable;

public class UserBody implements Serializable {
    private User user;

    public UserBody(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
