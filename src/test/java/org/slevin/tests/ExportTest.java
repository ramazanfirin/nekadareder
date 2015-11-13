package org.slevin.tests;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.HibernateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slevin.common.Emlak;
import org.slevin.dao.AmazonPredictionDao;
import org.slevin.dao.AzurePredictionDao;
import org.slevin.dao.EmlakDao;
import org.slevin.dao.GooglePredictionDao;
import org.slevin.util.ConvertUtil;
import org.slevin.util.EmlakQueryItem;
import org.slevin.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ExportTest {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
EmlakDao emlakDao;

	
	@Autowired
	GooglePredictionDao googlePredictionDao;
	
	@Autowired
	AmazonPredictionDao amazonPredictionDao;
	
	@Autowired
	AzurePredictionDao azurePredictionDao;
	//@Test
	public void exportToFile() throws Exception{
		FileUtil.resetFile();
		List<Emlak> emlakList = emlakDao.findByProperty("exportComplated", false, 10000);
		for (Iterator iterator = emlakList.iterator(); iterator.hasNext();) {
			Emlak emlak = (Emlak) iterator.next();
			FileUtil.appendToFile(emlak);
		}
		System.out.println("bitti");
	}
	
	
	
}
