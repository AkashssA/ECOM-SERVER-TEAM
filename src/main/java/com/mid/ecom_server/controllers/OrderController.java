package com.mtd.ecom_server.controllers;

import com.mtd.ecom_server.enums.OrderStatus;
import com.mtd.ecom_server.exceptions.ResourceNotFoundException;
import com.mtd.ecom_server.models.Order;
import com.mtd.ecom_server.models.Product;
import com.mtd.ecom_server.repos.OrderRepo;
import com.mtd.ecom_server.repos.ProductRepo;
import com.mtd.ecom_server.repos.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private static final Logger Log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductRepo productRepo;
    
    @Autowired
    private UserRepo userRepo;

    
    @PostMapping("/create")
    public Order createOrder(@RequestBody Order newOrder) {
        Log.info("Attempting to create a new order for user: " + newOrder.getUserId());

       
        userRepo.findById(newOrder.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found, cannot create order."));

        double total = 0.0;

        /
        for (Map.Entry<String, Integer> entry : newOrder.getProducts().entrySet()) {
            String productId = entry.getKey();
            Integer quantity = entry.getValue();

            
            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
            
            
            total += product.getPrice() * quantity;
        }

        newOrder.setTotalAmount(total);
        newOrder.setStatus(OrderStatus.PENDING); 

        Log.info("Order created successfully with total amount: " + total);
        return orderRepo.save(newOrder);
    }

    
    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUserId(@PathVariable String userId) {
        Log.info("Fetching all orders for user: " + userId);
        return orderRepo.findByUserId(userId);
    }
    
    
    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable String orderId) {
        Log.info("Fetching order details for order: " + orderId);
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }
}