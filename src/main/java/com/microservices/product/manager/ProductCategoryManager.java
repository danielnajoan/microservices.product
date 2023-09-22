package com.microservices.product.manager;

import java.util.List;

import com.microservices.product.entity.ProductCategory;
import com.microservices.product.entity.request.ProductCategoryRequest;
import com.microservices.product.entity.vm.ManagerViewModel;
import com.microservices.product.entity.vm.ProductCategoryViewModel;

public interface ProductCategoryManager {
	public ManagerViewModel<List<ProductCategoryViewModel>> getAllProductCategory(long hashing, int page, int size, String orderby);
	public ManagerViewModel<List<ProductCategoryViewModel>> getProductCategoryById(long hashing, int id);
	public ManagerViewModel<ProductCategoryViewModel> insertProductCategory(long hashing, ProductCategoryRequest request);
	public ManagerViewModel<ProductCategoryViewModel> updateProductCategory(long hashing, ProductCategoryRequest request);
	public ManagerViewModel<ProductCategoryViewModel> deleteProductCategoryById(long hashing, int id);
	public ProductCategoryViewModel entityToViewModel(ProductCategory productCategory);

}
