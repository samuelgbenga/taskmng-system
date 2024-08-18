package com.squad22podA.task_mgt.service.impl;

import com.squad22podA.task_mgt.payload.request.EmailDetails;
import com.squad22podA.task_mgt.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine tEngine;

    @Value("${spring.mail.username}")
    private String senderEmail;


    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {

        try {

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setFrom(senderEmail);
            simpleMailMessage.setTo(emailDetails.getRecipient());
            simpleMailMessage.setText(emailDetails.getMessageBody());
            simpleMailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(simpleMailMessage);
            System.out.println("Mail sent successfully!!");

        }catch (MailException exception){
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void sendSimpleMailMessage(EmailDetails message, String firstName, String lastName, String link) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        Map<String, Object> variables = Map.of(
                "name", firstName + " " + lastName,
                "link", link
        );
        context.setVariables(variables);
        helper.setFrom(senderEmail);
        helper.setTo(message.getRecipient());
        helper.setSubject(message.getSubject());
        String html = tEngine.process("email-verification", context);
        helper.setText(html, true);

        javaMailSender.send(msg);
        log.info("Sending email: to {}",message.getRecipient());
    }
}
