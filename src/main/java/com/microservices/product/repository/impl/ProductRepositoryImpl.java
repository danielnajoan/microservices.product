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
            String sql = "SELECT * FROM T_PRODUCT";
            Query query = session.createNativeQuery(sql, Product.class );
              
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
          String sql = "SELECT * FROM T_PRODUCT WHERE ID = ?";
          Query query = session.createNativeQuery(sql, Product.class ); 
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
	    Session session = entityManager.unwrap(Session.class);
	    Transaction transaction = null;
	    try {
	        transaction = session.beginTransaction();
	        String insertSql = "INSERT INTO T_PRODUCT (id, name, description, price, product_category_id) " +
	                "VALUES (t_product_id_seq.NEXTVAL, :name, :description, :price, :productCategoryId)";

	        Query insertQuery = session.createNativeQuery(insertSql);
	        insertQuery.setParameter("name", product.getName());
	        insertQuery.setParameter("description", product.getDescription());
	        insertQuery.setParameter("price", product.getPrice());
	        insertQuery.setParameter("productCategoryId", product.getProductCategory().getId());
	        insertQuery.executeUpdate();

            String getNewId = "SELECT t_product_id_seq.currval FROM DUAL";
            BigDecimal newId = (BigDecimal) session.createNativeQuery(getNewId).getSingleResult();

	        String updateTotalProductSql = "UPDATE T_PRODUCT_CATEGORY " +
	                "SET total_product = total_product + 1 " +
	                "WHERE id = :productCategoryId";

	        Query updateTotalProductQuery = session.createNativeQuery(updateTotalProductSql);
	        updateTotalProductQuery.setParameter("productCategoryId", product.getProductCategory().getId());
	        updateTotalProductQuery.executeUpdate();

	        String fetchProductSql = "SELECT * FROM T_PRODUCT WHERE id = :productId";
	        Query fetchProductQuery = session.createNativeQuery(fetchProductSql, Product.class);
	        fetchProductQuery.setParameter("productId", newId);

	        Product insertedProduct = (Product) fetchProductQuery.getSingleResult();
	        transaction.commit();
	        return insertedProduct;
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
	public Product updateProduct(long hashing, Product product) {
        Transaction transaction = null;
		Product newData = new Product();
        try (Session session = entityManager.unwrap(Session.class)) {
            transaction = session.beginTransaction();
            String sql = "UPDATE T_PRODUCT " +
                    "SET name = CASE WHEN :name IS NOT NULL AND :name <> name THEN :name ELSE name END, " +
                    "    description = CASE WHEN :description IS NOT NULL AND :description <> description THEN :description ELSE description END, " +
                    "    price = CASE WHEN :price IS NOT NULL AND :price <> price THEN :price ELSE price END, " +
                    "    product_category_id = CASE WHEN :productCategoryId IS NOT NULL AND :productCategoryId <> product_category_id THEN :productCategoryId ELSE product_category_id END " +
                    "WHERE id = :id " +
                    "RETURNING *";
            Query query = session.createNativeQuery(sql, Product.class);
            query.setParameter("name", product.getName());
            query.setParameter("description", product.getDescription());
            query.setParameter("price", product.getPrice());
            query.setParameter("productCategoryId", product.getProductCategory().getId());
            query.setParameter("id", product.getId());
            query.executeUpdate();
            
            List<Product> updatedProducts = query.getResultList();
            transaction.commit();
            return updatedProducts.isEmpty() ? null : updatedProducts.get(0);
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
            String deleteSql = "DELETE FROM T_PRODUCT WHERE id = :id";

            Query deleteQuery = session.createNativeQuery(deleteSql);
            deleteQuery.setParameter("id", id);
            int rowsAffected = deleteQuery.executeUpdate();

            if (rowsAffected > 0) {
                String updateTotalProductSql = "UPDATE T_PRODUCT_CATEGORY " +
                        "SET total_product = (SELECT COUNT(*) FROM T_PRODUCT WHERE product_category_id = :categoryId) " +
                        "WHERE id = (SELECT product_category_id FROM product WHERE id = :id)";
                Query updateQuery = session.createNativeQuery(updateTotalProductSql);
                updateQuery.setParameter("id", id);
                updateQuery.setParameter("categoryId", id);
                updateQuery.executeUpdate();
                transaction.commit();
                return true;
            } else {
                return false;
            }
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
