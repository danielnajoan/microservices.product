package com.microservices.product.repository;

import java.util.List;

import com.microservices.product.entity.ProductCategory;

public interface ProductCategoryRepository {
	public List<ProductCategory> getAllProductCategory(long hashing, int page, int size, String orderby);
	public List<ProductCategory> getProductCategoryById(long hashing, int id);
	public ProductCategory saveProductCategory(long hashing, ProductCategory productCategory);
	public ProductCategory updateProductCategory(long hashing, ProductCategory productCategory);
	public boolean deleteProductCategoryById(long hashing, int id);
}
