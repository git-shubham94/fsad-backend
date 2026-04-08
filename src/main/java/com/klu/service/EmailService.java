package com.klu.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromAddress;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendWelcomeEmail(String toEmail, String name) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromAddress);
            helper.setTo(toEmail);
            helper.setSubject("🎉 Welcome to the Student Achievement Platform!");
            helper.setText(buildWelcomeHtml(name), true);
            mailSender.send(message);
            System.out.println("✅ Welcome email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send welcome email: " + e.getMessage());
        }
    }

    @Async
    public void sendProfileUpdateEmail(String toEmail, String name) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromAddress);
            helper.setTo(toEmail);
            helper.setSubject("🔄 Your Profile Has Been Updated");
            helper.setText(buildProfileUpdateHtml(name), true);
            mailSender.send(message);
            System.out.println("✅ Profile update email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send profile update email: " + e.getMessage());
        }
    }

    private String buildWelcomeHtml(String name) {
        return """
            <div style="font-family:Arial,sans-serif;max-width:550px;margin:0 auto;border:1px solid #ddd;border-radius:10px;overflow:hidden;">
              <div style="background:linear-gradient(135deg,#534AB7,#7c3aed);padding:30px;text-align:center;">
                <h1 style="color:white;margin:0;font-size:24px;">Welcome to the Platform! 🎓</h1>
              </div>
              <div style="padding:40px 30px;background:#f9fafb;">
                <h2 style="color:#1f2937;margin-top:0;">Hello %s,</h2>
                <p style="color:#4b5563;font-size:16px;line-height:1.5;">
                  Your account has been successfully created on the <strong>Student Achievement Platform</strong>.
                </p>
                <div style="background:white;border-radius:8px;padding:20px;margin:25px 0;box-shadow:0 1px 3px rgba(0,0,0,0.1);">
                  <p style="margin:0;color:#374151;font-weight:bold;">What can you do now?</p>
                  <ul style="color:#6b7280;padding-left:20px;margin-bottom:0;">
                    <li style="margin-top:8px;">Add your latest academic achievements</li>
                    <li style="margin-top:8px;">Log your extracurricular participations</li>
                    <li style="margin-top:8px;">Build your digital resume</li>
                  </ul>
                </div>
                <p style="color:#4b5563;font-size:16px;">We are excited to see you grow!</p>
              </div>
              <div style="background:#f3f4f6;padding:15px;text-align:center;color:#9ca3af;font-size:12px;">
                © 2026 Student Achievement Platform. All rights reserved.
              </div>
            </div>
            """.formatted(name);
    }

    private String buildProfileUpdateHtml(String name) {
        return """
            <div style="font-family:Arial,sans-serif;max-width:550px;margin:0 auto;border:1px solid #ddd;border-radius:10px;overflow:hidden;">
              <div style="background:linear-gradient(135deg,#f59e0b,#d97706);padding:30px;text-align:center;">
                <h1 style="color:white;margin:0;font-size:24px;">Profile Updated 🔄</h1>
              </div>
              <div style="padding:40px 30px;background:#f9fafb;">
                <h2 style="color:#1f2937;margin-top:0;">Hello %s,</h2>
                <p style="color:#4b5563;font-size:16px;line-height:1.5;">
                  This is an automated notification to let you know that your profile details were recently updated by the Administration team.
                </p>
                <div style="background:white;border-left:4px solid #f59e0b;padding:15px 20px;margin:25px 0;box-shadow:0 1px 3px rgba(0,0,0,0.1);">
                  <p style="margin:0;color:#4b5563;font-size:14px;">
                    <strong>Note:</strong> If you believe this update was a mistake, please reach out directly to your department coordinator.
                  </p>
                </div>
                <p style="color:#4b5563;font-size:16px;">You can log in to your dashboard to review the changes anytime.</p>
              </div>
              <div style="background:#f3f4f6;padding:15px;text-align:center;color:#9ca3af;font-size:12px;">
                © 2026 Student Achievement Platform. All rights reserved.
              </div>
            </div>
            """.formatted(name);
    }
}
