package br.com.dswbackend.services;

public interface IEmailService {

  void sendEmail(String toEmail, String text);
}
