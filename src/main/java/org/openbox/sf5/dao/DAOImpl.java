package org.openbox.sf5.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.openbox.sf5.model.AbstractDbEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DAOImpl implements DAO, Serializable {

	@Override
	public <T extends AbstractDbEntity> void add(T obj) {
		Session s = sessionFactory.openSession();
		s.beginTransaction();
		s.save(obj);
		s.getTransaction().commit();
		s.close();

	}

	@Override
	public <T extends AbstractDbEntity> void remove(Class<T> type, long id) {
		Session s = sessionFactory.openSession();
		s.beginTransaction();
		Object c = s.get(type, id);
		s.delete(c);
		s.getTransaction().commit();
		s.close();
	}

	@Override
	public <T extends AbstractDbEntity> void update(T obj) {
		Session s = sessionFactory.openSession();
		s.beginTransaction();
		s.update(obj);
		s.getTransaction().commit();
		s.close();
	}

	@Override
	public <T extends AbstractDbEntity> T select(Class<T> type, long id) {
		Session s = sessionFactory.openSession();
		s.beginTransaction();
		@SuppressWarnings("unchecked")
		T obj = s.get(type, id);
		s.close();
		return obj;
	}

	@Override
	public <T extends AbstractDbEntity> void saveOrUpdate(T obj) {
		Session s = sessionFactory.openSession();
		s.beginTransaction();
		s.saveOrUpdate(obj);
		s.getTransaction().commit();
		s.close();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractDbEntity> List<T> list(Class<T> type) {

		List<T> list = new ArrayList<>();

		Session s = sessionFactory.openSession();
		s.beginTransaction();
		list = s.createQuery("from " + type.getName() + " order by id").list();
		s.getTransaction().commit();
		s.close();
		return list;
	}

	@Override
	public <T extends AbstractDbEntity> List<T> ObjectsCriterionList(Class<T> type, Criterion criterion) {
		Session s = sessionFactory.openSession();
		Criteria criteria = s.createCriteria(type)

				.add(criterion)

				.addOrder(Order.desc("id"));
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); // kill
																					// duplicates

		@SuppressWarnings("unchecked")
		List<T> list = criteria.list();

		s.close();
		return list;
	}

	@Autowired
	private SessionFactory sessionFactory;

	public DAOImpl() {

	}

	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public DAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private static final long serialVersionUID = 643710250463318145L;
}
