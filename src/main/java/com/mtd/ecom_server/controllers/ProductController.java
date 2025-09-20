package com.mtd.ecom_server.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mtd.ecom_server.exceptions.ResourceNotFoundException;
import com.mtd.ecom_server.models.Product;
import com.mtd.ecom_server.repos.ProductRepo;

import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin("*")
@RestController
@RequestMapping("/products")
public class ProductController {
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);

	@Autowired ProductRepo productRepo;
	@Tag(name = "Get All Products")
	@GetMapping("/all")
	public List<Product> getAllProducts() {
		log.info("Fetching products");
		return productRepo.findAll();
	}
	@Tag(name = "Add Product")
	@PostMapping("/add")
	public Product addProduct(@RequestBody Product newproduct) {
		log.info("Adding product");
		return productRepo.save(newproduct);
	}
	@Tag(name = "Delete Products")
	@DeleteMapping("/product/delete/{id}")
	public String deleteProduct(@PathVariable String id) {
	Optional<Product> findproduct  = productRepo.findById(id);
		if(findproduct.isEmpty()) {
			throw new ResourceNotFoundException("Product Not Found");
		}
		productRepo.deleteById(id);

		return "Product Deleted ";
	}
	@Tag(name = "Edit Products")
	@PutMapping ("/product/edit/{id}")
	public Product editPorduct(@PathVariable String id, @RequestBody Product newproduct) {
		Product findproduct = productRepo.findById(id).get();
		findproduct.setName(newproduct.getName());
		findproduct.setDescription(newproduct.getDescription());
		findproduct.setCategory(newproduct.getCategory());
		findproduct.setTags(newproduct.getTags());
		findproduct.setPrice(newproduct.getPrice());
		findproduct.setStock(newproduct.getStock());
		return productRepo.save(findproduct) ;
	}
	
	
}
