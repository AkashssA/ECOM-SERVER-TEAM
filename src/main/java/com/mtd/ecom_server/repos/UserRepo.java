package com.mtd.ecom_server.repos;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mtd.ecom_server.models.User;

public interface UserRepo extends MongoRepository<User, String> {

	Optional<User> findByEmail(String email);
}
