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

import com.microservices.product.entity.ProductCategory;
import com.microservices.product.repository.ProductCategoryRepository;
import com.microservices.product.util.Utils;

@SuppressWarnings("unchecked")
@Repository
public class ProductCategoryRepositoryImpl implements ProductCategoryRepository {
	private static final Logger logger = LogManager.getLogger(ProductCategoryRepositoryImpl.class);
	   
  @PersistenceContext
  private EntityManager entityManager;
	
  	@Override
	public List<ProductCategory> getAllProductCategory(long hashing, int page, int size, String orderby) {
  		List<ProductCategory> listData = new ArrayList<>();
          try (Session session = entityManager.unwrap(Session.class)) {
            String hql = "SELECT * FROM T_PRODUCT_CATEGORY tpc "
            		+ "LEFT JOIN T_PRODUCT tp ON tpc.id = tp.product_category_id ";
            Query query = session.createNativeQuery(hql, ProductCategory.class );
              
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
	public List<ProductCategory> getProductCategoryById(long hashing, int id) {
  		List<ProductCategory> listData = new ArrayList<>();
        try (Session session = entityManager.unwrap(Session.class)) {
          String hql = "SELECT * FROM T_PRODUCT_CATEGORY tpc "
          		+ "LEFT JOIN T_PRODUCT tp ON tpc.id = tp.product_category_id "
          		+ "WHERE tpc.ID = ?";
          Query query = session.createNativeQuery(hql, ProductCategory.class ); 
          query.setParameter(1, id);  
          listData = query.getResultList();
      } catch (Exception e) {
			String errMsg = Utils.starckTraceToString(e);
			logger.error("(" + hashing + ") " + errMsg);
		}
		return listData;
	}
	
	@Override
	public ProductCategory saveProductCategory(long hashing, ProductCategory productCategory) {
        Transaction transaction = null;
          try (Session session = entityManager.unwrap(Session.class)) {
              transaction = session.beginTransaction();
              String hql = "INSERT INTO T_PRODUCT_CATEGORY (NAME,TOTAL_PRODUCT,ID) "
              		+ "VALUES(?,?,t_product_category_id_seq.nextval)";
              Query query = session.createNativeQuery(hql); 
              query.setParameter(1, productCategory.getName());
              query.setParameter(2, productCategory.getTotalProduct());
              query.executeUpdate();
              String hqlId = "SELECT t_product_category_id_seq.currval FROM DUAL";
              BigDecimal id = (BigDecimal) session.createNativeQuery(hqlId).getSingleResult();
              productCategory.setId(id.intValue());
              
              transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
            	transaction.rollback();
            }
            String errMsg = Utils.starckTraceToString(e);
			logger.error("(" + hashing + ") " + errMsg);
		}
		return productCategory;
    }

	@Override
	public ProductCategory updateProductCategory(long hashing, ProductCategory productCategory) {
        Transaction transaction = null;
		ProductCategory newData = new ProductCategory();
        try (Session session = entityManager.unwrap(Session.class)) {
            transaction = session.beginTransaction();
            String hql = "UPDATE T_PRODUCT_CATEGORY SET NAME = ?, TOTAL_PRODUCT = ? WHERE ID = ? ";
            Query query = session.createNativeQuery(hql); 
            query.setParameter(1, productCategory.getName());
            query.setParameter(2, productCategory.getTotalProduct());
            query.setParameter(3, productCategory.getId());
            query.executeUpdate();
            

            String hqlNewRecord = "SELECT * FROM T_PRODUCT_CATEGORY WHERE ID = ?";
            Query queryNewRecord = session.createNativeQuery(hqlNewRecord, ProductCategory.class);
            queryNewRecord.setParameter(1, productCategory.getId());
            newData = (ProductCategory) queryNewRecord.getSingleResult();
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
            	transaction.rollback();
            }
			String errMsg = Utils.starckTraceToString(e);
			logger.error("(" + hashing + ") " + errMsg);
		}
		return newData;
	}

	@Override
	public boolean deleteProductCategoryById(long hashing, int id) {
        Transaction transaction = null;
        try (Session session = entityManager.unwrap(Session.class)) {
            transaction = session.beginTransaction();
            String hql = "DELETE FROM T_PRODUCT_CATEGORY WHERE ID = ?";
            Query query = session.createNativeQuery(hql, ProductCategory.class); 
            query.setParameter(1, id);
            query.executeUpdate();
            transaction.commit();
            return true;
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
