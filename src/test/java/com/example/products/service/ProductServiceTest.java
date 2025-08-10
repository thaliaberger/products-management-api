package com.example.products.service;

import com.example.products.entity.Product;
import com.example.products.exception.ProductAlreadyExistsException;
import com.example.products.repository.ProductRepository;
import com.example.products.repository.specification.ProductSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveAllUniqueProducts_SavesUniqueProducts() {
        // Arrange
        Product product1 = new Product("Product1", 1, BigDecimal.valueOf(10),"Type1", "Industry1", "Origin1");
        Product product2 = new Product("Product2", 1, BigDecimal.valueOf(20), "Type2", "Industry2", "Origin2");

        when(productRepository.findAllByProductAndTypeIn(anyList(), anyList()))
                .thenReturn(List.of(product1));

        List<Product> productsToSave = List.of(product1, product2);

        // Act
        Map<String, Integer> result = productService.saveAllUniqueProducts(productsToSave);

        // Assert
        assertEquals(1, result.get("savedProducts"));
        assertEquals(1, result.get("duplicates"));
        verify(productRepository).saveAll(List.of(product2));
    }

    @Test
    void saveAllUniqueProducts_NoProductsSaved() {
        // Arrange
        Product product1 = new Product("Product1", 1, BigDecimal.valueOf(10),"Type1", "Industry1", "Origin1");

        when(productRepository.findAllByProductAndTypeIn(anyList(), anyList()))
                .thenReturn(List.of(product1));

        List<Product> productsToSave = List.of(product1);

        // Act
        Map<String, Integer> result = productService.saveAllUniqueProducts(productsToSave);

        // Assert
        assertEquals(0, result.get("savedProducts"));
        assertEquals(1, result.get("duplicates"));
        verify(productRepository, never()).saveAll(anyList());
    }

    @Test
    void saveProduct_NewProduct_Success() {
        // Arrange
        Product product = new Product("Product2", 1, BigDecimal.valueOf(20), "Type2", "Industry2", "Origin2");
        when(productRepository.existsByProductAndType(product.getProduct(), product.getType()))
                .thenReturn(false);
        when(productRepository.save(product)).thenReturn(product);

        // Act
        Product result = productService.saveProduct(product);

        // Assert
        assertNotNull(result);
        assertEquals(product, result);
        verify(productRepository).save(product);
    }

    @Test
    void saveProduct_ExistingProduct_ThrowsException() {
        // Arrange
        Product product = new Product("Product2", 1, BigDecimal.valueOf(20), "Type2", "Industry2", "Origin2");
        when(productRepository.existsByProductAndType(product.getProduct(), product.getType()))
                .thenReturn(true);

        // Act & Assert
        assertThrows(ProductAlreadyExistsException.class, () -> productService.saveProduct(product));
        verify(productRepository, never()).save(product);
    }

    @Test
    void searchProducts_ReturnsProducts() {
        // Arrange
        ProductSpecification specification = mock(ProductSpecification.class);
        Pageable pageable = Pageable.ofSize(10);
        Page<Product> expectedPage = mock(Page.class);

        when(productRepository.findAll(specification, pageable)).thenReturn(expectedPage);

        // Act
        Page<Product> result = productService.searchProducts(specification, pageable);

        // Assert
        assertEquals(expectedPage, result);
    }
}