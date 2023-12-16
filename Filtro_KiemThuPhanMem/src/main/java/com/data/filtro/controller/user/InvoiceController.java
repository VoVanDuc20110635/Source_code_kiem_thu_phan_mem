package com.data.filtro.controller.user;

import com.data.filtro.exception.AuthenticationAccountException;
import com.data.filtro.model.*;
import com.data.filtro.service.CartService;
import com.data.filtro.service.InvoiceService;
import com.data.filtro.service.OrderService;
import jakarta.servlet.http.HttpSession;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    CartService cartService;

    @Autowired
    OrderService orderService;

    @Autowired
    InvoiceService invoiceService;

    @GetMapping("/{orderId}")
    public String show(@PathVariable("orderId") int orderId, HttpSession session, Model model) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                throw new AuthenticationAccountException("Please login to see your order!");
            }
            Order order = orderService.getOrderById(orderId);
            List<OrderDetail> orderDetailList = order.getOrderDetails();
            int check = orderService.checkOrderStatusById(orderId);
            model.addAttribute("order", order);
            model.addAttribute("orderDetailList", orderDetailList);
            model.addAttribute("check", check);
        } catch (AuthenticationAccountException ex) {
            model.addAttribute("message", ex.getMessage());
        }
        return "user/boot1/invoice";
    }

    @PostMapping("/makeInvoice/{orderId}")
    public String makeInvoice(@PathVariable("orderId") int orderId,
                              @RequestParam("totalPrice") int totalPrice,
                              HttpSession session, Model model) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                throw new AuthenticationAccountException("Please login to see your order!");
            }
            Order order = orderService.getOrderById(orderId);
            invoiceService.makeInvoice(order, totalPrice);
            orderService.updateOrderStatus(orderId);
            orderService.updateSoldByOrderStatus(order);
        } catch (AuthenticationAccountException ex) {
            model.addAttribute("message", ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "redirect:/";
    }

}
