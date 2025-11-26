package com.example.backend.dto.mapper;

import com.example.backend.dto.ProductDTO;
import com.example.backend.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    
    public ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }
        
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantitySold(product.getQuantitySold());
        dto.setCreatedDate(product.getCreatedDate());
        
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }
        
        return dto;
    }
}

