package com.me.social.config;

public interface ApplicationUrl {

    String BASE_URL = "/api/v1";
    String REGISTRATION = "/register";
    String USERS = "/users";
    String USER = "/user/{id}";
    String POSTS = "/posts";
    String POST = "/post/{id}";
    String USER_POSTS = "/posts/user";
    String AUTH_BASE_URL = "/api/v1/auth";
    String LOGIN = "/login";
    String FOLLOW = "/follow";
    String LIKE = "/post/{id}/react";
    String COMMENTS = "/comments";
    String COMMENT = "/comment/{id}";
    String POST_COMMENTS = "/post/{id}/comments";
}
