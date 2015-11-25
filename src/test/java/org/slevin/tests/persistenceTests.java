package org.slevin.tests;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.HibernateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slevin.common.BinaQueryItem;
import org.slevin.common.Emlak;
import org.slevin.common.Ilce;
import org.slevin.common.QualityReport;
import org.slevin.common.Sehir;
import org.slevin.common.Semt;
import org.slevin.dao.AmazonPredictionDao;
import org.slevin.dao.AzurePredictionDao;
import org.slevin.dao.BinaQueryDao;
import org.slevin.dao.EmlakDao;
import org.slevin.dao.GooglePredictionDao;
import org.slevin.dao.IlceDao;
import org.slevin.dao.ItemsDao;
import org.slevin.dao.OrdersDao;
import org.slevin.dao.ParserDao;
import org.slevin.dao.QualityReportDao;
import org.slevin.dao.SahibindenDao;
import org.slevin.dao.SehirDao;
import org.slevin.dao.UpgradeModelDao;
import org.slevin.util.ConvertUtil;
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
import com.google.api.services.prediction.model.Insert2;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.StorageObject;




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
	
	@Autowired
	AzurePredictionDao azurePredictionDao;
	
	@Autowired
	QualityReportDao qualityReportDao;
	
	@Autowired
	UpgradeModelDao upgradeModelDao;
	

	
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

		String result =googlePredictionDao.predict(list,"all");
		System.out.println(result);
	}
	
	//@Test
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
		String predictValue  = googlePredictionDao.predict(list,"all");
		
		String amazonPredictValue = amazonPredictionDao.predict(emlakQueryItem,"");
		System.out.println("google="+predictValue+",amazon="+amazonPredictValue);
	}
	
	//@Test
	public void prepareExport() throws HibernateException, Exception{
		List<Emlak> emlakList = emlakDao.findunExportedRecords(10000);
		for (Iterator iterator = emlakList.iterator(); iterator.hasNext();) {
			
			
			try {
				Date date = new Date();
				Emlak emlak = (Emlak) iterator.next();
//			if(emlak.getIlanNo()!=224533789)
//				continue;
					
				
				EmlakQueryItem emlakQueryItem = ConvertUtil.convertToEmlakQueryItem(emlak);
				
				emlakQueryItem.setOdaSayisi(ConvertUtil.prepareOdaSayisi(emlakQueryItem.getOdaSayisi()));
				emlakQueryItem.setBanyoSayisi(ConvertUtil.prepareBanyoSayisi(emlakQueryItem.getBanyoSayisi()));
				emlakQueryItem.setBinaYasi(ConvertUtil.prepareBinaYasi(emlakQueryItem.getBinaYasi()));
				emlakQueryItem.setBinaKatSayisi(ConvertUtil.prepareBinaKatSayisi(emlakQueryItem.getBinaKatSayisi()));
				emlakQueryItem.setBulunduguKat(ConvertUtil.prepareBulunduguKat(emlakQueryItem.getBulunduguKat()));
				
				
				emlak.setT1(getGooglePredict(emlakQueryItem));
				emlak.setT2(getAmazonPredict(emlakQueryItem));
				emlak.setT3(getAzurePredict(emlakQueryItem));
				emlak.setExportComplated(true);	
				Date date2 = new Date();
				
				System.out.println((date2.getTime()-date.getTime())+" ms. ilan no="+emlak.getIlanNo()+" "+emlak.getT1()+" "+emlak.getT2()+" "+emlak.getT3());
				emlakDao.merge(emlak);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public BigDecimal getGooglePredict(EmlakQueryItem emlakQueryItem,String trainModelName) throws Exception {
		return new BigDecimal(googlePredictionDao.predict(ConvertUtil.convertToObjectList(emlakQueryItem),trainModelName));
	}
	
	public BigDecimal getGooglePredict(EmlakQueryItem emlakQueryItem) throws Exception {
		return new BigDecimal(googlePredictionDao.predict(ConvertUtil.convertToObjectList(emlakQueryItem),"beylikduzuSegment"));
	}
	
	public BigDecimal getGooglePredict2(EmlakQueryItem emlakQueryItem) throws Exception {
		return new BigDecimal(googlePredictionDao.predict(ConvertUtil.convertToObjectList(emlakQueryItem),"beylikduzuSegmentBatch"));
	}
	
	public BigDecimal getAmazonPredict(EmlakQueryItem emlakQueryItem) throws Exception {
		return new BigDecimal(amazonPredictionDao.predict(emlakQueryItem,"ML model: Istanbul-Beylikduzu-segment.csv"));
	}
	
	public BigDecimal getAmazonPredict2(EmlakQueryItem emlakQueryItem) throws Exception {
		return new BigDecimal(amazonPredictionDao.predict(emlakQueryItem,"7batchtestdatasource model"));
	}
	
	public BigDecimal getAzurePredict(EmlakQueryItem emlakQueryItem) throws Exception {
		return new BigDecimal(azurePredictionDao.predict(emlakQueryItem,"beylikduzuSegment"));
	}
	
	//@Test
	public void testDataSource() throws IOException{
		amazonPredictionDao.testCloud();
	}
	
	public void testDataSourceFromRDS(){
		
	}
	
	
	//@Test
	public void testBatch() throws HibernateException, Exception{
		List<Emlak> emlakList = emlakDao.findunExportedRecords(100);
		for (Iterator iterator = emlakList.iterator(); iterator.hasNext();) {
			
			
			try {
				Date date = new Date();
				Emlak emlak = (Emlak) iterator.next();
//			if(emlak.getIlanNo()!=224533789)
//				continue;
					
				
				EmlakQueryItem emlakQueryItem = ConvertUtil.convertToEmlakQueryItem(emlak);
				
				emlakQueryItem.setOdaSayisi(ConvertUtil.prepareOdaSayisi(emlakQueryItem.getOdaSayisi()));
				emlakQueryItem.setBanyoSayisi(ConvertUtil.prepareBanyoSayisi(emlakQueryItem.getBanyoSayisi()));
				emlakQueryItem.setBinaYasi(ConvertUtil.prepareBinaYasi(emlakQueryItem.getBinaYasi()));
				emlakQueryItem.setBinaKatSayisi(ConvertUtil.prepareBinaKatSayisi(emlakQueryItem.getBinaKatSayisi()));
				emlakQueryItem.setBulunduguKat(ConvertUtil.prepareBulunduguKat(emlakQueryItem.getBulunduguKat()));
				
				BigDecimal predict1 = getGooglePredict(emlakQueryItem);
				BigDecimal predict2 = getGooglePredict2(emlakQueryItem);
	
				System.out.println("result ="+(predict1.longValue()/predict2.longValue()) +" "+ predict1+" "+predict2);
			}catch(Exception e){
				e.printStackTrace();
		}
			
		}
	}
	
	//@Test
	public void testGoogleBatch() throws Exception{
		//googlePredictionDao.insert();
		System.out.println("bitti");
	}
	
    //@Test
	public void exportByIlce() throws Exception{
		sahibindenDao.exportToFileByIlce("");
		System.out.println("bitti");
	}
	
	//@Test
    public void fileList() throws Exception{
		//deleteAllTrainingModels();
		
		List<StorageObject> objectList = googlePredictionDao.listBucket("nekadareder");
		for (Iterator iterator = objectList.iterator(); iterator.hasNext();) {
			StorageObject storageObject = (StorageObject) iterator.next();
			if (!trainingModelPresents(storageObject.getName())) {
				googlePredictionDao.insertTrainingModel(storageObject.getName(),storageObject.getBucket()+"/"+storageObject.getName());	
				System.out.println(storageObject.getName());
			}
			
		}
    }
    
  
    
    
    
    
    
    
    
    
    
    
    
    
  
    
    
    
    
   // @Test
    public void newDataModel(String path,String bucketName) throws Exception{
    	
    	
    	sahibindenDao.exportToFileByIlce("");
    	createBucket(bucketName);
    	uploadAllFiles(bucketName,path);
    	prepareTrainingModels(bucketName);
    	

     }
    
    //@Test
    public void testTrainingComplated(String directoryPath) throws Exception{
    	System.out.println("training model size="+getTrainingModels().size());
    	
    	File folder = new File(directoryPath);
    	File[] listOfFiles = folder.listFiles();
    	for (int i = 0; i < listOfFiles.length; i++) {
			if(!trainingModelPresents(listOfFiles[i].getName())){
				System.out.println(listOfFiles[i].getName() +" bulunamadi");
			}
			
    	
    }
    }
   // @Test
    public void insertTestData() throws HibernateException, Exception{
    	List<Emlak> emlakList ;
    	List<Ilce> ilceList = ilceDao.findByProperty("sehir.name", "İstanbul");
		for (Iterator iterator = ilceList.iterator(); iterator.hasNext();) {
			Ilce ilce = (Ilce) iterator.next();

			emlakList = emlakDao.exportByIlce(ilce.getName(), new BigDecimal(150000),new BigDecimal(350000));
			makePrediction(emlakList, ilce.getId()+"_"+ilce.getName() + "_1.cvs",1l);
			
			emlakList = emlakDao.exportByIlce(ilce.getName(), new BigDecimal(350000),new BigDecimal(550000));
			makePrediction(emlakList, ilce.getId()+"_"+ilce.getName() + "_2.cvs",2l);
			
			emlakList = emlakDao.exportByIlce(ilce.getName(), new BigDecimal(550000),new BigDecimal(800000));
			makePrediction(emlakList, ilce.getId()+"_"+ilce.getName() + "_3.cvs",3l);
			
			emlakList = emlakDao.exportByIlce(ilce.getName(), new BigDecimal(800000),new BigDecimal(10000000));
			makePrediction(emlakList, ilce.getId()+"_"+ilce.getName() + "_4.cvs",4l);
			
		}
		
		System.out.println("insert test bitti");
    }
		
	public void makePrediction(List<Emlak> emlakList,String trainingModelName,Long segment) throws Exception{
		try {
			Random rand = new Random(); 
			for (int i = 0; i < 10; i++) {
				Emlak emlak = emlakList.get(rand.nextInt(emlakList.size()));
				BigDecimal predict = predict(emlak, trainingModelName,false);
				
				BigDecimal predictOriginal=null;
				if(emlak.getIlce().equals("Beylikdüzü"))
					predictOriginal =predict(emlak, trainingModelName, true);
				createReport(emlak.getFiyatLong(),emlak.getIlce(),emlak.getSehir(),predict,predictOriginal,segment);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createReport(BigDecimal fiyat,String ilce,String sehir,BigDecimal prediction,BigDecimal predictionOriginal,Long segment) throws Exception{
		QualityReport report= new QualityReport();
		report.setFiyat(fiyat);
		report.setIlce(ilce);
		report.setSehir(sehir);
		report.setPrediction(prediction);
		report.setPredictionOriginal(predictionOriginal);
		report.setSuccessRate(new Double(fiyat.doubleValue()/prediction.doubleValue()));
		report.setSegment(segment);
		System.out.println("rate ="+report.getSuccessRate());
		qualityReportDao.persist(report);;
	}
		
		
	public BigDecimal predict(Emlak emlak,String trainingModelName,boolean forTest) throws Exception{
		EmlakQueryItem emlakQueryItem = ConvertUtil.convertToEmlakQueryItem(emlak);
		emlakQueryItem.setOdaSayisi(ConvertUtil.prepareOdaSayisi(emlakQueryItem.getOdaSayisi()));
		emlakQueryItem.setBanyoSayisi(ConvertUtil.prepareBanyoSayisi(emlakQueryItem.getBanyoSayisi()));
		emlakQueryItem.setBinaYasi(ConvertUtil.prepareBinaYasi(emlakQueryItem.getBinaYasi()));
		emlakQueryItem.setBinaKatSayisi(ConvertUtil.prepareBinaKatSayisi(emlakQueryItem.getBinaKatSayisi()));
		emlakQueryItem.setBulunduguKat(ConvertUtil.prepareBulunduguKat(emlakQueryItem.getBulunduguKat()));
		
		
		BigDecimal predict;
		if (!forTest) {
			predict = getGooglePredict(emlakQueryItem,trainingModelName);
		}else
			predict = getGooglePredict(emlakQueryItem);
		
		return predict;
	}
		
		
    
    public void createBucket(String bucketName) throws Exception{
    	List<Bucket> list = googlePredictionDao.listBuckets();
    	for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Bucket bucket = (Bucket) iterator.next();
			if(bucket.getName().equals(bucketName)){
				System.out.println("Bucket tespit edildi.");  
				return;
			}
		}
    	googlePredictionDao.createBucket(bucketName);
    	System.out.println("bucket olusturuldu");
    }
    
    public void uploadAllFiles(String bucketName,String directoryPath) throws Exception{
    	
    	List<String> currentBucketList=getBucketList(bucketName);
    	
    	File folder = new File(directoryPath);
    	File[] listOfFiles = folder.listFiles();
    	for (int i = 0; i < listOfFiles.length; i++) {
			try {
				File file = listOfFiles[i];
				if(!currentBucketList.contains(file.getName()))
					googlePredictionDao.uploadFile(bucketName, file.getPath());
			} catch (Exception e) {
				System.out.println("upload file hata:"+e.getMessage());
			}
		}
    	System.out.println("upload file tamamlandi");
    }
    
    public void prepareTrainingModels(String bucketName) throws Exception{
    	deleteAllTrainingModels();
    	traingNewModels(bucketName);
    }
    
    public void traingNewModels(String bucketName) throws Exception{
    	List<StorageObject> objectList = googlePredictionDao.listBucket(bucketName);
		for (Iterator iterator = objectList.iterator(); iterator.hasNext();) {
			StorageObject storageObject = (StorageObject) iterator.next();
			if (!trainingModelPresents(storageObject.getName())) {
				googlePredictionDao.insertTrainingModel(storageObject.getName(),storageObject.getBucket()+"/"+storageObject.getName());	
				
			}
			
		}
		
		System.out.println("insert training bitti");
    }

    
    public List<String> getBucketList(String bucketName) throws Exception{
    	List<String> currentBucketList= new ArrayList<String>();
    	List<StorageObject> currentFileList =googlePredictionDao.listBucket(bucketName);
		for (Iterator iterator = currentFileList.iterator(); iterator.hasNext();) {
			StorageObject storageObject = (StorageObject) iterator.next();
			currentBucketList.add(storageObject.getName());
		}
    	
    	return currentBucketList;
    }
    
    
    public Boolean trainingModelPresents(String name) throws Exception{
    	try {
			Insert2 get = googlePredictionDao.getTrainingModel(name);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
    }
    
    //@Test
    public void checkSize() throws Exception{
    	getTrainingModels();
    }
    
    public List<Insert2> getTrainingModels() throws Exception{
    	List<Insert2> list = googlePredictionDao.listTrainingModel();
    	//System.out.println("size " +list.size());
    	return list;
    } 
    
    public void deleteAllTrainingModels() throws Exception{
    	List<Insert2> list = getTrainingModels();
    	for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Insert2 insert2 = (Insert2) iterator.next();
			if(insert2.getId().equals("istanbul-Beylikduzu-segment.csv") || insert2.getId().equals("İstanbul-Beylikdüzü") ||  insert2.getId().equals("kgsahibindenallv2"))
				continue;
			googlePredictionDao.deleteTrainingModel(insert2.getId());
		}
    	System.out.println("delete training models bitti");
    }
    
    
    
    
    @Test
    public void main() throws Exception{
//    	String path="C:\\Users\\ETR00529\\Desktop\\sahibinden\\cloudData\\batch4\\";
//    	String bucketName = "11_12_2015_1";
//    	
//    	newDataModel(path,bucketName);
//    	testTrainingComplated(path);
    	//insertTestData();
    	
//upgradeModelDao.upgrade("19112015");
   // 	upgradeModelDao.checkUpgradeComplated("19112015");
    	
    	batchPredict();
    }
    
    public void batchPredict() throws Exception{
    	
    	Calendar cal = Calendar.getInstance();
    	cal.set(2015, 10, 15,0,0,0);
    	Date dateStart = cal.getTime();
    	
    	cal.set(2015, 10, 16,0,0,0);
    	Date dateEnd= cal.getTime();
    	
    	List<Emlak> emlakList = emlakDao.findAllEmlak(dateStart,dateEnd);
    	String trainingModelName;
    	String segment;
    	BigDecimal fiyat;
    	for (Iterator iterator = emlakList.iterator(); iterator.hasNext();) {
			Emlak emlak2 = (Emlak) iterator.next();
			Ilce ilce = getIlcebyName(emlak2.getIlce().replaceFirst(" ", ""));
			fiyat = new BigDecimal(emlak2.getFiyat());
			segment = getSegment(fiyat.longValue());
			
			trainingModelName=ilce.getId()+"_"+ilce.getName() + "_"+segment+".cvs";
			BigDecimal predict = predict(emlak2, trainingModelName,false);
			
			
			createReport(fiyat,emlak2.getIlce(),emlak2.getSehir(),predict,null,new Long(segment));
		}
    	
    	
    }
    
    public Ilce getIlcebyName(String name) throws Exception{
    	List<Ilce> ilceList = ilceDao.findByProperty("name",name);
    	return ilceList.get(0);
    }
    
    public String getSegment(Long fiyat){
    	String segment="5";
       	if(fiyat>10000 && fiyat<150000)
    		segment="0";
    	else if(fiyat>150000 && fiyat<350000)
    		segment="1";
    	else if(fiyat>350000 && fiyat<550000)
    		segment="2";
    	else if(fiyat>550000 && fiyat<800000)
    		segment="3";
    	else if(fiyat>800000 && fiyat<10000000)
    		segment="4";
    	else
    		segment="5";
    	
    	return segment;
    }
}
	
