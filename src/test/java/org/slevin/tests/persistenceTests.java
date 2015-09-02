package org.slevin.tests;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slevin.common.BinaQueryItem;
import org.slevin.common.Ilce;
import org.slevin.common.Sehir;
import org.slevin.common.Semt;
import org.slevin.dao.AmazonPredictionDao;
import org.slevin.dao.BinaQueryDao;
import org.slevin.dao.EmlakDao;
import org.slevin.dao.IlceDao;
import org.slevin.dao.ItemsDao;
import org.slevin.dao.OrdersDao;
import org.slevin.dao.ParserDao;
import org.slevin.dao.GooglePredictionDao;
import org.slevin.dao.SahibindenDao;
import org.slevin.dao.SehirDao;
import org.slevin.util.EmlakQueryItem;
import org.slevin.util.HttpClientUtil;
import org.slevin.util.ParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;




@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class persistenceTests {

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
	
	//@Test
	@Transactional
	@Rollback(false)
	public void testDaoImpl() throws Exception {
		String url = "https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_country=1&sorting=bm&language=tr&pagingOffset=0&pagingSize=100";
		String result = HttpClientUtil.parse(url);
		//https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_country=1&sorting=bm&language=tr&pagingOffset=45&pagingSize=15		System.out.println(result);
		
		 url = "https://api.sahibinden.com/sahibinden-ral/rest/classifieds/__parameter__?language=tr";
		url = url.replace("__parameter__", "217387692");
		result = HttpClientUtil.parse(url);
		
		
		System.out.println(result);
		BinaQueryItem item = new BinaQueryItem();
		item.setUrl("asdasd");
		item.setContent(result);
		binaQueryDao.persist(item);
		System.out.println("bitti");
	}
	
	//@Test
	@Transactional
	@Rollback(false)
	public void query() throws Exception {
		BinaQueryItem item = binaQueryDao.findById(new Long(9));
		String context = item.getContent();
		
		JsonValue value = Json.parse(context);
		JsonObject object  = value.asObject();
		
		Boolean success = object.get("success").asBoolean();
		System.out.println("success:"+success);
		
		JsonObject response = object.get("response").asObject();
		response.get("id");
		
		System.out.println("bitti");
		
	}
	
	//@Test
	@Transactional
	@Rollback(false)
	public void serviceTest() throws Exception {
		String url = "https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_country=1&sorting=bm&a1966=true&language=tr&pagingOffset=__parameter__&pagingSize=100";
		  //https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_country=1&sorting=bm&a1966=true&language=tr&pagingOffset=30&pagingSize=15

		for(int i=0;i<=100;i=i+100){
			String urlTemp = url.replace("__parameter__", String.valueOf(i));
			sahibindenDao.getDataForCreditSuitable(urlTemp,true,new Long(i));
		}
	}

	
	//@Test
	@Transactional
	@Rollback(false)
	public void sehirInsert() throws Exception {
		String url = "https://api.sahibinden.com/sahibinden-ral/rest/countries/1/cities?language=tr";
		String result=HttpClientUtil.parse(url);
		List<Sehir> sehirList = ParseUtil.parseSehirList(result);
		sehirDao.saveAll(sehirList);
	}
	
	//@Test
	@Transactional
	@Rollback(false)
	public void ilceInsert() throws Exception {
		String url = "https://api.sahibinden.com/sahibinden-ral/rest/cities/_parameter_/towns?language=tr";
		List<Sehir> sehirList = sehirDao.findAll();
		sehirList = sehirDao.findByProperty("itemId", "34");
		for (Iterator iterator = sehirList.iterator(); iterator.hasNext();) {
			Sehir sehir = (Sehir) iterator.next();
			String urlTemp = url.replace("_parameter_", sehir.getItemId());
			String result=HttpClientUtil.parse(urlTemp);
			List<Ilce> ilceList = ParseUtil.parseIlceList(result);
			sehir.setIlceList(ilceList);
			sehirDao.merge(sehir);
			
		}
		
		
	}
	
	//@Test
	@Transactional
	@Rollback(false)
	public void semtInsert() throws Exception {
		String url = "https://api.sahibinden.com/sahibinden-ral/rest/towns/_parameter_/districts?language=tr";
		List<Ilce> ilceList = ilceDao.findAll();
		ilceList = ilceDao.findByProperty("itemId", "448");
		for (Iterator iterator = ilceList.iterator(); iterator.hasNext();) {
			Ilce ilce = (Ilce) iterator.next();
			String urlTemp = url.replace("_parameter_", ilce.getItemId());
			String result=HttpClientUtil.parse(urlTemp);
			List<Semt> semtList = ParseUtil.parseSemtList(result);
			ilce.setSemtList(semtList);
			ilceDao.merge(ilce);
			
		}
		
		
	}
	
	//@Test
	public void test() throws Exception {
		System.out.println("test");
	}
	
	//@Test
	public void towntest() throws Exception {
		String url = "https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_town=420&sorting=date_desc&a1966=true&language=tr&pagingOffset=15&pagingSize=15";
		       url = "https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_town=420&sorting=date_desc&a1966=true&language=tr&pagingOffset=0&pagingSize=100";
		       url = "https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_town=438&sorting=date_desc&a1966=true&language=tr&pagingOffset=0&pagingSize=100";	
		       
		       String result=HttpClientUtil.parse(url);
		
		System.out.println(result);
	}
	
	
	
	//@Test
	public void exportToFile() throws Exception{
		sahibindenDao.exportToFile();
	}
	//@Test
	public void predict() throws Exception{
		List<Object> list= new ArrayList<Object>();
		list.add("İstanbul");
		list.add("Beylikdüzü");
		list.add("Beylikdüzü OSB");
		list.add("true");
		list.add("Satılık Daire");
		list.add(new Long(2015));
		list.add(new Long(110));
		list.add(new Long(3));
		list.add(new Long(1));
		list.add(new Long(0));
		list.add(new Long(4));
		list.add(new Long(90));
		
		list.add("Doğalgaz (Kombi)");
		list.add("Boş");
		list.add("Hayır");
		list.add("İnşaat Firmasından");

		String result =googlePredictionDao.predict(list);
		System.out.println(result);
	}
	
	@Test
	public void predict2() throws Exception{
		EmlakQueryItem emlakQueryItem = new EmlakQueryItem();
		String url = "https://api.sahibinden.com/sahibinden-ral/rest/classifieds/__parameter__?language=tr";
		url = url.replace("__parameter__", "211648036");
		String result = HttpClientUtil.parse(url);
		ParseUtil.parseSingleEmlakData(emlakQueryItem,result);
		
		List<Object> list = new ArrayList<Object>();
		list.add(emlakQueryItem.getSehir());
		list.add(emlakQueryItem.getIlce());
		list.add(emlakQueryItem.getMah());
		list.add(emlakQueryItem.getKrediyeUygun());
		list.add(emlakQueryItem.getEmlakTipi());
		list.add(emlakQueryItem.getYil());
		list.add(emlakQueryItem.getM2());
		
		list.add(emlakQueryItem.getOdaSayisi());
		list.add(emlakQueryItem.getBanyoSayisi());
		list.add(emlakQueryItem.getBinaYasi());
		list.add(emlakQueryItem.getBinaKatSayisi());
		list.add(emlakQueryItem.getBulunduguKat());
		list.add(emlakQueryItem.getIsitma());
		list.add(emlakQueryItem.getKullanimDurumu());
		list.add(emlakQueryItem.getSiteIcinde());
		list.add(emlakQueryItem.getKimden());
		String predictValue  = googlePredictionDao.predict(list);
		
		String amazonPredictValue = amazonPredictionDao.predict(emlakQueryItem);
		System.out.println("google="+predictValue+",amazon="+amazonPredictValue);
	}
}
