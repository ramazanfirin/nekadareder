package org.slevin.tests;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slevin.dao.AmazonPredictionDao;
import org.slevin.dao.AzurePredictionDao;
import org.slevin.dao.BinaQueryDao;
import org.slevin.dao.EmlakDao;
import org.slevin.dao.GooglePredictionDao;
import org.slevin.dao.IlceDao;
import org.slevin.dao.ItemsDao;
import org.slevin.dao.OrdersDao;
import org.slevin.dao.ParserDao;
import org.slevin.dao.SahibindenDao;
import org.slevin.dao.SehirDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class DatasourceTest {

	@PersistenceContext
	private EntityManager entityManager;

	
	@Autowired
    OrdersDao orderDao;
	
	@Autowired
	ItemsDao itemDao;
	
	@Autowired
	SahibindenDao sahibindenDao;
	
	@Autowired
	ParserDao parserDao;
	
	@Autowired
	BinaQueryDao binaQueryDao;
	
	@Autowired
	SehirDao sehirDao;
	
	@Autowired
	IlceDao ilceDao;
	
	@Autowired
	EmlakDao emlakDao;
	
	@Autowired
	GooglePredictionDao googlePredictionDao;
	
	@Autowired
	AmazonPredictionDao amazonPredictionDao;
	
	@Autowired
	AzurePredictionDao azurePredictionDao;
	
	
	@Test
	@Transactional
	@Rollback(false)
	public void testDataSource(){
		//amazonPredictionDao.testCloud();
		//System.out.println("bitti");
	}
}
