package com.hygge.microservices.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hygge.microservices.dto.ProductRequest;
import com.hygge.microservices.dto.ProductResponse;
import com.hygge.microservices.model.Product;
import com.hygge.microservices.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

  private final ProductRepository productRepository;

  public ProductResponse createProduct(ProductRequest productRequest) {
    Product product = Product.builder()
        .name(productRequest.name())
        .description(productRequest.description())
        .price(productRequest.price())
        .build();

    productRepository.save(product);
    log.info("Product {} is saved", product.getId());
    return mapToProductResponse(product);
  }

  public List<ProductResponse> getAllProducts() {
    List<Product> products = productRepository.findAll();

    return products.stream().map(this::mapToProductResponse).toList();
  }

  private ProductResponse mapToProductResponse(Product product) {
    return new ProductResponse(product.getId(), product.getName(),
        product.getDescription(), product.getPrice());
  }
}
