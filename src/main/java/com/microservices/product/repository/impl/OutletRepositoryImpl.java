package com.microservices.product.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.microservices.product.entity.Outlet;
import com.microservices.product.repository.OutletRepository;
import com.microservices.product.util.Utils;

@SuppressWarnings("unchecked")
@Repository
public class OutletRepositoryImpl implements OutletRepository {
	private static final Logger logger = LogManager.getLogger(OutletRepositoryImpl.class);
	   
  @PersistenceContext
  private EntityManager entityManager;
	
  	@Override
	public List<Outlet> getAllOutlet(long hashing, int page, int size, String orderby) {
  		List<Outlet> listData = new ArrayList<>();
          try (Session session = entityManager.unwrap(Session.class)) {
            String hql = "SELECT * FROM T_OUTLET tol "
            		+ "LEFT JOIN T_PRODUCT_CATEGORY tpc ON tol.id = tpc.outlet_id "
            		+ "LEFT JOIN T_PRODUCT tp ON tpc.id = tp.product_category_id ";
            Query query = session.createNativeQuery(hql, Outlet.class );
              
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
              
            listData = query.getResultList();
        } catch (Exception e) {
			String errMsg = Utils.starckTraceToString(e);
			logger.error("(" + hashing + ") " + errMsg);
		}
		return listData;
    }
	
	@Override
	public List<Outlet> getOutletById(long hashing, int id) {
  		List<Outlet> listData = new ArrayList<>();
        try (Session session = entityManager.unwrap(Session.class)) {
          String hql = "SELECT * FROM T_OUTLET tol "
          		+ "LEFT JOIN T_PRODUCT_CATEGORY tpc ON tol.id = tpc.outlet_id "
          		+ "LEFT JOIN T_PRODUCT tp ON tpc.id = tp.product_category_id "
          		+ "WHERE tol.ID = ?";
          Query query = session.createNativeQuery(hql, Outlet.class ); 
          query.setParameter(1, id);  
          listData = query.getResultList();
      } catch (Exception e) {
			String errMsg = Utils.starckTraceToString(e);
			logger.error("(" + hashing + ") " + errMsg);
		}
		return listData;
	}
	
	@Override
	public Outlet saveOutlet(long hashing, Outlet outlet) {
	    Session session = entityManager.unwrap(Session.class);
	    Transaction transaction = null;
	    try {
	        transaction = session.beginTransaction();

	        String insertSql = "INSERT INTO T_OUTLET (id, name) VALUES (t_outlet_id_seq.NEXTVAL, :name)";
	        Query insertQuery = session.createNativeQuery(insertSql);
	        insertQuery.setParameter("name", outlet.getName());
	        insertQuery.executeUpdate();
	        
            String getNewId = "SELECT t_outlet_id_seq.currval FROM DUAL";
            BigDecimal newId = (BigDecimal) session.createNativeQuery(getNewId).getSingleResult();
            
            // Create native query to select the newly added outlet
            String fetchOutletSql = "SELECT * FROM T_OUTLET WHERE id = " + newId;
            Query fetchOutletQuery = session.createNativeQuery(fetchOutletSql, Outlet.class);
            Outlet insertedOutlet = (Outlet) fetchOutletQuery.getSingleResult();


	        transaction.commit();

	        return insertedOutlet;
	    } catch (Exception e) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
			String errMsg = Utils.starckTraceToString(e);
			logger.error("(" + hashing + ") " + errMsg);
	    } finally {
	        session.close();
	    }
	    return null;
	}

	@Override
	public Outlet updateOutlet(long hashing, Outlet outlet) {
	    Session session = entityManager.unwrap(Session.class);
	    Transaction transaction = null;

	    try {
	        transaction = session.beginTransaction();
	        String updateSql = "UPDATE T_OUTLET SET name = :name WHERE id = :outletId";

	        Query updateQuery = session.createNativeQuery(updateSql);
	        updateQuery.setParameter("name", outlet.getName());
	        updateQuery.setParameter("outletId", outlet.getId());

	        int updatedRows = updateQuery.executeUpdate();

	        if (updatedRows > 0) {
	            String fetchOutletSql = "SELECT * FROM T_OUTLET WHERE id = :outletId";
	            Query fetchOutletQuery = session.createNativeQuery(fetchOutletSql, Outlet.class);
	            fetchOutletQuery.setParameter("outletId", outlet.getId());

	            Outlet updatedOutlet = (Outlet) fetchOutletQuery.getSingleResult();
	            transaction.commit();
	            return updatedOutlet;
	        }
	    } catch (Exception e) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
			String errMsg = Utils.starckTraceToString(e);
			logger.error("(" + hashing + ") " + errMsg);
	    } finally {
	        session.close();
	    }
	    return null;
	}

	@Override
	public boolean deleteOutletById(long hashing, int id) {
        Transaction transaction = null;
        try (Session session = entityManager.unwrap(Session.class)) {
            transaction = session.beginTransaction();
            String deleteProductsSql = "DELETE FROM T_PRODUCT WHERE product_category_id IN " +
                    "(SELECT id FROM T_PRODUCT_CATEGORY WHERE outlet_id = :outletId)";
            Query deleteProductsQuery = session.createNativeQuery(deleteProductsSql);
            deleteProductsQuery.setParameter("outletId", id);
            int productsDeleted = deleteProductsQuery.executeUpdate();

            String deleteCategoriesSql = "DELETE FROM T_PRODUCT_CATEGORY WHERE outlet_id = :outletId";
            Query deleteCategoriesQuery = session.createNativeQuery(deleteCategoriesSql);
            deleteCategoriesQuery.setParameter("outletId", id);
            int categoriesDeleted = deleteCategoriesQuery.executeUpdate();

            String deleteOutletSql = "DELETE FROM T_OUTLET WHERE id = :id";
            Query deleteOutletQuery = session.createNativeQuery(deleteOutletSql);
            deleteOutletQuery.setParameter("id", id);
            int outletDeleted = deleteOutletQuery.executeUpdate();

            transaction.commit();
            return productsDeleted > 0 || categoriesDeleted > 0 || outletDeleted > 0;
        } catch (Exception e) {
            if (transaction != null) {
            	transaction.rollback();
            }
			String errMsg = Utils.starckTraceToString(e);
			logger.error("(" + hashing + ") " + errMsg);
			return false;
		}
	}
}
