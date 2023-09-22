package com.microservices.product.manager;

import java.util.List;

import com.microservices.product.entity.Product;
import com.microservices.product.entity.request.ProductRequest;
import com.microservices.product.entity.vm.ManagerViewModel;
import com.microservices.product.entity.vm.ProductViewModel;

public interface ProductManager {
	public ManagerViewModel<List<ProductViewModel>> getAllProduct(long hashing, int page, int size, String orderby);
	public ManagerViewModel<List<ProductViewModel>> getProductById(long hashing, int id);
	public ManagerViewModel<ProductViewModel> insertProduct(long hashing, ProductRequest request);
	public ManagerViewModel<ProductViewModel> updateProduct(long hashing, ProductRequest request);
	public ManagerViewModel<ProductViewModel> deleteProductById(long hashing, int id);
	public ProductViewModel entityToViewModel(Product product);
}
