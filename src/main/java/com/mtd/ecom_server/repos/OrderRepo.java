package com.mtd.ecom_server.repos;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mtd.ecom_server.models.Order;

public interface OrderRepo extends MongoRepository<Order, String> {
	List<Order> findByUserId(String userId);
}
