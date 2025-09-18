package com.mtd.ecom_server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;
import java.util.Optional;

@RestController 
@RequestMapping("/products")
public class ProductController {
	private static final Logger Log=LoggerFactory.getLogger(ProductController.class);
	
	
    
    @Autowired 
    ProductRepo productRepo;
    @Tag(name="get all products")
     
    @GetMapping("/all")
    public List<Product> getAllProducts() {
    	Log.info("fetching products");
        return productRepo.findAll();
    }

    @PostMapping("/add")
    public Product addProduct(@RequestBody Product newproduct) {
    	Log.info("Adding product"+newproduct);
        
        return productRepo.save(newproduct);
    }

    @DeleteMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable String id) {
        Optional<Product> findproduct = productRepo.findById(id);

        if (findproduct.isEmpty()) {
            Log.error("Failed to delete product " + id);
            throw new ResourceNotFoundException("product not found");
            
        }

        productRepo.deleteById(id);
        Log.info("Product Deleted " + id);
       
        return "product Deleted";
    }


    @PutMapping("/product/edit/{id}")
    public Product editProduct(@PathVariable String id, @RequestBody Product newproduct) {
        Product findproduct = productRepo.findById(id).get();
        findproduct.setName(newproduct.getName());
        findproduct.setDescription(newproduct.getDescription());
        findproduct.setCategory(newproduct.getCategory());
        findproduct.setTags(newproduct.getTags()); 
        findproduct.setPrice(newproduct.getPrice());
        findproduct.setStock(newproduct.getStock());
    	Log.info("updating product"+findproduct);
        return productRepo.save(findproduct);
    }
}
