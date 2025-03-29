package com.techeazy.linkedIn.controller;

import com.techeazy.linkedIn.dto.LinkedInPostDTO;
import com.techeazy.linkedIn.entity.LinkedInPost;
import com.techeazy.linkedIn.service.LinkedInService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/linkedin")
@RequiredArgsConstructor
public class LinkedInController {

    private final LinkedInService linkedInService;


    @GetMapping("/callback")
    public String handleLinkedInCallback(@RequestParam("code") String code) {
        System.out.println("Authorization Code: " + code);
        return "OAuth Successful!";
    }


    @PostMapping("/createPost")
    public LinkedInPost createPost(@RequestHeader("Authorization") String accessToken,
                                   @RequestParam String userId,
                                   @RequestBody LinkedInPostDTO postDTO) {
        return linkedInService.createPost(userId, postDTO, accessToken.replace("Bearer ", ""));
    }

    @GetMapping("/userProfile")
    public String getUserProfile(@RequestHeader("Authorization") String accessToken,
                                 @RequestParam String userId) {
        return linkedInService.getUserProfile(userId, accessToken.replace("Bearer ", ""));
    }
}

