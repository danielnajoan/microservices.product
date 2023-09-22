package com.microservices.product.repository;

import java.util.List;

import com.microservices.product.entity.Product;

public interface ProductRepository {
	public List<Product> getAllProduct(long hashing, int page, int size, String orderby);
	public List<Product> getProductById(long hashing, int id);
	public Product saveProduct(long hashing, Product product);
	public Product updateProduct(long hashing, Product product);
	public boolean deleteProductById(long hashing, int id);
}
