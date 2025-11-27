package com.example.backend.service;

import com.example.backend.entity.Product;
import com.example.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    /**
     * Lấy tất cả sản phẩm theo danh mục
     */
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
    
    /**
     * Lấy 10 sản phẩm có số lượng bán nhiều nhất
     */
    public List<Product> getTop10BestSellingProducts() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Product> allProducts = productRepository.findTop10BestSelling();
        return allProducts.stream().limit(10).toList();
    }
    
    /**
     * Lấy 10 sản phẩm được tạo trong vòng 7 ngày
     */
    public List<Product> getRecentProducts() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<Product> recentProducts = productRepository.findRecentProducts(sevenDaysAgo);
        return recentProducts.stream().limit(10).toList();
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}

