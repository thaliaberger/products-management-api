package com.example.products.dto;

import com.example.products.entity.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ProductDataDTO(
        @JsonProperty("data") List<Product> products
) {}
