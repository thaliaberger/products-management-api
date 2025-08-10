package com.example.products.repository.specification;

import com.example.products.entity.Product;
import com.example.products.validation.ValidPriceRange;
import com.example.products.validation.ValidationGroups;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.group.GroupSequenceProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ValidPriceRange(groups = ValidationGroups.SearchValidation.class)
@GroupSequenceProvider(ProductSpecificationGroupSequenceProvider.class)
public class ProductSpecification implements Specification<Product> {
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Name can only contain alphanumeric characters and spaces", groups = ValidationGroups.SearchValidation.class)
    private String name;

    @DecimalMin(value = "0.0", message = "Minimum price must be greater than or equal to 0", groups = ValidationGroups.SearchValidation.class)
    private BigDecimal minPrice;

    @DecimalMin(value = "0.0", message = "Maximum price must be greater than or equal to 0", groups = ValidationGroups.SearchValidation.class)
    private BigDecimal maxPrice;

    public ProductSpecification(String name, BigDecimal minPrice, BigDecimal maxPrice) {
        this.name = StringUtils.hasText(name) ? name.trim() : null;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }
    
    public boolean hasAtLeastOneParameter() {
        return name != null || minPrice != null || maxPrice != null;
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasText(name)) {
            predicates.add(cb.like(
                cb.lower(root.get("product")), 
                "%" + name.toLowerCase() + "%"
            ));
        }

        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
