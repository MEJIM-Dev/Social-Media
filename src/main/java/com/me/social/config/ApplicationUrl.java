package com.me.social.config;

public interface ApplicationUrl {

    String BASE_URL = "api/v1";
    String REGISTRATION = "/register";
    String USERS = "/users";
    String USER = "/user/{id}";
    String POSTS = "/posts";
    String POST = "/post/{id}";
    String USER_POSTS = "/posts/user/{userId}";
}
