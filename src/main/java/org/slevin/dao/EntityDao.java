package org.slevin.dao;

import java.util.List;

import org.hibernate.HibernateException;


public interface EntityDao<E> {
	
	void persist(E e) throws Exception;
	
	void merge(E e) throws Exception;

	void remove(Object id) throws Exception;
	
	E findById(Object id) throws Exception;
	
	List<E> findAll() throws Exception;
	
	List<E> findByProperty(String prop, Object val) throws Exception;
	
	List<E> findInRange(int firstResult, int maxResults) throws Exception;
	
	long count() throws Exception;
	
	public void  saveAll(List<E> list) throws HibernateException, Exception;
	
	public List<E> findByProperty(String prop, Object val,int limit) throws Exception;
	
	
}
