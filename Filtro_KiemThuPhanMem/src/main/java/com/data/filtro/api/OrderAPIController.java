package com.data.filtro.api;

import com.data.filtro.model.*;
import com.data.filtro.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderAPIController {



    @Autowired
    OrderService orderService;
    @Autowired
    PaymentMethodService paymentMethodService;
    @Autowired
    OrderShipperService orderShipperService;

    @Autowired
    UserService userService;
    @Autowired
    CartItemService cartItemService;
    @Autowired
    CartService cartService;

    @GetMapping("/findByUserId/{id}")
    public ResponseEntity<?> getOrderByUserId(@PathVariable int id) {
        List<Order> listOrder = orderService.getOrderByUserId(id);
        if (listOrder == null) {
            String message = "Order not found!";
            ErrorResponse err = new ErrorResponse(message, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(listOrder, HttpStatus.OK);
    }

    @GetMapping("/findByStatusOrder")
    public ResponseEntity<?> getOrderByStatusOrder(@RequestParam int status, @RequestParam int shipperId){
        List<Order> listOrder = new ArrayList<>();
        if (status == 2 || status == 3 || status == 4) {
            if (status == 2){
                listOrder = orderService.findOrderByStatusOrder(status);
                return new ResponseEntity<>(listOrder, HttpStatus.OK);
            } else {
                List<OrderShipper> orderShippers = orderShipperService.loadOrdersByOrderShipperAndStatus(shipperId, status);
                for (OrderShipper orderShipper : orderShippers){
                    System.out.println(orderShipper.getOrderId() + " " + orderShipper.getUserId() + " " + orderShipper.getStatus());
                    listOrder.add(orderService.getOrderById(orderShipper.getOrderId()));
                }
                return new ResponseEntity<>(listOrder, HttpStatus.OK);
            }
        }
        else {
            listOrder = null;
            return new ResponseEntity<>(listOrder, HttpStatus.OK);
        }
    }

    @PostMapping("/updateStatusOrder")
    public ResponseEntity<?> nhanGiaoDonHangHoacDaGiaoHang(@RequestParam int orderId, @RequestParam int status, @RequestParam int shipperId) {
        String message;
        ErrorResponse err;
        Order order = orderService.getOrderById(orderId);
        System.out.println("nhan duoc request");
        try {
            if (order != null) {
                if (status == 3) {
                    order.setStatus(3);
                    orderService.update(order);
                    message = "Gửi yêu cầu giao đơn hàng thành công!";
                    System.out.println(message);
                    OrderShipper orderShipper = new OrderShipper();
                    orderShipper.setOrderId(orderId);
                    orderShipper.setUserId(shipperId);
                    orderShipper.setStatus(status);
                    System.out.println(orderShipper.getUserId() + " " + orderShipper.getOrderId()
                            + " " + orderShipper.getStatus());
                    orderShipperService.create(orderShipper);
                    System.out.println("khong them donHangDaGiaoUser vao table duoc");
                } else if (status == 4){
                    order.setStatus(4);
                    orderService.update(order);
                    OrderShipper orderShipper = orderShipperService.loadOrderShipperByOrderId(orderId);
                    orderShipper.setStatus(4);
                    orderShipperService.update(orderShipper);
                    message = "Đã giao hàng thành công!";
                    System.out.println(message);
                } else {
                    message = "Trạng thái đơn hàng không đúng";
                    System.out.println(message);
                    err = new ErrorResponse(message, HttpStatus.OK.value());
                    return new ResponseEntity<>(err, HttpStatus.OK);
                }
                orderService.update(order);
                orderService.updateSoldByOrderStatus(order);

                err = new ErrorResponse(message, HttpStatus.OK.value());
                return new ResponseEntity<>(err, HttpStatus.OK);
            }
            else {
                message = "Không nhạn được đơn hàng từ id!";
                err = new ErrorResponse(message, HttpStatus.OK.value());
                return new ResponseEntity<>(err, HttpStatus.OK);
            }
        } catch (Exception e){
            message = "Không nhận được API!";
            err = new ErrorResponse(message, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> find(@PathVariable int id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            String message = "Order not found!";
            ErrorResponse err = new ErrorResponse(message, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);

    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        List<Order> orders = orderService.getAll();
        if (orders == null) {
            String message = "Orders not found!";
            ErrorResponse err = new ErrorResponse(message, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);

    }

    //    @PostMapping("/placeOrder")
//    public ResponseEntity<?> placeOrder(@RequestParam int userId,
//                                        @RequestParam String phone,
//                                        @RequestParam String email,
//                                        @RequestParam String address,
//                                        @RequestParam String city,
//                                        @RequestParam int zip,
//                                        @RequestParam int paymentId){
//        User user = userService.getByUserId(userId);
//        Cart cart = cartService.getCartByUserId(userId);
//        List<CartItem> cartItemList = cartItemService.getCartItemByCartId(cart.getId());
//        if(cartItemList != null){
//            Order order = orderService.placeOrder(user,phone,email,address,city,zip,paymentMethodService.getMethodById(paymentId),
//                    cartItemList);
//
//            cartService.removeCartByCartId(cart.getId());
//            return new ResponseEntity<>("Đặt hàng thành công", HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Giỏ hàng trống", HttpStatus.BAD_REQUEST);
//
//    }
//
    @PostMapping("/placeOrder")
    public ResponseEntity<?> placeOrder(@RequestParam int userId,
                                        @RequestParam String phone,
                                        @RequestParam String email,
                                        @RequestParam String address,
                                        @RequestParam String city,
                                        @RequestParam int zip,
                                        @RequestParam int paymentId) {
        User user = userService.getByUserId(userId);
        if (user != null) {
            Cart cart = cartService.getCartByUserId(user.getId());
            if (cart == null) {
                return new ResponseEntity<>("Cart ko hop le!", HttpStatus.BAD_REQUEST);
            }
            List<CartItem> cartItemList = cart.getCartItemList();
            PaymentMethod payment = paymentMethodService.getMethodById(paymentId);

            Order order = orderService.placeOrder(user, phone, email, address, city, zip, payment,
                    cartItemList);
            cartService.removeAllProductInCar(cart);

            return new ResponseEntity<>("Đặt hàng thành công " + order.getId(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Dat hang khong thanh cong!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PostMapping("/changestatusorder")
    public ResponseEntity<?> changeStatusOrder(@RequestParam int orderId,
                                               @RequestParam int status){
        try{
        Order order=orderService.getOrderById(orderId);
        order.setStatus(status);
        orderService.updatess(order);
        String responseapp="";
        switch (status){
            case 1:
                responseapp="Hàng đang đợi duyệt";
                break;
            case 2:
                responseapp="Hàng đang chờ shipper lấy";
                break;
            case 3:
                responseapp="Hàng đang được chuyển đi";
                break;
            case 4:
                responseapp="Hàng đã nhận";
                break;
            case 5:
                responseapp="Hàng hàng đang hoàn trả";
                break;
            case 6:
                responseapp="Hàng đã bị hủy";
                break;
            default:
                responseapp="Hàng bị chìm";
                break;
        }
        return new ResponseEntity<>(responseapp,HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>("Lỗi đã xảy ra "+ e.toString(),HttpStatus.OK);
        }

    }

}


