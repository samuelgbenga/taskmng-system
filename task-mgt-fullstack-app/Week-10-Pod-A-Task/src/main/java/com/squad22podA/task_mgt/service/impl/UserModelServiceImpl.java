package com.squad22podA.task_mgt.service.impl;

import com.squad22podA.task_mgt.config.JwtService;
import com.squad22podA.task_mgt.entity.enums.Role;
import com.squad22podA.task_mgt.entity.enums.TokenType;
import com.squad22podA.task_mgt.entity.model.ConfirmationToken;
import com.squad22podA.task_mgt.entity.model.JToken;
import com.squad22podA.task_mgt.entity.model.UserModel;
import com.squad22podA.task_mgt.exception.EmailAlreadyExistsException;
import com.squad22podA.task_mgt.exception.UserNotEnabledException;
import com.squad22podA.task_mgt.exception.UserNotFoundException;
import com.squad22podA.task_mgt.payload.request.*;
import com.squad22podA.task_mgt.payload.response.LoginInfo;
import com.squad22podA.task_mgt.payload.response.LoginResponse;
import com.squad22podA.task_mgt.payload.response.UserProfileResponseDto;
import com.squad22podA.task_mgt.payload.response.UserProfileResponseInfo;
import com.squad22podA.task_mgt.repository.ConfirmationTokenRepository;
import com.squad22podA.task_mgt.repository.JTokenRepository;
import com.squad22podA.task_mgt.repository.UserModelRepository;
import com.squad22podA.task_mgt.service.EmailService;
import com.squad22podA.task_mgt.service.UserModelService;
import com.squad22podA.task_mgt.utils.EmailTemplate;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class UserModelServiceImpl implements UserModelService {


    private final UserModelRepository userModelRepository;
    private final JTokenRepository jTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService emailService;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${baseUrl}")
    private String baseUrl;


    @Override
    public String registerUser(UserRegistrationRequest registrationRequest) throws MessagingException {


        // Validate email format
        String emailRegex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(registrationRequest.getEmail());

        if(!matcher.matches()){
            return "Invalid Email domain";
        }

        String[] emailParts = registrationRequest.getEmail().split("\\.");
        if (emailParts.length < 2 || emailParts[emailParts.length - 1].length() < 2) {
            System.out.println("Invalid email domain. Email parts: " + Arrays.toString(emailParts));
            return "Invalid Email domain";
        }

        if(!registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())){
            throw new IllegalArgumentException("Passwords do not match!");
        }

        Optional<UserModel> existingUser = userModelRepository.findByEmail(registrationRequest.getEmail());

        if(existingUser.isPresent()){
            throw new EmailAlreadyExistsException("Email already exists. Login to your account");
        }


        UserModel newUser = UserModel.builder().firstName(registrationRequest.getFirstName())
                                        .lastName(registrationRequest.getLastName())
                                        .email(registrationRequest.getEmail())
                                        .phoneNumber(registrationRequest.getPhoneNumber())
                                        .password(passwordEncoder.encode(registrationRequest.getPassword()))
                                        .role(Role.USER)
                                        .build();

        UserModel savedUser = userModelRepository.save(newUser);

        ConfirmationToken confirmationToken = new ConfirmationToken(savedUser);
        confirmationTokenRepository.save(confirmationToken);

        String confirmationUrl = EmailTemplate.getVerificationUrl(baseUrl, confirmationToken.getToken());

        //send email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION SUCCESSFUL")
                .build();
        emailService.sendSimpleMailMessage(emailDetails, savedUser.getFirstName(), savedUser.getLastName(), confirmationUrl);
        return "Confirmed Email";

    }

    @Override
    public LoginResponse loginUser(LoginRequestDto loginRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );
        UserModel user = userModelRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + loginRequestDto.getEmail()));

        if (!user.isEnabled()) {
            throw new UserNotEnabledException("User account is not enabled. Please check your email to confirm your account.");
        }

        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return LoginResponse.builder()
                .responseCode("002")
                .responseMessage("Login Successfully")
                .loginInfo(LoginInfo.builder()
                        .email(user.getEmail())
                        .token(jwtToken)
                        .build())
                .build();
    }

    @Override
    public UserProfileResponseDto fetchUserProfile(String email) {
        UserModel user = userModelRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        return UserProfileResponseDto.builder()
                .responseCode("success")
                .responseMessage("Profile fetched Successfully")
                .userProfileResponseInfo(UserProfileResponseInfo.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .build()
                ).build();

    }

    private void saveUserToken(UserModel userModel, String jwtToken) {
        var token = JToken.builder()
                .userModel(userModel)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        jTokenRepository.save(token);
    }

    private void revokeAllUserTokens(UserModel userModel) {
        var validUserTokens = jTokenRepository.findAllValidTokenByUser(userModel.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        jTokenRepository.saveAll(validUserTokens);
    }



}
