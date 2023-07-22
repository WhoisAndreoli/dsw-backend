package br.com.dswbackend.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class JavaEmailService implements IEmailService {

  private JavaMailSender mailSender;

  public JavaEmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Override
  public void sendEmail(String toEmail, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("${APP_USERNAME}");
    message.setTo(toEmail);
    message.setText(text);
    message.setSubject("Recuperação de senha");

    mailSender.send(message);
  }
}
