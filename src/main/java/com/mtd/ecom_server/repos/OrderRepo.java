package com.mtd.ecom_server.repos;

import com.mtd.ecom_server.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface OrderRepo extends MongoRepository<Order, String> {

    
    List<Order> findByUserId(String userId);

}