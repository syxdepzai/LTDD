-- Tạo dữ liệu mẫu cho Categories
INSERT INTO categories (id, name, description) VALUES 
(1, 'Điện thoại', 'Các sản phẩm điện thoại di động'),
(2, 'Laptop', 'Máy tính xách tay'),
(3, 'Tai nghe', 'Tai nghe và phụ kiện âm thanh'),
(4, 'Đồng hồ', 'Đồng hồ thông minh và đồng hồ thời trang');

-- Tạo dữ liệu mẫu cho Products
INSERT INTO products (id, name, description, price, quantity_sold, created_date, category_id) VALUES 
-- Điện thoại
(1, 'iPhone 15 Pro Max', 'Điện thoại cao cấp từ Apple', 29990000, 250, DATE_SUB(NOW(), INTERVAL 2 DAY), 1),
(2, 'Samsung Galaxy S24 Ultra', 'Flagship Samsung 2024', 26990000, 180, DATE_SUB(NOW(), INTERVAL 3 DAY), 1),
(3, 'Xiaomi 14 Pro', 'Điện thoại Xiaomi cao cấp', 18990000, 120, DATE_SUB(NOW(), INTERVAL 5 DAY), 1),
(4, 'OPPO Find X7', 'Camera chuyên nghiệp', 15990000, 95, DATE_SUB(NOW(), INTERVAL 10 DAY), 1),

-- Laptop
(5, 'MacBook Pro M3', 'Laptop Apple chip M3', 42990000, 200, DATE_SUB(NOW(), INTERVAL 1 DAY), 2),
(6, 'Dell XPS 15', 'Laptop cao cấp cho đồ họa', 35990000, 150, DATE_SUB(NOW(), INTERVAL 4 DAY), 2),
(7, 'Lenovo ThinkPad X1', 'Laptop doanh nhân', 28990000, 110, DATE_SUB(NOW(), INTERVAL 6 DAY), 2),
(8, 'HP Spectre x360', 'Laptop 2 trong 1', 32990000, 85, DATE_SUB(NOW(), INTERVAL 15 DAY), 2),

-- Tai nghe
(9, 'AirPods Pro 2', 'Tai nghe không dây Apple', 5990000, 300, DATE_SUB(NOW(), INTERVAL 1 DAY), 3),
(10, 'Sony WH-1000XM5', 'Tai nghe chống ồn cao cấp', 7990000, 220, DATE_SUB(NOW(), INTERVAL 2 DAY), 3),
(11, 'Samsung Galaxy Buds Pro', 'Tai nghe true wireless', 3990000, 160, DATE_SUB(NOW(), INTERVAL 8 DAY), 3),
(12, 'Bose QuietComfort 45', 'Tai nghe chống ồn Bose', 6990000, 90, DATE_SUB(NOW(), INTERVAL 20 DAY), 3),

-- Đồng hồ
(13, 'Apple Watch Series 9', 'Đồng hồ thông minh Apple', 9990000, 280, DATE_SUB(NOW(), INTERVAL 2 DAY), 4),
(14, 'Samsung Galaxy Watch 6', 'Đồng hồ Android cao cấp', 7990000, 140, DATE_SUB(NOW(), INTERVAL 5 DAY), 4),
(15, 'Garmin Fenix 7', 'Đồng hồ thể thao chuyên nghiệp', 15990000, 75, DATE_SUB(NOW(), INTERVAL 12 DAY), 4),
(16, 'Amazfit GTR 4', 'Đồng hồ giá tốt', 3990000, 100, DATE_SUB(NOW(), INTERVAL 25 DAY), 4);

