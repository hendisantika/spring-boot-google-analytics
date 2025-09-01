package id.my.hendisantika.googleanalytics.repository;

import id.my.hendisantika.googleanalytics.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByStockQuantityLessThan(Integer quantity);

    @Query("SELECT p FROM Product p WHERE p.name ILIKE %:keyword% OR p.description ILIKE %:keyword%")
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.stockQuantity < :threshold")
    Long countLowStockProducts(@Param("threshold") Integer threshold);
}