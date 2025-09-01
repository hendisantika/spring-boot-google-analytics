package id.my.hendisantika.googleanalytics.service;

import id.my.hendisantika.googleanalytics.entity.Product;
import id.my.hendisantika.googleanalytics.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final GoogleAnalyticsService googleAnalyticsService;

    public List<Product> getAllProducts() {
        log.debug("Fetching all products");
        List<Product> products = productRepository.findAll();
        googleAnalyticsService.trackProductListView(products.size(), "all_products");
        return products;
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        log.debug("Fetching products with pagination: {}", pageable);
        Page<Product> products = productRepository.findAll(pageable);
        googleAnalyticsService.trackProductListView((int) products.getTotalElements(), "paginated_products");
        return products;
    }

    public Optional<Product> getProductById(Long id) {
        log.debug("Fetching product by id: {}", id);
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            googleAnalyticsService.trackProductView(id, product.get().getName());
        }
        return product;
    }

    public Product createProduct(Product product) {
        log.debug("Creating new product: {}", product.getName());
        Product savedProduct = productRepository.save(product);
        googleAnalyticsService.trackProductCreate(savedProduct.getId(), savedProduct.getName());
        return savedProduct;
    }

    public Product updateProduct(Long id, Product productDetails) {
        log.debug("Updating product with id: {}", id);
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    product.setDescription(productDetails.getDescription());
                    product.setPrice(productDetails.getPrice());
                    product.setStockQuantity(productDetails.getStockQuantity());
                    Product updatedProduct = productRepository.save(product);
                    googleAnalyticsService.trackProductUpdate(id, updatedProduct.getName());
                    return updatedProduct;
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public void deleteProduct(Long id) {
        log.debug("Deleting product with id: {}", id);
        Optional<Product> productToDelete = productRepository.findById(id);
        if (productToDelete.isPresent()) {
            productRepository.deleteById(id);
            googleAnalyticsService.trackProductDelete(id, productToDelete.get().getName());
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    public List<Product> searchProductsByName(String name) {
        log.debug("Searching products by name: {}", name);
        List<Product> results = productRepository.findByNameContainingIgnoreCase(name);
        googleAnalyticsService.trackProductSearch(name, results.size());
        return results;
    }

    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        log.debug("Searching products with keyword: {}", keyword);
        Page<Product> results = productRepository.searchProducts(keyword, pageable);
        googleAnalyticsService.trackProductSearch(keyword, (int) results.getTotalElements());
        return results;
    }

    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.debug("Fetching products by price range: {} - {}", minPrice, maxPrice);
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Product> getLowStockProducts(Integer threshold) {
        log.debug("Fetching low stock products with threshold: {}", threshold);
        return productRepository.findByStockQuantityLessThan(threshold);
    }

    public Long countLowStockProducts(Integer threshold) {
        log.debug("Counting low stock products with threshold: {}", threshold);
        return productRepository.countLowStockProducts(threshold);
    }
}