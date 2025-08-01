package com.company.finflow.service;

public interface IEmailService {
    void sendVerificationEmail(String toEmail, String verificationToken);
}
