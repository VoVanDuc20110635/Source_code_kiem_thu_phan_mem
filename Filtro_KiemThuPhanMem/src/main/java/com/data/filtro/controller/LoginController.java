package com.data.filtro.controller;

import com.data.filtro.exception.AuthenticationAccountException;
import com.data.filtro.model.*;
import com.data.filtro.repository.AccountRepository;
import com.data.filtro.service.AccountService;
import com.data.filtro.service.CartService;
import com.data.filtro.service.ProductService;
import com.data.filtro.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

import static com.data.filtro.service.InputService.containsAllowedCharacters;

@Controller
@RequestMapping("/login")
public class LoginController {

    private String csrfToken;
    private final AccountService accountService;

    private final CartService cartService;
    private final UserService userService;
    @Autowired
    AccountRepository accountRepository;


    @Autowired
    private ProductService productService;

    @Autowired
    public LoginController(AccountService accountService, UserService userService, CartService cartService) {
        this.accountService = accountService;
        this.userService = userService;
        this.cartService = cartService;
    }

    @GetMapping
    public String show(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            String _csrfToken = generateRandomString();
            csrfToken = _csrfToken;
//        System.out.println("csrfToken:" + _csrfToken);
            model.addAttribute("_csrfToken", _csrfToken);
            return "user/boot1/login";
        }
        else {
            return "redirect:/";
        }
    }

    @PostMapping
    public String login(@RequestParam("accountName") String accountName,
                        @RequestParam("password") String password,
                        @RequestParam("_csrfParameterName") String csrfTokenForm,
                        HttpSession session,
                        Model model) {
        if(!containsAllowedCharacters(accountName) || !containsAllowedCharacters(password)){
            String message = "Username and password can only contain lowercase letters, and the characters (), @.";
            model.addAttribute("errorMessage", message);
            return "redirect:/login";
        }
        if (!csrfTokenForm.equals(csrfToken)) {
            String message = "Anti-CSRF token is not correct!";
            model.addAttribute("errorMessage", message);
            return "redirect:/login";
        }
        Account account;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Account tempAccount = accountService.getAccountByName(accountName.trim());
        if (tempAccount != null) {
            if (passwordEncoder.matches(password, tempAccount.getPassword())) {
                Account authenticateAccount = accountRepository.authenticate(accountName, tempAccount.getPassword());
                account = authenticateAccount;
            } else {
                model.addAttribute("message","Incorrect Password!");
                return "user/boot1/login";
            }
        } else {
            model.addAttribute("message","Incorrect AccountName!");
            return "user/boot1/login";
        }
        if (tempAccount.getRoleNumber() != 3){
            model.addAttribute("message","Incorrect Account");
            return "user/boot1/login";
        }
        User user = userService.getUserById(account.getUser().getId());
        session.setAttribute("account", account);
        session.setAttribute("user", user);
        Cart cart = (Cart) session.getAttribute("cart");
        GuestCart guestCart = (GuestCart) session.getAttribute("guestCart");
        if (guestCart != null) {
            cart = cartService.convertGuestCartToCart(guestCart, user);
            session.removeAttribute("guestCart");
        }
        return "redirect:/";
    }

    @GetMapping("/session")
    public String check(HttpSession session) {
//        Account account = (Account) session.getAttribute("account");
//        System.out.println("session lay duoc la: " + account.getAccountName());
        return "session";
    }
    public String generateRandomString() {
        return UUID.randomUUID().toString();
    }
}
