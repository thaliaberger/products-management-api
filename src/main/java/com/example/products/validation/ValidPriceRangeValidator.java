package com.example.products.validation;

import com.example.products.repository.specification.ProductSpecification;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidPriceRangeValidator 
    implements ConstraintValidator<ValidPriceRange, ProductSpecification> {
    
    @Override
    public void initialize(ValidPriceRange constraint) {
        // No initialization needed
    }

    @Override
    public boolean isValid(ProductSpecification spec, ConstraintValidatorContext context) {
        if (spec.getMinPrice() == null || spec.getMaxPrice() == null) {
            return true;
        }
        return spec.getMinPrice().compareTo(spec.getMaxPrice()) <= 0;
    }
}
