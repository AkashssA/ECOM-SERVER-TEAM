package com.mtd.ecom_server.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mtd.ecom_server.enums.OrderStatus;
import com.mtd.ecom_server.exceptions.ResourceNotFoundException;
import com.mtd.ecom_server.models.Order;
import com.mtd.ecom_server.models.Product;
import com.mtd.ecom_server.repos.OrderRepo;
import com.mtd.ecom_server.repos.ProductRepo;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired OrderRepo orderRepo;
    @Autowired ProductRepo productRepo;

    private final static Logger log = LoggerFactory.getLogger(OrderController.class);

    @Tag(name = "Place Order")
    @PostMapping("/{userId}/place")
    public Order placeOrder(@PathVariable String userId, @RequestBody Map<String, Integer> products) {
        log.info("Placing order for userId: {}", userId);
        double total = 0.0;

        for (Map.Entry<String, Integer> entry : products.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();

            Product product = productRepo.findById(productId).orElseThrow(() -> {
                log.error("Product Not Found: {}", productId);
                return new ResourceNotFoundException("Product Not Found");
            });

            if (product.getStock() < quantity) {
                log.error("Insufficient stock for product: {}", product.getName());
                throw new ResourceNotFoundException("Insufficient stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - quantity);
            productRepo.save(product);

            total += product.getPrice() * quantity;
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setProducts(products);
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.PENDING); 

        return orderRepo.save(order);
    }

    @Tag(name = "Update Order Status")
    @PutMapping("/{orderId}/status")
    public Order updateOrderStatus(@PathVariable String orderId, @RequestParam OrderStatus status) {
        log.info("Updating order status. OrderId: {}, Status: {}", orderId, status);

        Order order = orderRepo.findById(orderId).orElseThrow(() -> {
            log.error("Order Not Found with ID: {}", orderId);
            return new ResourceNotFoundException("Order Not Found");
        });

        order.setStatus(status);
        return orderRepo.save(order);
    }

    @Tag(name = "Get All Orders")
    @GetMapping("/all")
    public List<Order> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepo.findAll();
    }

    @Tag(name = "Get Order by ID")
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable String id) {
        log.info("Fetching order with ID: {}", id);
        Optional<Order> findOrder = orderRepo.findById(id);
        if (findOrder.isEmpty()) {
            log.error("Order Not Found with ID: {}", id);
            throw new ResourceNotFoundException("Order Not Found");
        }
        return findOrder.get();
    }

    @Tag(name = "Get Orders by User")
    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable String userId) {
        log.info("Fetching orders for user: {}", userId);
        return orderRepo.findByUserId(userId);
    }

    @Tag(name = "Delete Order")
    @DeleteMapping("/delete/{id}")
    public String deleteOrder(@PathVariable String id) {
        Optional<Order> findOrder = orderRepo.findById(id);
        if (findOrder.isEmpty()) {
            log.error("Order Not Found with ID: {}", id);
            throw new ResourceNotFoundException("Order Not Found");
        }
        orderRepo.deleteById(id);
        return "Order Deleted";
    }
}
