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
	    Session session = entityManager.unwrap(Session.class);
	    Transaction transaction = null;

	    try {
	        transaction = session.beginTransaction();
	        String insertSql = "INSERT INTO T_PRODUCT_CATEGORY (id, name, outlet_id) " +
	                "VALUES (t_product_category_id_seq.NEXTVAL, :name, :outletId)";

	        Query insertQuery = session.createNativeQuery(insertSql);
	        insertQuery.setParameter("name", productCategory.getName());
	        insertQuery.setParameter("outletId", productCategory.getOutlet().getId());
	        insertQuery.executeUpdate();

            String getNewId = "SELECT t_product_category_id_seq.currval FROM DUAL";
            BigDecimal newId = (BigDecimal) session.createNativeQuery(getNewId).getSingleResult();

	        String updateTotalProductCategorySql = "UPDATE T_OUTLET " +
	                "SET total_product_category = total_product_category + 1 " +
	                "WHERE id = :outletId";

	        Query updateTotalProductCategoryQuery = session.createNativeQuery(updateTotalProductCategorySql);
	        updateTotalProductCategoryQuery.setParameter("outletId", productCategory.getOutlet().getId());
	        updateTotalProductCategoryQuery.executeUpdate();

	        String fetchProductCategorySql = "SELECT * FROM T_PRODUCT_CATEGORY WHERE id = :categoryId";
	        Query fetchProductCategoryQuery = session.createNativeQuery(fetchProductCategorySql, ProductCategory.class);
	        fetchProductCategoryQuery.setParameter("categoryId", newId);

	        ProductCategory insertedProductCategory = (ProductCategory) fetchProductCategoryQuery.getSingleResult();
	        transaction.commit();
	        return insertedProductCategory;
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
	public ProductCategory updateProductCategory(long hashing, ProductCategory productCategory) {
        Transaction transaction = null;
		ProductCategory newData = new ProductCategory();
        try (Session session = entityManager.unwrap(Session.class)) {
            transaction = session.beginTransaction();
            String sql = "UPDATE T_PRODUCT_CATEGORY " +
                    "SET name = CASE WHEN :name IS NOT NULL AND :name <> name THEN :name ELSE name END, " +
                    "    outlet_id = CASE WHEN :outletId IS NOT NULL AND :outletId <> outlet_id THEN :outletId ELSE outlet_id END " +
                    "WHERE id = :id " +
                    "RETURNING *";

            Query query = session.createNativeQuery(sql, ProductCategory.class);
            query.setParameter("name", productCategory.getName());
            query.setParameter("outletId", productCategory.getOutlet().getId());
            query.setParameter("id", productCategory.getId());

            List<ProductCategory> updatedCategories = query.getResultList();
            transaction.commit();
            return updatedCategories.isEmpty() ? null : updatedCategories.get(0);
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
            String deleteProductsSql = "DELETE FROM T_PRODUCT WHERE product_category_id = :productCategoryId";
            Query deleteProductsQuery = session.createNativeQuery(deleteProductsSql);
            deleteProductsQuery.setParameter("productCategoryId", id);
            int productsDeleted = deleteProductsQuery.executeUpdate();

            String deleteCategorySql = "DELETE FROM T_PRODUCT_CATEGORY WHERE id = :id";
            Query deleteCategoryQuery = session.createNativeQuery(deleteCategorySql);
            deleteCategoryQuery.setParameter("id", id);
            int categoryDeleted = deleteCategoryQuery.executeUpdate();

            if (productsDeleted > 0 || categoryDeleted > 0) {
                String updateTotalProductCategorySql = "UPDATE T_OUTLET " +
                        "SET total_product_category = (SELECT COUNT(*) FROM T_PRODUCT_CATEGORY WHERE outlet_id = :outletId) " +
                        "WHERE id = :outletId";
                Query updateQuery = session.createNativeQuery(updateTotalProductCategorySql);
                updateQuery.setParameter("outletId", id);
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
