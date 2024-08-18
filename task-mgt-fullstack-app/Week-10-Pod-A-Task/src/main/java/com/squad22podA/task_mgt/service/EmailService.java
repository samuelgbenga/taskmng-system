package com.squad22podA.task_mgt.service;

import com.squad22podA.task_mgt.payload.request.EmailDetails;
import jakarta.mail.MessagingException;

public interface EmailService {

    void sendEmailAlert(EmailDetails emailDetails);

    void sendSimpleMailMessage(EmailDetails message, String firstName, String lastName, String link) throws MessagingException;
}
