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



    @PostMapping("/createPost")
    public LinkedInPost createPersonalPost(@RequestParam String userId,
                                           @RequestParam String accessToken,
                                           @RequestBody LinkedInPostDTO postDTO) {
        return linkedInService.createPost(userId, postDTO, accessToken);
    }

    @PostMapping("/createOrgPost")
    public LinkedInPost createOrganizationPost(@RequestParam String orgId,
                                               @RequestParam String accessToken,
                                               @RequestBody LinkedInPostDTO postDTO) {
        // Reuse the createPost method by passing the organization URN as the userId parameter.
        String organizationUrn = orgId; // assume orgId already contains the organization URN if not, prepend "urn:li:organization:"
        return linkedInService.createPost(organizationUrn, postDTO, accessToken);
    }

    @PostMapping("/createGroupPost")
    public LinkedInPost createGroupPost(@RequestParam String groupId,
                                        @RequestParam String accessToken,
                                        @RequestBody LinkedInPostDTO postDTO) {
        // For groups, we assume the group URN is "urn:li:group:" + groupId
        String groupUrn = "urn:li:group:" + groupId;
        // You can reuse the same createPost method.
        // Note: LinkedIn Groups API is restricted – ensure your app is allowed to post in groups.
        return linkedInService.createPost(groupUrn, postDTO, accessToken);
    }

    @GetMapping("/me")
    public String getMyProfile() {
        return linkedInService.getMyProfile();
    }

    @GetMapping("/profile")
    public String getUserProfile(@RequestParam String userId, @RequestParam String accessToken) {
        return linkedInService.getUserProfile(userId, accessToken);
    }

    @DeleteMapping("/deletePost")
    public String deletePost(@RequestParam String postUrn) {
        return linkedInService.deletePost(postUrn);
    }

    @PutMapping("/updatePost")
    public LinkedInPost updatePost(@RequestParam String postUrn,
                                   @RequestParam String userId,
                                   @RequestParam String accessToken,
                                   @RequestParam String newMessage) {
        String authorUrn = "urn:li:person:" + userId;
        return linkedInService.updatePost(postUrn, newMessage, authorUrn);
    }
}

