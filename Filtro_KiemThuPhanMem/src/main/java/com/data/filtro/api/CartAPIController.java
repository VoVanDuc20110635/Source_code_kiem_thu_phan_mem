package com.data.filtro.api;

import com.data.filtro.model.Cart;
import com.data.filtro.model.CartItem;
import com.data.filtro.model.ErrorResponse;
import com.data.filtro.model.User;
import com.data.filtro.service.CartItemService;
import com.data.filtro.service.CartService;
import com.data.filtro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartAPIController {
    @Autowired
    CartService cartService;
    @Autowired
    CartItemService cartItemService;

    @Autowired
    UserService userService;

    @GetMapping("/getUserCart/{id}")
    public ResponseEntity<?> getUserCart(@PathVariable int id) {
        User user = userService.getByUserId(id);
        if (user == null) {
            String message = "User not found!";
            ErrorResponse err = new ErrorResponse(message, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        } else {
            Cart cart = cartService.getCartByUserId(user.getId());
            if (cart == null) {
                cart = cartService.createCart(user);
            }
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }
    }

    @GetMapping("/getUserCartItems/{id}")
    public ResponseEntity<?> getUserCartItems(@PathVariable int id){
        User user = userService.getByUserId(id);
        if (user == null) {
            String message = "User not found!";
            ErrorResponse err = new ErrorResponse(message, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        } else {
            Cart cart = cartService.getCartByUserId(user.getId());
            List<CartItem> cartItemList = cart.getCartItemList();
            return new ResponseEntity<>(cartItemList, HttpStatus.OK);
        }
    }

//    @PostMapping("/addToCart/{id}")
//    public ResponseEntity<?> addToCart(@PathVariable int id,
//                                       @RequestParam int quantity,
//                                       @RequestParam int userId){
//        Cart cart = new Cart();
//    }
@PostMapping("/addToCart")
public ResponseEntity<?> addToCart(@RequestParam int userId,
                                   @RequestParam int productId,
                                   @RequestParam int quantity) {
    User user = userService.getByUserId(userId);
    Cart cart;
    if (user != null) {
        cart = cartService.getCartByUserId(userId);
        if (cart == null) {
            cart = cartService.createCart(user);
        }
        cartService.addProductToCart(cart, productId, quantity);
        return new ResponseEntity<>("San pham " + productId + "da duoc them vao gio hang cua user " + userId + "voi so luong " + quantity, HttpStatus.OK);
    } else {
        return new ResponseEntity<>("Khong tim thay user thich hop", HttpStatus.BAD_REQUEST);
    }
}

    @DeleteMapping("/removeCartItem")
    public ResponseEntity<?> removeCartItem(@RequestParam int userId, @RequestParam int productId) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null) {
            return new ResponseEntity<>("Khong co cart can tim!", HttpStatus.BAD_REQUEST);
        } else {
            cartItemService.removeCartItemByCartIdAndProductId(cart.getId(), productId);
            return new ResponseEntity<>("Da xoa san pham " + productId + " ra khoi gio hang " + cart.getId(), HttpStatus.OK);
        }
    }

}
