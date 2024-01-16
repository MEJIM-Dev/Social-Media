package com.me.social.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;

    @Column(nullable = false)
    private Instant creationDate;

    @Column(nullable = false)
    private boolean deleted;
}
