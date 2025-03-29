package com.techeazy.linkedIn.service;

import com.techeazy.linkedIn.dto.LinkedInPostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;


@Service
@RequiredArgsConstructor
public class LinkedInService {

    private final RestTemplate restTemplate;

    @Value("${linkedin.access.token}")
    private String accessToken;

    @Value("${linkedin.api.url}")
    private String LINKEDIN_API_URL;

    public String createPost(String userId, LinkedInPostDTO postDTO, String accessToken) {
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
            return response.getBody();
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

    public String getMyProfile() {
        String url = LINKEDIN_API_URL + "me?projection=(id,localizedFirstName,localizedLastName,profilePicture(displayImage~:playableStreams))";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    public String deletePost(String postUrn) {
        String url = LINKEDIN_API_URL +  "ugcPosts/" + postUrn;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class);
        if(response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return "Post deleted successfully.";
        }
        return "Failed to delete post.";
    }

    public String updatePost(String postUrn, String newMessage, String authorUrn) {
        try {
            String url = LINKEDIN_API_URL + "ugcPosts/" + postUrn;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestBody = "{ " +
                    "\"author\": \"" + authorUrn + "\", " +
                    "\"lifecycleState\": \"PUBLISHED\", " +
                    "\"specificContent\": { \"com.linkedin.ugc.ShareContent\": { " +
                    "\"shareCommentary\": { \"text\": \"" + newMessage + "\" }, " +
                    "\"shareMediaCategory\": \"NONE\" } }, " +
                    "\"visibility\": { \"com.linkedin.ugc.MemberNetworkVisibility\": \"PUBLIC\" }" +
                    "}";

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return "Post updated successfully. Response: " + response.getBody();
            } else {
                return "Failed to update the post: " + response.getBody();
            }
        } catch (Exception e) {
            return "Exception occurred: " + e.getMessage();
        }
    }
}

