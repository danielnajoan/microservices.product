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

import com.microservices.product.entity.Product;
import com.microservices.product.entity.request.ProductRequest;
import com.microservices.product.entity.vm.ManagerViewModel;
import com.microservices.product.entity.vm.ProductViewModel;
import com.microservices.product.manager.ProductManager;
import com.microservices.product.manager.ProductCategoryManager;
import com.microservices.product.repository.ProductRepository;
import com.microservices.product.util.ErrorDetailInfoList;
import com.microservices.product.util.Utils;

@Service
public class ProductManagerImpl extends ErrorDetailInfoList implements ProductManager{
	private static final Logger logger = LogManager.getLogger(ProductManagerImpl.class);
	
	@Autowired 
	ProductRepository productRepository;
	
	@Autowired 
	ProductCategoryManager productCategoryManager;
	
	@Override
  	@Transactional
	public ManagerViewModel<List<ProductViewModel>> getAllProduct(long hashing, int page, int size, String orderby) {
		ManagerViewModel<List<ProductViewModel>> mvm = new ManagerViewModel<>();
		List<ProductViewModel> lvm = new ArrayList<>();
		try {
			List<Product> listData = productRepository.getAllProduct(hashing, page, size, orderby);
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
	public ManagerViewModel<List<ProductViewModel>> getProductById(long hashing, int id) {
        ManagerViewModel<List<ProductViewModel>> mvm = new ManagerViewModel<>();
		List<ProductViewModel> lvm = new ArrayList<>();
		try {
			List<Product> listData = productRepository.getProductById(hashing, id);
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
	public ManagerViewModel<ProductViewModel> insertProduct(long hashing, ProductRequest request) {
        ManagerViewModel<ProductViewModel> mvm = new ManagerViewModel<>();
        try {
        	request.setId(null);
        	Product product = new Product();
    		BeanUtils.copyProperties(request, product);
			Product newData = productRepository.saveProduct(hashing, product);
    		if(newData.getId() != null) {
    			ProductViewModel vm = new ProductViewModel();
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
	public ManagerViewModel<ProductViewModel> updateProduct(long hashing, ProductRequest request) {
        ManagerViewModel<ProductViewModel> mvm = new ManagerViewModel<>();
        try {
        	Product product = new Product();
    		BeanUtils.copyProperties(request, product);
			Product newData = productRepository.updateProduct(hashing, product);
    		if(newData.getId() != null) {
    			ProductViewModel vm = new ProductViewModel();
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
	public ManagerViewModel<ProductViewModel> deleteProductById(long hashing, int id) {
		ManagerViewModel<ProductViewModel> mvm = new ManagerViewModel<>();
        try {
			boolean status = productRepository.deleteProductById(hashing, id);
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
	public ProductViewModel entityToViewModel(Product product) {
    	ProductViewModel vm = new ProductViewModel();
		BeanUtils.copyProperties(product, vm);
        return vm;
    }
}
