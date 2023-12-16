package com.data.filtro.api;

import com.data.filtro.model.Account;
import com.data.filtro.model.Cart;
import com.data.filtro.model.ErrorResponse;
import com.data.filtro.model.User;
import com.data.filtro.service.AccountService;
import com.data.filtro.service.CartService;
import com.data.filtro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/user")
public class UserAPIController {

    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;
    @Autowired
    private AccountService accountService;


    @GetMapping("/find/{id}")
    public ResponseEntity<?> findUser(@PathVariable("id") int id) {
        try {
            User user = userService.getByUserId(id);
            if (user == null) {
                String message = "No user found!";
                ErrorResponse err = new ErrorResponse(message, HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/find/getAll")
    public ResponseEntity<?> getAll() {
        try {
            List<User> users = userService.getAll();
            if (users == null) {
                String message = "No users found!";
                ErrorResponse err = new ErrorResponse(message, HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    @PostMapping("/editUserInfo")
    public ResponseEntity<?> editUserInfo(@RequestParam int userId,
                                          @RequestParam String name,
                                          @RequestParam String sex,
                                          @RequestParam String address,
                                          @RequestParam int zipcode,
                                          @RequestParam String email,
                                          @RequestParam String phonenumber){
        try{
            User user = userService.getByUserId(userId);
            user.setName(name);
            user.setSex(sex);
            user.setAddress(address);
            user.setZip(zipcode);
            user.setEmail(email);
            user.setPhoneNumber(phonenumber);
            userService.update(user);
            return new ResponseEntity<>("Đã cập nhật user", HttpStatus.OK);
        }
        catch (NoSuchElementException ex){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }
    @PostMapping("/editRole")
    public ResponseEntity<?> editUserInfo(@RequestParam int userId,
                                          @RequestParam int role){
        try{
            User user = userService.getByUserId(userId);
            Account account = user.getAccount();
            account.setRoleNumber(role);
            accountService.updateRole(account);

            return new ResponseEntity<>("Đã cập nhật user", HttpStatus.OK);
        }
        catch (NoSuchElementException ex){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }


}
