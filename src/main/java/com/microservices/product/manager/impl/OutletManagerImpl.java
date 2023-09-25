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

import com.microservices.product.entity.Outlet;
import com.microservices.product.entity.ProductCategory;
import com.microservices.product.entity.request.OutletRequest;
import com.microservices.product.entity.vm.ManagerViewModel;
import com.microservices.product.entity.vm.OutletViewModel;
import com.microservices.product.entity.vm.ProductCategoryViewModel;
import com.microservices.product.manager.OutletManager;
import com.microservices.product.manager.ProductCategoryManager;
import com.microservices.product.repository.OutletRepository;
import com.microservices.product.util.ErrorDetailInfoList;
import com.microservices.product.util.Utils;

@Service
public class OutletManagerImpl extends ErrorDetailInfoList implements OutletManager{
	private static final Logger logger = LogManager.getLogger(OutletManagerImpl.class);
	
	@Autowired 
	OutletRepository outletRepository;
	
	@Autowired 
	ProductCategoryManager productCategoryManager;
	
	@Override
  	@Transactional
	public ManagerViewModel<List<OutletViewModel>> getAllOutlet(long hashing, int page, int size, String orderby) {
		ManagerViewModel<List<OutletViewModel>> mvm = new ManagerViewModel<>();
		List<OutletViewModel> lvm = new ArrayList<>();
		try {
			List<Outlet> listData = outletRepository.getAllOutlet(hashing, page, size, orderby);
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
	public ManagerViewModel<List<OutletViewModel>> getOutletById(long hashing, int id) {
        ManagerViewModel<List<OutletViewModel>> mvm = new ManagerViewModel<>();
		List<OutletViewModel> lvm = new ArrayList<>();
		try {
			List<Outlet> listData = outletRepository.getOutletById(hashing, id);
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
	public ManagerViewModel<OutletViewModel> insertOutlet(long hashing, OutletRequest request) {
        ManagerViewModel<OutletViewModel> mvm = new ManagerViewModel<>();
        try {
        	request.setId(null);
        	Outlet outlet = new Outlet();
    		BeanUtils.copyProperties(request, outlet);
			Outlet newData = outletRepository.saveOutlet(hashing, outlet);
    		if(newData.getId() != null) {
    			OutletViewModel vm = new OutletViewModel();
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
	public ManagerViewModel<OutletViewModel> updateOutlet(long hashing, OutletRequest request) {
        ManagerViewModel<OutletViewModel> mvm = new ManagerViewModel<>();
        try {
        	Outlet outlet = new Outlet();
    		BeanUtils.copyProperties(request, outlet);
			Outlet newData = outletRepository.updateOutlet(hashing, outlet);
    		if(newData.getId() != null) {
    			OutletViewModel vm = new OutletViewModel();
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
	public ManagerViewModel<OutletViewModel> deleteOutletById(long hashing, int id) {
		ManagerViewModel<OutletViewModel> mvm = new ManagerViewModel<>();
        try {
			boolean status = outletRepository.deleteOutletById(hashing, id);
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
	
    private OutletViewModel entityToViewModel(Outlet outlet) {
    	OutletViewModel vm = new OutletViewModel();
		BeanUtils.copyProperties(outlet, vm, "productCategories");
		List<ProductCategory> listProductCategory = outlet.getProductCategories();
		List<ProductCategoryViewModel> listProductCategoryVm = listProductCategory.stream()
				.map(this::productCategoryEntityToProductCategoryViewModel)
				.collect(Collectors.toList());
		vm.setProductCategories(listProductCategoryVm);
        return vm;
    }
    
    private ProductCategoryViewModel productCategoryEntityToProductCategoryViewModel(ProductCategory productCategory) {
        return productCategoryManager.entityToViewModel(productCategory);
    }
}
