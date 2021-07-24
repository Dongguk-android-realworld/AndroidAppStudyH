package com.example.week6.model;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String password;
    private String token;
    private String username;
    private String bio;
    private String image;

    // 로그인용
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // 회원가입용
    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public User(String email, String password, String token, String username, String bio, String image) {
        this.email = email;
        this.password = password;
        this.token = token;
        this.username = username;
        this.bio = bio;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
