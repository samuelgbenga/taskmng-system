package com.squad22podA.task_mgt.controller;


import com.squad22podA.task_mgt.payload.request.LoginRequestDto;
import com.squad22podA.task_mgt.payload.response.LoginResponse;
import com.squad22podA.task_mgt.payload.request.UserRegistrationRequest;
import com.squad22podA.task_mgt.service.UserModelService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserModelController {

    private final UserModelService userModelService;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest registrationRequest) {

        try{
            String registerUser  = userModelService.registerUser(registrationRequest);
            if(!registerUser.equals("Invalid Email domain")){
                return ResponseEntity.ok("User registered successfully. Please check your email to confirm your account");
            }else {
                return ResponseEntity.badRequest().body("Invalid Email!!!");
            }

        } catch (IllegalArgumentException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequestDto loginRequestDto){

            return ResponseEntity.ok(userModelService.loginUser(loginRequestDto));

    }

}
