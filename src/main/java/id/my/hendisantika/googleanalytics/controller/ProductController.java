package id.my.hendisantika.googleanalytics.controller;

import id.my.hendisantika.googleanalytics.entity.Product;
import id.my.hendisantika.googleanalytics.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("GET /api/products - Fetching all products");
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Product>> getAllProductsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        log.info("GET /api/products/paginated - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        log.info("GET /api/products/{} - Fetching product by id", id);
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok(product))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        log.info("POST /api/products - Creating new product: {}", product.getName());
        try {
            Product createdProduct = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (Exception e) {
            log.error("Error creating product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
                                                 @Valid @RequestBody Product productDetails) {
        log.info("PUT /api/products/{} - Updating product", id);
        try {
            Product updatedProduct = productService.updateProduct(id, productDetails);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            log.error("Error updating product with id {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating product with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("DELETE /api/products/{} - Deleting product", id);
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting product with id {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProductsByName(@RequestParam String name) {
        log.info("GET /api/products/search?name={} - Searching products by name", name);
        List<Product> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search/advanced")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("GET /api/products/search/advanced?keyword={} - Advanced search", keyword);
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.searchProducts(keyword, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {

        log.info("GET /api/products/price-range?minPrice={}&maxPrice={} - Fetching products by price range",
                minPrice, maxPrice);
        List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts(@RequestParam Integer threshold) {
        log.info("GET /api/products/low-stock?threshold={} - Fetching low stock products", threshold);
        List<Product> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/low-stock/count")
    public ResponseEntity<Long> countLowStockProducts(@RequestParam Integer threshold) {
        log.info("GET /api/products/low-stock/count?threshold={} - Counting low stock products", threshold);
        Long count = productService.countLowStockProducts(threshold);
        return ResponseEntity.ok(count);
    }
}