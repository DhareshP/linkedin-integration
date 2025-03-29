package com.techeazy.linkedIn.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "linkedin_posts")
public class LinkedInPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String content;
    private String groupId;
}