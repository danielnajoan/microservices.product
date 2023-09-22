package com.microservices.product.manager;

import java.util.List;

import com.microservices.product.entity.request.OutletRequest;
import com.microservices.product.entity.vm.ManagerViewModel;
import com.microservices.product.entity.vm.OutletViewModel;

public interface OutletManager {
	public ManagerViewModel<List<OutletViewModel>> getAllOutlet(long hashing, int page, int size, String orderby);
	public ManagerViewModel<List<OutletViewModel>> getOutletById(long hashing, int id);
	public ManagerViewModel<OutletViewModel> insertOutlet(long hashing, OutletRequest request);
	public ManagerViewModel<OutletViewModel> updateOutlet(long hashing, OutletRequest request);
	public ManagerViewModel<OutletViewModel> deleteOutletById(long hashing, int id);
}
