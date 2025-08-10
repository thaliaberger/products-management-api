package com.example.products.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPriceRangeValidator.class)
@Documented
public @interface ValidPriceRange {
    String message() default "Minimum price must be less than or equal to maximum price";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
