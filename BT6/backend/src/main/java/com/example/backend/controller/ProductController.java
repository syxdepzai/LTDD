package com.example.backend.controller;

import com.example.backend.entity.Product;
import com.example.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    /**
     * API: Hiển thị tất cả sản phẩm theo danh mục
     * GET /api/products/category/{categoryId}
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }
    
    /**
     * API: Hiển thị 10 sản phẩm có số lượng bán nhiều nhất
     * GET /api/products/best-selling
     */
    @GetMapping("/best-selling")
    public ResponseEntity<List<Product>> getTop10BestSellingProducts() {
        List<Product> products = productService.getTop10BestSellingProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * API: Hiển thị 10 sản phẩm được tạo <= 7 ngày
     * GET /api/products/recent
     */
    @GetMapping("/recent")
    public ResponseEntity<List<Product>> getRecentProducts() {
        List<Product> products = productService.getRecentProducts();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
}

