package com.mtd.ecom_server;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mtd.ecom_server.controllers.ProductController;
import com.mtd.ecom_server.models.Product;
import com.mtd.ecom_server.repos.ProductRepo;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepo repo;

    @Test
    void shouldReturnAllProducts() throws Exception {
        Product product = new Product();
        product.setId("1");
        product.setName("Laptop");
        product.setDescription("Desc");
        product.setCategory("Cat");
        product.setTags("tag");
        product.setPrice(10000);
        product.setStock(5);

        when(repo.findAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/products/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }
}
