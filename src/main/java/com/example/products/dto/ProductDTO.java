package com.example.products.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ProductDTO(
        @JsonProperty("product") String product,
        @JsonProperty("quantity") Integer quantity,
        @JsonProperty("price") BigDecimal price,
        @JsonProperty("type") String type,
        @JsonProperty("industry") String industry,
        @JsonProperty("origin") String origin
) { }
