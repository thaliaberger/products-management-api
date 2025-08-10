package com.example.products.repository.specification;

import com.example.products.validation.ValidationGroups;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecificationGroupSequenceProvider 
    implements DefaultGroupSequenceProvider<ProductSpecification> {

    @Override
    public List<Class<?>> getValidationGroups(ProductSpecification spec) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(ProductSpecification.class);
        
        if (spec != null && spec.hasAtLeastOneParameter()) {
            groups.add(ValidationGroups.SearchValidation.class);
        }
        
        return groups;
    }
}
