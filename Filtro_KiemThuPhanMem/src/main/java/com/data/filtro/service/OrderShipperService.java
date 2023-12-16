package com.data.filtro.service;

import com.data.filtro.model.OrderShipper;
import com.data.filtro.repository.OrderShipperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderShipperService {
    @Autowired
    OrderShipperRepository orderShipperRepository;

    public List<OrderShipper> loadOrdersNotDelivery(){
        return orderShipperRepository.findOrderDontDelivery();
    }

    public List<OrderShipper> loadOrdersByOrderShipperAndStatus(int shipperId, int status){
        return orderShipperRepository.findByShipperIdAndStatus(shipperId, status);
    }

    public OrderShipper loadOrderShipperByOrderId(int orderId){
        return orderShipperRepository.findByOrderId(orderId);
    }

    public void create(OrderShipper orderShipper){
        OrderShipper newOrderShipper = new OrderShipper();
        newOrderShipper.setOrderId(orderShipper.getOrderId());
        newOrderShipper.setUserId(orderShipper.getUserId());
        newOrderShipper.setStatus(orderShipper.getStatus());
        orderShipperRepository.save(newOrderShipper);
    }

    public void update(OrderShipper newOrderShipper){
//        OrderShipper orderShipper = loadOrderShipperByOrderId(newOrderShipper.getOrderId());
//        orderShipper.setStatus(newOrderShipper.getStatus());
        orderShipperRepository.save(newOrderShipper);
    }
}
