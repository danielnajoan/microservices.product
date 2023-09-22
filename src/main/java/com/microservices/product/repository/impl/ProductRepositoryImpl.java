package com.microservices.product.repository.impl;

import org.springframework.stereotype.Repository;

import com.microservices.product.entity.Product;
import com.microservices.product.repository.ProductRepository;
import com.microservices.product.util.Utils;

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

@SuppressWarnings("unchecked")
@Repository
public class ProductRepositoryImpl implements ProductRepository {
	private static final Logger logger = LogManager.getLogger(ProductRepositoryImpl.class);
	   
  @PersistenceContext
  private EntityManager entityManager;
	
  	@Override
	public List<Product> getAllProduct(long hashing, int page, int size, String orderby) {
  		List<Product> listData = new ArrayList<>();
          try (Session session = entityManager.unwrap(Session.class)) {
            String hql = "SELECT * FROM T_PRODUCT";
            Query query = session.createNativeQuery(hql, Product.class );
              
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
	public List<Product> getProductById(long hashing, int id) {
  		List<Product> listData = new ArrayList<>();
        try (Session session = entityManager.unwrap(Session.class)) {
          String hql = "SELECT * FROM T_PRODUCT WHERE ID = ?";
          Query query = session.createNativeQuery(hql, Product.class ); 
          query.setParameter(1, id);  
          listData = query.getResultList();
      } catch (Exception e) {
			String errMsg = Utils.starckTraceToString(e);
			logger.error("(" + hashing + ") " + errMsg);
		}
		return listData;
	}
	
	@Override
	public Product saveProduct(long hashing, Product product) {
        Transaction transaction = null;
          try (Session session = entityManager.unwrap(Session.class)) {
              transaction = session.beginTransaction();
              String hql = "INSERT INTO T_PRODUCT (NAME,DESCRIPTION,PRICE,ID) "
              		+ "VALUES(?,?,?,t_product_id_seq.nextval)";
              Query query = session.createNativeQuery(hql); 
              query.setParameter(1, product.getName());
              query.setParameter(2, product.getDescription());
              query.setParameter(3, product.getPrice());
              query.executeUpdate();
              String hqlId = "SELECT t_product_id_seq.currval FROM DUAL";
              BigDecimal id = (BigDecimal) session.createNativeQuery(hqlId).getSingleResult();
              product.setId(id.intValue());
              
              transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
            	transaction.rollback();
            }
            String errMsg = Utils.starckTraceToString(e);
			logger.error("(" + hashing + ") " + errMsg);
		}
		return product;
    }

	@Override
	public Product updateProduct(long hashing, Product product) {
        Transaction transaction = null;
		Product newData = new Product();
        try (Session session = entityManager.unwrap(Session.class)) {
            transaction = session.beginTransaction();
            String hql = "UPDATE T_PRODUCT SET NAME = ?, DESCRIPTION = ?, PRICE = ? WHERE ID = ? ";
            Query query = session.createNativeQuery(hql); 
            query.setParameter(1, product.getName());
            query.setParameter(2, product.getDescription());
            query.setParameter(3, product.getPrice());
            query.setParameter(4, product.getId());
            query.executeUpdate();
            

            String hqlNewRecord = "SELECT * FROM T_PRODUCT WHERE ID = ?";
            Query queryNewRecord = session.createNativeQuery(hqlNewRecord, Product.class);
            queryNewRecord.setParameter(1, product.getId());
            newData = (Product) queryNewRecord.getSingleResult();
            
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
	public boolean deleteProductById(long hashing, int id) {
        Transaction transaction = null;
        try (Session session = entityManager.unwrap(Session.class)) {
            transaction = session.beginTransaction();
        	Product product = (Product) session.load(Product.class, id);
        	session.delete(product);
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
