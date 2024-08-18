package com.squad22podA.task_mgt.service;

import com.squad22podA.task_mgt.payload.request.LoginRequestDto;
import com.squad22podA.task_mgt.payload.response.LoginResponse;
import com.squad22podA.task_mgt.payload.request.UserRegistrationRequest;
import com.squad22podA.task_mgt.payload.response.UserProfileResponseDto;
import jakarta.mail.MessagingException;

public interface UserModelService {

    String registerUser(UserRegistrationRequest registrationRequest) throws MessagingException;

    LoginResponse loginUser(LoginRequestDto loginRequestDto);

    UserProfileResponseDto fetchUserProfile(String email);
}
