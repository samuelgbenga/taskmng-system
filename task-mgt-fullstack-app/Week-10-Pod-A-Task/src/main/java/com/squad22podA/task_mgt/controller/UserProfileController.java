package com.squad22podA.task_mgt.controller;


import com.squad22podA.task_mgt.payload.response.UserProfileResponseDto;
import com.squad22podA.task_mgt.service.UserModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserModelService userModelService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponseDto> getUserProfile() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();// Get authenticated user's email from token
        UserProfileResponseDto userProfile = userModelService.fetchUserProfile(email);
        return ResponseEntity.ok(userProfile);
    }
}
