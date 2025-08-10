package com.example.products.loader;

import com.example.products.dto.ProductDataDTO;
import com.example.products.entity.Product;
import com.example.products.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader {
    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void loadData() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        var resources = resolver.getResources("classpath*:data/*.json");

        Arrays.stream(resources)
                .parallel()
                .forEach(resource -> {
                    try (InputStream inputStream = resource.getInputStream()) {
                        var productData = objectMapper.readValue(inputStream, ProductDataDTO.class);
                        Set<Product> uniqueProducts = new HashSet<>(productData.products());
                        List<Product> batch = new ArrayList<>();

                        for (Product product : uniqueProducts) {
                            batch.add(product);

                            if (batch.size() >= 1000) {
                                productService.saveAllUniqueProducts(batch);
                                batch.clear();
                            }
                        }

                        if (!batch.isEmpty()) {
                            productService.saveAllUniqueProducts(batch);
                        }

                    } catch (IOException e) {
                        log.error("Error loading data from {}", resource, e);
                    }
                });
    }
}
