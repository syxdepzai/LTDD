package com.example.backend.repository;

import com.example.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Lấy tất cả sản phẩm theo category ID
    List<Product> findByCategoryId(Long categoryId);
    
    // Lấy 10 sản phẩm có số lượng bán nhiều nhất
    @Query("SELECT p FROM Product p ORDER BY p.quantitySold DESC")
    List<Product> findTop10BestSelling();
    
    // Lấy 10 sản phẩm được tạo trong vòng 7 ngày
    @Query("SELECT p FROM Product p WHERE p.createdDate >= :startDate ORDER BY p.createdDate DESC")
    List<Product> findRecentProducts(@Param("startDate") LocalDateTime startDate);
}

