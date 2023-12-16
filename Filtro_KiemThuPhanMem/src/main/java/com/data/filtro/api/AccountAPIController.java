package com.data.filtro.api;

import com.data.filtro.Util.Utility;
import com.data.filtro.exception.AccountNameExistException;
import com.data.filtro.exception.PasswordDoNotMatchException;
import com.data.filtro.exception.UserNotFoundException;
import com.data.filtro.model.*;
import com.data.filtro.service.AccountService;
import com.data.filtro.service.CartService;
import com.data.filtro.service.EmailValidCodeService;
import com.data.filtro.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.checkerframework.checker.units.qual.A;
import java.io.UnsupportedEncodingException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@RestController
@RequestMapping("/api/account")
public class AccountAPIController {

    @Autowired
    AccountService accountService;

    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    EmailValidCodeService emailValidCodeService;
    @GetMapping("/authenticate")
    public ResponseEntity<Account> authenticate(@RequestParam String accountName, @RequestParam String password) {
        Account account = accountService.authenticateUser(accountName, password);
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> find(@PathVariable int id) {
        Account account = accountService.getAccountById(id);
        if (account == null) {
            String message = "No account found!";
            ErrorResponse err = new ErrorResponse(message, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(account, HttpStatus.OK);
    }
    @GetMapping("/findUserByAccountId")
    public ResponseEntity<?> findUserByAccountId(@RequestParam int accountId){
        User user = userService.getUserByAccountId(accountId);
        if (user == null){
            String message = "No user found!";
            ErrorResponse err = new ErrorResponse(message, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String userName,
                                      @RequestParam String accountName,
                                      @RequestParam String email,
                                      @RequestParam String password,
                                      @RequestParam String repeatPassword) {
        System.out.println("keke");
        System.out.println(userName + " " + accountName  + " " + email + " " + password + " " + repeatPassword);
        try {
            userService.registerUser(userName, accountName, email, password, repeatPassword);
            String message = "Tạo tài khoản thành công !";
            System.out.println(message);
            ErrorResponse err = new ErrorResponse(message, HttpStatus.OK.value());
            return new ResponseEntity<>(err, HttpStatus.OK);
        } catch (AccountNameExistException ex) {
            String message = "Tài khoản đã tồn tại";
            System.out.println(message);
            ErrorResponse err = new ErrorResponse(message, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(err, HttpStatus.OK);
        } catch (PasswordDoNotMatchException ex) {
            String message = "Mật khẩu không đúng";
            System.out.println(message);
            ErrorResponse err = new ErrorResponse(message, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(err, HttpStatus.OK);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> processForgotPassword (@RequestParam String email){
        String token = UUID.randomUUID().toString();
        System.out.println(email);
        String message ="";
        try {
            accountService.updateResetPasswordToken(token, email);
            System.out.println("Da update reset token");
            Random random = new Random();
            int randomNumber = random.nextInt(9000) + 1000; // tạo số ngẫu nhiên từ 1000 đến 9999
            String validCode = String.valueOf(randomNumber);
            System.out.println("Validcode: " +  validCode);
            EmailValidCode emailValidCode1 = emailValidCodeService.findByEmail(email);
            //System.out.println(emailValidCode1.getEmail() + " "+ emailValidCode1.getValidCode());

            if (emailValidCodeService.findByEmail(email) == null){
                EmailValidCode emailValidCode = new EmailValidCode();
                emailValidCode.setEmail(email);
                emailValidCode.setValidCode(validCode);
                emailValidCodeService.create(emailValidCode);
            }
            else {
                EmailValidCode emailValidCode = new EmailValidCode();
                emailValidCode.setEmail(email);
                emailValidCode.setValidCode(validCode);
                emailValidCodeService.changeValidCode(emailValidCode);
            }

            sendMail(email, validCode);
            message= "Da gui ma xac thuc thong qua email thanh cong";
            ErrorResponse err = new ErrorResponse(message, HttpStatus.OK.value());
            return new ResponseEntity<>(err, HttpStatus.OK);
        } catch (UnsupportedEncodingException | MessagingException exception) {
            message="Loi khi gui email";
            ErrorResponse err = new ErrorResponse(message, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }
    }

    public void sendMail(String recipentAddress, String validCode) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("contact@ark.com", "Arkadian");
        helper.setTo(recipentAddress);
        String subject = "Đặt lại mật khẩu";
        String content = "Mã xác thực sau đây dùng để đặt lại mật khẩu: "  + validCode;
        helper.setSubject(subject);
        helper.setText(content);
        mailSender.send(message);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> processPasswordReset(@RequestParam("email") String email,
                                       @RequestParam("code" ) String code,
                                       @RequestParam("password") String password,
                                       @RequestParam("repeatpassword") String repeatpassword) {
        System.out.println();
        System.out.println(email);
        System.out.println(code);
        System.out.println(password);
        System.out.println(repeatpassword);
        String message= "";
        EmailValidCode emailValidCode1 = emailValidCodeService.findByEmail(email);
        System.out.println(emailValidCode1.getEmail() + " "+ emailValidCode1.getValidCode());


        Account account = accountService.getbyEmail(email);
        EmailValidCode emailValidCode = emailValidCodeService.findByEmail(email);

        if (account == null) {
            message="tai khoan khong ton tai!";
            ErrorResponse err = new ErrorResponse(message, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        } else {
            if (emailValidCode1!= null && emailValidCode1.getValidCode().equals(code)  && password.equals(repeatpassword)){
                System.out.println(emailValidCode1.getValidCode());
                accountService.updatePassword(account, password);
                message="doi mat khau thanh cong!";
                ErrorResponse err = new ErrorResponse(message, HttpStatus.OK.value());
                return new ResponseEntity<>(err, HttpStatus.OK);
            }
            else {
                message="doi mat khau khong thanh cong do code khong dung!";
                ErrorResponse err = new ErrorResponse(message, HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
            }
        }
    }


}
