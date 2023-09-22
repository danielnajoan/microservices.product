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
        Transaction transaction = null;
          try (Session session = entityManager.unwrap(Session.class)) {
              transaction = session.beginTransaction();
              String hql = "INSERT INTO T_OUTLET (NAME,TOTAL_CATEGORY,ID) "
              		+ "VALUES(?,?,t_outlet_id_seq.nextval)";
              Query query = session.createNativeQuery(hql); 
              query.setParameter(1, outlet.getName());
              query.setParameter(2, outlet.getTotalCategory());
              query.executeUpdate();
              String hqlId = "SELECT t_outlet_id_seq.currval FROM DUAL";
              BigDecimal id = (BigDecimal) session.createNativeQuery(hqlId).getSingleResult();
              outlet.setId(id.intValue());
              
              transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
            	transaction.rollback();
            }
            String errMsg = Utils.starckTraceToString(e);
			logger.error("(" + hashing + ") " + errMsg);
		}
		return outlet;
    }

	@Override
	public Outlet updateOutlet(long hashing, Outlet outlet) {
        Transaction transaction = null;
		Outlet newData = new Outlet();
        try (Session session = entityManager.unwrap(Session.class)) {
            transaction = session.beginTransaction();
            String hql = "UPDATE T_OUTLET SET NAME = ?, TOTAL_CATEGORY = ? WHERE ID = ? ";
            Query query = session.createNativeQuery(hql); 
            query.setParameter(1, outlet.getName());
            query.setParameter(2, outlet.getTotalCategory());
            query.setParameter(3, outlet.getId());
            query.executeUpdate();
            

            String hqlNewRecord = "SELECT * FROM T_OUTLET WHERE ID = ?";
            Query queryNewRecord = session.createNativeQuery(hqlNewRecord, Outlet.class);
            queryNewRecord.setParameter(1, outlet.getId());
            newData = (Outlet) queryNewRecord.getSingleResult();
            
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
	public boolean deleteOutletById(long hashing, int id) {
        Transaction transaction = null;
        try (Session session = entityManager.unwrap(Session.class)) {
            transaction = session.beginTransaction();
            String hql = "DELETE FROM T_OUTLET WHERE ID = ?";
            Query query = session.createNativeQuery(hql, Outlet.class); 
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
