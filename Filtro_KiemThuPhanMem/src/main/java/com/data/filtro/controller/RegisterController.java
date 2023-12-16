package com.data.filtro.controller;

import com.data.filtro.exception.AccountNameExistException;
import com.data.filtro.exception.PasswordDoNotMatchException;
import com.data.filtro.repository.UserRepository;
import com.data.filtro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

import static com.data.filtro.service.InputService.*;
import static com.data.filtro.service.InputService.isStringLengthLessThan50;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    UserService userService;

    private String csrfToken;

    @GetMapping
    public String showRegister(Model model) {
        String _csrfToken = generateRandomString();
        csrfToken = _csrfToken;
//        System.out.println("csrfToken:" + _csrfToken);
        model.addAttribute("_csrfToken", _csrfToken);
        return "user/boot1/register";
    }

    @PostMapping
    public String registerUser(@RequestParam("userName") String userName,
                               @RequestParam("accountName") String accountName,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("repeatPassword") String repeatPassword,
                               @RequestParam("_csrfToken") String _csrfToken,
                               Model model) {
        if(containsUTF8(userName) && containsAllowedCharacters(accountName)
                && containsAllowedCharacters(email) && isStringLengthLessThan50(userName)
                && isStringLengthLessThan50(accountName) && isStringLengthLessThan50(password)){
            //        System.out.println("Sau khi nhan nut dang ky thi csrf token la: " + csrfToken);
            if (!_csrfToken.equals(csrfToken)) {
//            System.out.println("csrfToken form: " + _csrfToken);
                String message = "Anti-CSRF token is not correct";
//            System.out.println(message);
                model.addAttribute("errorMessage", message);
                model.addAttribute("_csrfToken", csrfToken);
                return "user/boot1/register";
            }
            System.out.println(password);
            if (password.length() >= 8 && password.matches(".*[A-Z].*")
                    && password.matches(".*\\d.*") && password.matches("^(?=.*[@#$%^&+=]).*$")){
                try {
                    userService.registerUser(userName, accountName, email, password, repeatPassword);
                } catch (AccountNameExistException ex) {
                    model.addAttribute("errorMessage", ex.getMessage());
                    return "user/boot1/register";
                } catch (PasswordDoNotMatchException ex) {
                    model.addAttribute("errorMessage", ex.getMessage());
                    return "user/boot1/register";
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                String message = "Register Successfully! Login now!";
                model.addAttribute("message", message);
                return "user/boot1/register";
            }
            else{
                String message = "The password must have a minimum length of 8 characters, including at least one uppercase letter, at least one digit, and exactly one special character.";
                model.addAttribute("errorMessage", message);
                model.addAttribute("_csrfToken", csrfToken);
                return "user/boot1/register";
            }
        }
        else {
            String message = "Username can only contain lowercase letters; email, name can only contain lowercase letters, and the characters '()', '@', and the length must be more than 8 and less than 50 symbols.";
//            System.out.println(message);
            model.addAttribute("errorMessage", message);
            model.addAttribute("_csrfToken", csrfToken);
            return "user/boot1/register";
        }
    }
    public String generateRandomString() {
        return UUID.randomUUID().toString();
    }
}
