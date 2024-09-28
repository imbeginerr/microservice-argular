package com.hygge.microservices.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hygge.microservices.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
