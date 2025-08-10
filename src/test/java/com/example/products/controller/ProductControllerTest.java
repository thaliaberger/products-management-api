package com.example.products.controller;

import com.example.products.entity.Product;
import com.example.products.exception.FieldException;
import com.example.products.exception.ProductAlreadyExistsException;
import com.example.products.repository.specification.ProductSpecification;
import com.example.products.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchProducts_ValidRequest_ReturnsProducts() {
        // Arrange
        Product product1 = new Product("Product1", 1, BigDecimal.valueOf(10),"Type1", "Industry1", "Origin1");
        Product product2 = new Product("Product2", 1, BigDecimal.valueOf(20), "Type2", "Industry2", "Origin2");

        List<Product> productList = List.of(product1, product2);
        Page<Product> productPage = new PageImpl<>(productList);
        when(productService.searchProducts(any(ProductSpecification.class), any(Pageable.class))).thenReturn(productPage);

        // Act
        ResponseEntity<Page<Product>> response = productController.searchProducts("Product", BigDecimal.ZERO, BigDecimal.TEN, Pageable.ofSize(10));

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productPage, response.getBody());
    }

    @Test
    void searchProducts_NoParameters_ThrowsFieldException() {
        // Act
        FieldException thrown = assertThrows(FieldException.class, () -> {
            productController.searchProducts(null, null, null, Pageable.ofSize(10));
        });

        // Assert
        assertEquals("At least one search parameter must be provided: name, minPrice or maxPrice", thrown.getMessage());
    }

    @Test
    void saveAllUniqueProducts_ValidRequest_Success() {
        // Arrange
        Product product1 = new Product("Product1", 1, BigDecimal.valueOf(10),"Type1", "Industry1", "Origin1");
        Product product2 = new Product("Product2", 1, BigDecimal.valueOf(20), "Type2", "Industry2", "Origin2");
        List<Product> productsToSave = Arrays.asList(product1, product2);

        Map<String, Integer> expectedResponse = new HashMap<>();
        expectedResponse.put("savedProducts", 2);
        expectedResponse.put("duplicates", 0);

        when(productService.saveAllUniqueProducts(productsToSave)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<Map<String, Integer>> response = productController.saveAllUniqueProducts(productsToSave);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void saveSingleProduct_ValidRequest_Success() {
        // Arrange
        Product product = new Product("Product1", 1, BigDecimal.valueOf(10),"Type1", "Industry1", "Origin1");
        when(productService.saveProduct(product)).thenReturn(product);

        // Act
        ResponseEntity<Product> response = productController.saveSingleProduct(product);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(product, response.getBody());
    }

    @Test
    void saveSingleProduct_ExistingProduct_ThrowsException() {
        // Arrange
        Product product = new Product("Product1", 1, BigDecimal.valueOf(10),"Type1", "Industry1", "Origin1");
        when(productService.saveProduct(product)).thenThrow(new ProductAlreadyExistsException("A product with the same name and type already exists"));

        // Act
        ProductAlreadyExistsException thrown = assertThrows(ProductAlreadyExistsException.class, () -> {
            productController.saveSingleProduct(product);
        });

        // Assert
        assertEquals("A product with the same name and type already exists", thrown.getMessage());
    }
}