package com.squad22podA.task_mgt.service.impl;

import com.squad22podA.task_mgt.entity.model.ConfirmationToken;
import com.squad22podA.task_mgt.entity.model.UserModel;
import com.squad22podA.task_mgt.repository.ConfirmationTokenRepository;
import com.squad22podA.task_mgt.repository.UserModelRepository;
import com.squad22podA.task_mgt.service.TokenValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenValidationServiceImpl implements TokenValidationService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserModelRepository userModelRepository;


    @Override
    public String validateToken(String token) {

        Optional<ConfirmationToken> confirmationTokenOptional = confirmationTokenRepository.findByToken(token);
        if (confirmationTokenOptional.isEmpty()) {
            return "Invalid token";
        }

        ConfirmationToken confirmationToken = confirmationTokenOptional.get();

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return "Token has expired";
        }

        UserModel user = confirmationToken.getUser();
        user.setEnabled(true);
        userModelRepository.save(user);

        confirmationTokenRepository.delete(confirmationToken); //delete the token after successful verification

        return "Email confirmed successfully";

    }
}
