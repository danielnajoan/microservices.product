package com.microservices.product.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservices.product.entity.ProductCategory;
import com.microservices.product.entity.Product;
import com.microservices.product.entity.request.ProductCategoryRequest;
import com.microservices.product.entity.vm.ManagerViewModel;
import com.microservices.product.entity.vm.ProductCategoryViewModel;
import com.microservices.product.entity.vm.ProductViewModel;
import com.microservices.product.manager.ProductCategoryManager;
import com.microservices.product.manager.ProductManager;
import com.microservices.product.repository.ProductCategoryRepository;
import com.microservices.product.util.ErrorDetailInfoList;
import com.microservices.product.util.Utils;

@Service
public class ProductCategoryManagerImpl extends ErrorDetailInfoList implements ProductCategoryManager{
	private static final Logger logger = LogManager.getLogger(ProductCategoryManagerImpl.class);
	
	@Autowired 
	ProductCategoryRepository productCategoryRepository;
	
	@Autowired 
	ProductManager productManager;
	
	@Override
  	@Transactional
	public ManagerViewModel<List<ProductCategoryViewModel>> getAllProductCategory(long hashing, int page, int size, String orderby) {
		ManagerViewModel<List<ProductCategoryViewModel>> mvm = new ManagerViewModel<>();
		List<ProductCategoryViewModel> lvm = new ArrayList<>();
		try {
			List<ProductCategory> listData = productCategoryRepository.getAllProductCategory(hashing, page, size, orderby);
			System.out.println(listData.toString());
			if (null != listData && listData.size() > 0) {
		        lvm = listData.stream().map(this::entityToViewModel).collect(Collectors.toList());
				mvm.setContent(lvm);
				mvm.setInfo(getInfoOk("Success"));
				mvm.setTotalRows(lvm.size());
			} else {
				mvm.setContent(null);
				mvm.setInfo(getInfoNoContent("No Data Found"));
				mvm.setTotalRows(0);
			}
		} catch (Exception e) {
			String errMsg = Utils.starckTraceToString(e);
			logger.error("(" + hashing + ") " + errMsg);
			mvm.setContent(null);
			mvm.setInfo(getInfoConflict("Error"));
			mvm.setTotalRows(0);
		}
        return mvm;
	}

	@Override
  	@Transactional
	public ManagerViewModel<List<ProductCategoryViewModel>> getProductCategoryById(long hashing, int id) {
        ManagerViewModel<List<ProductCategoryViewModel>> mvm = new ManagerViewModel<>();
		List<ProductCategoryViewModel> lvm = new ArrayList<>();
		try {
			List<ProductCategory> listData = productCategoryRepository.getProductCategoryById(hashing, id);
			if (null != listData && listData.size() > 0) {
		        lvm = listData.stream().map(this::entityToViewModel).collect(Collectors.toList());
				mvm.setContent(lvm);
				mvm.setInfo(getInfoOk("Success"));
				mvm.setTotalRows(lvm.size());
			} else {
				mvm.setContent(null);
				mvm.setInfo(getInfoNoContent("No Data Found"));
				mvm.setTotalRows(0);
			}
		} catch (Exception e) {
			String errMsg = Utils.starckTraceToString(e);
			logger.error("(" + hashing + ") " + errMsg);
			mvm.setContent(null);
			mvm.setInfo(getInfoConflict("Error"));
			mvm.setTotalRows(0);
		}
        return mvm;
	}

	@Override
	public ManagerViewModel<ProductCategoryViewModel> insertProductCategory(long hashing, ProductCategoryRequest request) {
        ManagerViewModel<ProductCategoryViewModel> mvm = new ManagerViewModel<>();
        try {
        	request.setId(null);
        	ProductCategory productCategory = new ProductCategory();
    		BeanUtils.copyProperties(request, productCategory);
			ProductCategory newData = productCategoryRepository.saveProductCategory(hashing, productCategory);
    		if(newData.getId() != null) {
    			ProductCategoryViewModel vm = new ProductCategoryViewModel();
        		BeanUtils.copyProperties(newData, vm);
				mvm.setContent(vm);
				mvm.setInfo(getInfoOk("Success"));
				mvm.setTotalRows(1);
    		} else {
				mvm.setContent(null);
				mvm.setInfo(getInfoNoContent("No Data Found"));
				mvm.setTotalRows(0);
    		}
		} catch (Exception e) {
			String errMsg = Utils.starckTraceToString(e);
			logger.error("(" + hashing + ") " + errMsg);
			mvm.setContent(null);
			mvm.setInfo(getInfoConflict("Error"));
			mvm.setTotalRows(0);
		}
        return mvm;
	}

	@Override
	public ManagerViewModel<ProductCategoryViewModel> updateProductCategory(long hashing, ProductCategoryRequest request) {
        ManagerViewModel<ProductCategoryViewModel> mvm = new ManagerViewModel<>();
        try {
        	ProductCategory productCategory = new ProductCategory();
    		BeanUtils.copyProperties(request, productCategory);
			ProductCategory newData = productCategoryRepository.updateProductCategory(hashing, productCategory);
    		if(newData.getId() != null) {
    			ProductCategoryViewModel vm = new ProductCategoryViewModel();
        		BeanUtils.copyProperties(newData, vm, "productCategories");
				mvm.setContent(vm);
				mvm.setInfo(getInfoOk("Success"));
				mvm.setTotalRows(1);
    		} else {
				mvm.setContent(null);
				mvm.setInfo(getInfoNoContent("No Data Found"));
				mvm.setTotalRows(0);
    		}
		} catch (Exception e) {
			String errMsg = Utils.starckTraceToString(e);
			logger.error("(" + hashing + ") " + errMsg);
			mvm.setContent(null);
			mvm.setInfo(getInfoConflict("Error"));
			mvm.setTotalRows(0);
		}
        return mvm;
	}

	@Override
	public ManagerViewModel<ProductCategoryViewModel> deleteProductCategoryById(long hashing, int id) {
		ManagerViewModel<ProductCategoryViewModel> mvm = new ManagerViewModel<>();
        try {
			boolean status = productCategoryRepository.deleteProductCategoryById(hashing, id);
    		if(status == true) {
				mvm.setContent(null);
				mvm.setInfo(getInfoOk("Success"));
				mvm.setTotalRows(0);
    		} else {
				mvm.setContent(null);
				mvm.setInfo(getInfoConflict("Failed To Delete"));
				mvm.setTotalRows(0);
    		}
		} catch (Exception e) {
			String errMsg = Utils.starckTraceToString(e);
			logger.error("(" + hashing + ") " + errMsg);
			mvm.setContent(null);
			mvm.setInfo(getInfoConflict("Error"));
			mvm.setTotalRows(0);
		}
        return mvm;
	}
	
	@Override
	public ProductCategoryViewModel entityToViewModel(ProductCategory productCategory) {
    	ProductCategoryViewModel vm = new ProductCategoryViewModel();
    	BeanUtils.copyProperties(productCategory, vm,"products");
		List<Product> listProduct = productCategory.getProducts();
		List<ProductViewModel> listProductVm = listProduct.stream()
				.map(this::productEntityToProductViewModel)
				.collect(Collectors.toList());
		vm.setProducts(listProductVm);
        return vm;
    }
    
    private ProductViewModel productEntityToProductViewModel(Product product) {
    	return productManager.entityToViewModel(product);
    }
}
