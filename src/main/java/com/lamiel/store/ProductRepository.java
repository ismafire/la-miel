package com.lamiel.store;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // This instantly lets us sort items by category (e.g., just show Shoes, or just show Soaps)
    List<Product> findByCategory(String category);
}