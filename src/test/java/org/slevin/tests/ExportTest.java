package org.slevin.tests;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slevin.common.Emlak;
import org.slevin.dao.EmlakDao;
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
	
	@Test
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
