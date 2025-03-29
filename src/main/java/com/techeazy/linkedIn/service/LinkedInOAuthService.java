package com.techeazy.linkedIn.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class LinkedInOAuthService {

    @Value("${linkedin.access.token}")
    private String accessToken;

    @Value("${linkedin.clientId}")
    private String clientId;

    @Value("${linkedin.clientSecret}")
    private String clientSecret;

    @Value("${linkedin.redirectUri}")
    private String redirectUri = "https://oauth.pstmn.io/v1/callback";

    @Value("${linkedin.tokenUrl}")
    private String tokenUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    Logger log = LoggerFactory.getLogger(LinkedInOAuthService.class);


    public String exchangeCodeForAccessToken(String code) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Correct way to pass form-data
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("grant_type", "authorization_code");
            requestBody.add("code", code);
            requestBody.add("redirect_uri", redirectUri);
            requestBody.add("client_id", clientId);
            requestBody.add("client_secret", clientSecret);

            log.info("Request body : " + requestBody);
            log.info("Code : " +  code);

            // Create HTTP entity
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

            // Send POST request
            ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, requestEntity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return "Access Token: " + response.getBody().get("access_token");
            } else {
                throw new RuntimeException("Failed to retrieve access token. Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during token exchange: " + e.getMessage());
        }
    }
}