package com.microservices.product.repository;

import java.util.List;

import com.microservices.product.entity.Outlet;

public interface OutletRepository {
	public List<Outlet> getAllOutlet(long hashing, int page, int size, String orderby);
	public List<Outlet> getOutletById(long hashing, int id);
	public Outlet saveOutlet(long hashing, Outlet outlet);
	public Outlet updateOutlet(long hashing, Outlet outlet);
	public boolean deleteOutletById(long hashing, int id);
}
