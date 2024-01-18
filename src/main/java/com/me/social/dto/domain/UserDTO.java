package com.me.social.dto.domain;


import com.me.social.domain.Post;
import com.me.social.domain.User;
import com.me.social.domain.enumeration.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
public class UserDTO {
    private long id;

    private String email;

    private String username;

    private String password;

    private String profilePicture;

    private List<PostDTO> posts = new ArrayList<>();

    private Set<UserDTO> following = new HashSet<>();

    private String firstName;

    private String lastName;

    private Gender sex;

    private String otherNames;

    private boolean active;

    private Instant createdAt;

    private boolean deleted;

    private long followersCount;
}
