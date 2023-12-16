package com.data.filtro.controller;

import com.data.filtro.Util.Utility;
import com.data.filtro.exception.UserNotFoundException;
import com.data.filtro.model.Account;
import com.data.filtro.service.AccountService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.mail.javamail.*;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Controller
public class PasswordResetController {
    public static int statusAPIResetPassword;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/forgot-password")
    public String showForgotPassword() {

        return "user/boot1/forgotPassword";
    }


    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, HttpServletRequest request, Model model) {
        String token = UUID.randomUUID().toString();
        try {
            accountService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset-password?token=" + token;
            sendMail(email, resetPasswordLink);
            model.addAttribute("successMessage", "The email has been sent with a link to reset your password.!");
        } catch (UserNotFoundException ex) {
            model.addAttribute("message", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException exception) {
            model.addAttribute("message", "Error sending email");
        }
        return "user/boot1/forgotPassword";
    }


    public void sendMail(String recipentAddress, String link) throws MessagingException, UnsupportedEncodingException {
        JavaMailSender mailSender = new JavaMailSenderImpl();
        ((JavaMailSenderImpl) mailSender).setHost("smtp.gmail.com");
        ((JavaMailSenderImpl) mailSender).setPort(587);
        ((JavaMailSenderImpl) mailSender).setUsername("voduc0100@gmail.com");
        ((JavaMailSenderImpl) mailSender).setPassword("tvhnwfasjnjhbcro");
        System.out.println("tao doi tuong MimeMessage");
        MimeMessage message = mailSender.createMimeMessage();
        System.out.println("gan gia tri helper");
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("contact@ark.com", "Arkadian");
        helper.setTo(recipentAddress);
        String subject = "Reset password Four Leaves Shoes";
        String content = "\"The link below is used to reset the password\n" + link;
        helper.setSubject(subject);
        helper.setText(content);
        System.out.println("gan xong gia tri helper");
        System.out.println("gui gmail");
        mailSender.send(message);
    }

    @GetMapping("/reset-password")
    public String showPasswordReset(@RequestParam(value = "token") String token, Model model) {
        statusAPIResetPassword = 1;
        Account account = accountService.getByPasswordResetToken(token);
        model.addAttribute("token", token);
        if (account == null) {
            model.addAttribute("message", "Token không hợp lệ");
            return "user/boot1/passwordReset";
        }
        return "user/boot1/passwordReset";
    }


    @PostMapping("/reset-password")
    public String processPasswordReset(@RequestParam("token") String token,
                                       @RequestParam("password") String password,
                                       Model model) {
        Account account = accountService.getByPasswordResetToken(token);
        if (account == null) {
            model.addAttribute("message", "Token không hợp lệ");
            return "user/boot1/passwordReset";
        } else {
            accountService.updatePassword(account, password);
            model.addAttribute("message", "Đổi mật khẩu thành công!");
        }
        return "user/boot1/passwordReset";
    }


}
