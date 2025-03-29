package com.techeazy.linkedIn.controller;

import com.techeazy.linkedIn.service.LinkedInOAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {

    @Autowired
    private LinkedInOAuthService linkedInOAuthService;

    @GetMapping("/callback")
    public String handleLinkedInCallback(@RequestParam("code") String code) {
        try {
            String accessToken = linkedInOAuthService.exchangeCodeForAccessToken(code);
            return "OAuth Successful! Access Token: " + accessToken;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
