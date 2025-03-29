package com.techeazy.linkedIn.controller;

import com.techeazy.linkedIn.dto.LinkedInPostDTO;
import com.techeazy.linkedIn.service.LinkedInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/linkedin")
@RequiredArgsConstructor
public class LinkedInController {

    private final LinkedInService linkedInService;



    @PostMapping("/createPost")
    public ResponseEntity<String> createPersonalPost(@RequestParam String userId,
                                                     @RequestParam String accessToken,
                                                     @RequestBody LinkedInPostDTO postDTO) {
        String response = linkedInService.createPost(userId, postDTO, accessToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/createOrgPost")
    public ResponseEntity<String> createOrganizationPost(@RequestParam String orgId,
                                                         @RequestParam String accessToken,
                                                         @RequestBody LinkedInPostDTO postDTO) {
        String response = linkedInService.createPost(orgId, postDTO, accessToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/createGroupPost")
    public ResponseEntity<String> createGroupPost(@RequestParam String groupId,
                                                  @RequestParam String accessToken,
                                                  @RequestBody LinkedInPostDTO postDTO) {
        String groupUrn = "urn:li:group:" + groupId;
        String response = linkedInService.createPost(groupUrn, postDTO, accessToken);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<String> getMyProfile() {
        String response = linkedInService.getMyProfile();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<String> getUserProfile(@RequestParam String userId, @RequestParam String accessToken) {
        String response = linkedInService.getUserProfile(userId, accessToken);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deletePost")
    public ResponseEntity<String> deletePost(@RequestParam String postUrn) {
        String response = linkedInService.deletePost(postUrn);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/updatePost")
    public ResponseEntity<String> updatePost(@RequestParam String postUrn,
                                             @RequestParam String userId,
                                             @RequestParam String accessToken,
                                             @RequestParam String newMessage) {
        String authorUrn = "urn:li:person:" + userId;
        String response = linkedInService.updatePost(postUrn, newMessage, authorUrn);
        return ResponseEntity.ok(response);
    }
}
