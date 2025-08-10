package com.example.products.controller;

import com.example.products.entity.Product;
import com.example.products.exception.FieldException;
import com.example.products.repository.specification.ProductSpecification;
import com.example.products.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(page = 0, size = 10, sort = "product") Pageable pageable) {

        ProductSpecification spec = new ProductSpecification(name, minPrice, maxPrice);

        if (!spec.hasAtLeastOneParameter()) {
            throw new FieldException("At least one search parameter must be provided: name, minPrice or maxPrice");
        }

        Page<Product> products = productService.searchProducts(spec, pageable);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/saveAll")
    public ResponseEntity<Map<String, Integer>> saveAllUniqueProducts(@Valid @RequestBody List<Product> products) {
        Map<String, Integer> result = productService.saveAllUniqueProducts(products);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/save")
    public ResponseEntity<Product> saveSingleProduct(@Valid @RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(product));
    }
}
