package com.mtd.ecom_server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;


public class GlobalExcptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleNotFound(String msg){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
	}
}
