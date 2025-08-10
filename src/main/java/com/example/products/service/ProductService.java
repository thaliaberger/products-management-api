package com.example.products.service;

import com.example.products.entity.Product;
import com.example.products.exception.ProductAlreadyExistsException;
import com.example.products.repository.ProductRepository;
import com.example.products.repository.specification.ProductSpecification;
import com.example.products.validation.ValidationGroups;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Map<String, Integer> saveAllUniqueProducts(List<Product> products) {
        Map<String, Product> existingProductsMap = getExistingProductsMap(products);

        List<Product> uniqueProducts = filterUniqueProducts(products, existingProductsMap);

        int savedCount = uniqueProducts.size();
        int duplicateCount = products.size() - savedCount;

        log.info("Saving {} products ({} ignored duplicates)", savedCount, duplicateCount);

        if (!uniqueProducts.isEmpty()) {
            productRepository.saveAll(uniqueProducts);
        }

        return createResultMap(savedCount, duplicateCount);
    }

    private Map<String, Product> getExistingProductsMap(List<Product> products) {
        return productRepository.findAllByProductAndTypeIn(
                        products.stream()
                                .map(Product::getProduct)
                                .distinct()
                                .collect(Collectors.toList()),
                        products.stream()
                                .map(Product::getType)
                                .distinct()
                                .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(
                        product -> product.getProduct() + ":" + product.getType(),
                        Function.identity(),
                        (existing, replacement) -> existing
                ));
    }

    private List<Product> filterUniqueProducts(List<Product> products, Map<String, Product> existingProductsMap) {
        return products.stream()
                .filter(product -> !existingProductsMap.containsKey(product.getProduct() + ":" + product.getType()))
                .collect(Collectors.toList());
    }

    private Map<String, Integer> createResultMap(int savedCount, int duplicateCount) {
        return Map.of(
                "savedProducts", savedCount,
                "duplicates", duplicateCount
        );
    }

    @Transactional(readOnly = true)
    @Validated(ValidationGroups.SearchValidation.class)
    public Page<Product> searchProducts(
            @Valid @NotNull(message = "Product specification cannot be null") ProductSpecification productSpecification,
            @NotNull(message = "Pageable cannot be null") Pageable pageable) {
        return productRepository.findAll(productSpecification, pageable);
    }

    @Transactional
    public Product saveProduct(Product product) {
        if (!productRepository.existsByProductAndType(product.getProduct(), product.getType())) {
            return productRepository.save(product);
        } else {
            throw new ProductAlreadyExistsException("A product with the same name and type already exists");
        }
    }
}
