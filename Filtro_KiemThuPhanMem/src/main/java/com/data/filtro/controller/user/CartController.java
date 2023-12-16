package com.data.filtro.controller.user;

import com.data.filtro.model.*;
import com.data.filtro.repository.CartItemRepository;
import com.data.filtro.repository.CartRepository;
import com.data.filtro.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {


    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CartService cartService;

    @Autowired
    GuestCartService guestCartService;

    @Autowired
    CartItemService cartItemService;

    private String[] productIdArray;
    private String[] quantityArray;

    private Product tempProduct = new Product();


    @GetMapping
    public String showCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        GuestCart guestCart = (GuestCart) session.getAttribute("guestCart");

        if (user != null) {
            Cart cart = cartService.getCurrentCartByUserId(user.getId());
            if (cart != null) {
                List<CartItem> cartItemList = cart.getCartItemList();
                model.addAttribute("cartItemList", cartItemList);
                model.addAttribute("cart", cart);
            }
        } else if (guestCart != null) {
            List<CartItem> cartItemList = guestCart.getCartItemList();
            model.addAttribute("cartItemList", cartItemList);
            model.addAttribute("guestCart", guestCart);
        } else {
            model.addAttribute("cartItemList", new ArrayList<CartItem>());
            model.addAttribute("message", "Không có sản phẩm nào trong giỏ hàng!");
        }
        return "user/boot1/cart";
    }

    @PostMapping("/add")
    public String addCart(@RequestParam("productId") int productId, @RequestParam("quantity") int quantity, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        GuestCart guestCart = (GuestCart) session.getAttribute("guestCart");
        Cart cart = null;
        if (user != null) {
            cart = cartService.getCurrentCartByUserId(user.getId());
            if (cart == null) {
                cart = cartService.createCart(user);
                session.setAttribute("cart", cart);
            }
            Product product = productService.getProductById(productId);
            if (product == null) {
                throw new RuntimeException("Không tìm thấy sản phẩm!");
            }
            List<CartItem> cartItemList = cart.getCartItemList();
            int temp = 0;
            boolean isExist = false;
            if (cartItemList.size() > 0){
                for (CartItem cartItem : cartItemList) {
                    if (cartItem.getProduct().getId() == productId) {
                        cartItem.setQuantity(quantity);
                        cartItem.setTotal(product.getPrice() * quantity);
                        cartItemRepository.save(cartItem);
                        isExist = true;
                        break;
                    }
                    temp = temp + 1;
                    if (temp == cartItemList.size()) break;
                }
            }
            if (isExist == false){
                CartItem newCartItem = new CartItem();
                newCartItem.setProduct(product);
                newCartItem.setPrice(product.getPrice());
                newCartItem.setQuantity(quantity);
                newCartItem.setTotal(product.getPrice() * quantity);
                newCartItem.setPurchasedDate(new Date());
                newCartItem.setCart(cart);
                cart.getCartItemList().add(newCartItem);

                cart.setUpdatedDate(newCartItem.getPurchasedDate());
                cartRepository.save(cart);
            }
        }
        else if (guestCart != null) {
            cartService.addProductToGuestCart(guestCart, productId, quantity);
            return "redirect:/cart";
        } else {
            if (guestCart == null) {
                guestCart = cartService.createGuestCart();
                session.setAttribute("guestCart", guestCart);
            }
            cartService.addProductToGuestCart(guestCart, productId, quantity);
        }
        return "redirect:/cart";
    }


    @PostMapping("/remove/{productId}")
    public String removeCartItem(@PathVariable("productId") int productId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        GuestCart guestCart = (GuestCart) session.getAttribute("guestCart");
        if (user != null) {
            Cart cart = cartService.getCurrentCartByUserId(user.getId());
            cartItemService.removeCartItemByCartIdAndProductId(cart.getId(), productId);
        } else if (guestCart != null) {
            cartItemService.removeCartItemByGuestCartIdAndProductId(guestCart.getId(), productId);
            guestCart.setCartItemList(cartItemService.getCartItemByGuestCartId(guestCart.getId()));
            session.setAttribute("guestCart", guestCart);
        }
        return "redirect:/cart";
    }


    @PostMapping("/update")
    public String updateCartBeforePlaceOrder(@RequestParam("productIds") String productIds,
                                             @RequestParam("quantities") String quantities,
                                             HttpSession session,
                                             Model model){
        User user = (User) session.getAttribute("user");
        if (user == null){
            model.addAttribute("message", "Please Login To Continue!");
            return "user/boot1/cart";
        }
        if (productIds != null && quantities != null) {
            productIdArray = productIds.split(",");
            quantityArray = quantities.split(",");
        }
        Cart cart = cartService.getCurrentCartByUserId(user.getId());

        int totalPriceItem = 0;
        int latestPrice = 0;
        for (int i = 0; i < productIdArray.length; i++) {
            String productId = productIdArray[i].trim();
            String quantity = quantityArray[i].trim();
            tempProduct = productService.getProductById(Integer.parseInt(productId));
            totalPriceItem = (tempProduct.getPrice() - tempProduct.getPrice()*tempProduct.getDiscount()/100) * Integer.parseInt(quantity);
            latestPrice = tempProduct.getPrice() - tempProduct.getPrice()*tempProduct.getDiscount()/100;
            cartItemService.updateQuantityByProductId(cart.getId(),Integer.parseInt(productId), Integer.parseInt(quantity), totalPriceItem, latestPrice);
        }
        return "redirect:/order";
    }

    @ModelAttribute("sum")
    public int sumOfProducts(HttpSession session) {
        User user = (User) session.getAttribute("user");
        GuestCart guestCart = (GuestCart) session.getAttribute("guestCart");
        if (user != null) {
            //Cart cart = cartService.getCartByUserId(user.getId());
            Cart cart = cartService.getCurrentCartByUserId(user.getId());
            if (cart != null) {
                return cartService.totalOfCartItem(user);
            } else {
                return 0;
            }
        } else if (guestCart != null) {
            return cartService.totalOfCartItemTemp(guestCart.getId());
        }
        return 0;
    }

}
