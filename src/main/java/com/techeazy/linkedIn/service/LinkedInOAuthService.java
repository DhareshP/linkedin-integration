package com.techeazy.linkedIn.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class LinkedInOAuthService {

    private final String clientId = "77eg9jdagks0u9";
    private final String clientSecret = "WPL_AP1.Brs0GSENsqqt02Hd.JJO77w==";
    //    private final String redirectUri = "https://linkedin.techeazycounsulting.com/callback";
    private final String redirectUri = "http://localhost:8080/callback";
    private final String tokenUrl = "https://www.linkedin.com/oauth/v2/accessToken";

    private final RestTemplate restTemplate = new RestTemplate();

    Logger log = LoggerFactory.getLogger(LinkedInOAuthService.class);


    public String exchangeCodeForAccessToken(String code) {
        try {
            // Prepare request headers
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

//        public String exchangeCodeForAccessToken(String code) {
//            try {
//                // Prepare request headers
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//                // Prepare request body
////                String requestBody = String.format(
////                        "grant_type=authorization_code&code=%s&redirect_uri=%s&client_id=%s&client_secret=%s",
////                        code, redirectUri, clientId, clientSecret
////                );
//
//                String requestBody = "grant_type=authorization_code" +
//                        "&code=" + code +
//                        "&redirect_uri=" + redirectUri +
//                        "&client_id=" + clientId +
//                        "&client_secret=" + clientSecret;
//
//                // Create HTTP entity
//                HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
//
//                // Send POST request
//                ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, requestEntity, Map.class);
//
//                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//                    return "Access Token: " + response.getBody().get("access_token");
//                } else {
//                    throw new RuntimeException("Failed to retrieve access token. Status: " + response.getStatusCode());
//                }
//            } catch (Exception e) {
//                throw new RuntimeException("Error during token exchange: " + e.getMessage());
//            }
//        }
//    }



//        public String exchangeCodeForAccessToken(String code) {
//            try {
//                // Prepare form data
//                Map<String, String> requestData = new HashMap<>();
//                requestData.put("grant_type", "authorization_code");
//                requestData.put("code", code);
//                requestData.put("redirect_uri", redirectUri);
//                requestData.put("client_id", clientId);
//                requestData.put("client_secret", clientSecret);
//
//                // Convert data to URL-encoded format
//                StringBuilder formData = new StringBuilder();
//                for (Map.Entry<String, String> entry : requestData.entrySet()) {
//                    if (formData.length() > 0) formData.append("&");
//                    formData.append(entry.getKey()).append("=").append(entry.getValue());
//                }
//
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//                HttpEntity<String> requestEntity = new HttpEntity<>(formData.toString(), headers);
//
//                // Send POST request to get the access token
//                ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, requestEntity, Map.class);
//
//                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//                    return "Access Token: " + response.getBody().get("access_token").toString();
//                } else {
//                    return "Failed to retrieve access token. Status: " + response.getStatusCode();
//                }
//
//            } catch (Exception e) {
//                throw new RuntimeException("Error during token exchange: " + e.getMessage());
//            }
//        }
//    }


//    public String exchangeCodeForAccessToken(String code) {
//        try {
//            // Prepare request data
//            Map<String, String> requestData = new HashMap<>();
//            requestData.put("grant_type", "authorization_code");
//            requestData.put("code", code);
//            requestData.put("redirect_uri", redirectUri);
//            requestData.put("client_id", clientId);
//            requestData.put("client_secret", clientSecret);
//
//            // Send POST request to exchange code for access token
//            Map<String, Object> response = restTemplate.postForObject(tokenUrl, requestData, Map.class);
//
//            if (response != null && response.containsKey("access_token")) {
//                return response.get("access_token").toString();
//            } else {
//                throw new RuntimeException("Failed to retrieve access token.");
//            }
//        } catch (HttpClientErrorException e) {
//            throw new RuntimeException("Error during token exchange: " + e.getMessage());
//        }
//    }

