package com.techeazy.linkedIn.service;

import com.techeazy.linkedIn.dto.LinkedInPostDTO;
import com.techeazy.linkedIn.entity.LinkedInPost;
import com.techeazy.linkedIn.repository.LinkedInRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LinkedInService {

    private final RestTemplate restTemplate;

    @Autowired
    private final LinkedInRepo postRepository;


    private final String LINKEDIN_API_URL = "https://api.linkedin.com/v2/";

    public LinkedInPost createPost(String userId, LinkedInPostDTO postDTO, String accessToken) {
        String url = LINKEDIN_API_URL + "ugcPosts";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{ \"author\": \"urn:li:person:" + userId + "\", " +
                "\"lifecycleState\": \"PUBLISHED\", " +
                "\"specificContent\": { \"com.linkedin.ugc.ShareContent\": { " +
                "\"shareCommentary\": { \"text\": \"" + postDTO.getContent() + "\" }, " +
                "\"shareMediaCategory\": \"NONE\" } }, " +
                "\"visibility\": { \"com.linkedin.ugc.MemberNetworkVisibility\": \"PUBLIC\" } }";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            LinkedInPost post = new LinkedInPost();
            post.setUserId(userId);
            post.setContent(postDTO.getContent());
            post.setGroupId(postDTO.getGroupId());
            return postRepository.save(post);
        } else {
            throw new RuntimeException("Failed to create LinkedIn post: " + response.getBody());
        }
    }

    public String getUserProfile(String userId, String accessToken) {
        String url = LINKEDIN_API_URL + "me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch user profile: " + response.getBody());
        }
    }
}

