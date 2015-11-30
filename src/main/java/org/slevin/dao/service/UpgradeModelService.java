package org.slevin.dao.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slevin.dao.GooglePredictionDao;
import org.slevin.dao.SahibindenDao;
import org.slevin.dao.UpgradeModelDao;
import org.slevin.prime.faces.bean.QueryMB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.services.prediction.model.Insert2;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.StorageObject;

@Component
@Transactional
public class UpgradeModelService implements UpgradeModelDao{

	String path;
	
	@Autowired
	SahibindenDao sahibindenDao;
	
	@Autowired
	GooglePredictionDao googlePredictionDao;
	
	@PostConstruct
	public void init() throws IOException{
		Properties prop = new Properties();
		prop.load(QueryMB.class.getClassLoader().getResourceAsStream("META-INF/spring/database.properties"));
		path = prop.getProperty("upgradePath");
	}
	
	@Override
	public void upgrade(String bucketName) throws Exception {
		String directoryName =path+bucketName ;
		System.out.println("directory name:"+directoryName);
		File file = new File(directoryName);
		if(!file.exists()){
			file.mkdir();
			System.out.println(directoryName+" olusturuldu");
		}else{
			System.out.println(directoryName+" mevcut");
		}
		
		
	sahibindenDao.exportToFileByIlce(directoryName);
		createBucket(bucketName);
		uploadAllFiles(bucketName,directoryName);
		prepareTrainingModels(bucketName);
		//traingNewModelsByLocal(bucketName,directoryName);
	}
	
	
	@Override
	public void checkUpgradeComplated(String bucketName) throws Exception {
		String directoryName =path+bucketName ;
		File folder = new File(directoryName);
    	File[] listOfFiles = folder.listFiles();
		System.out.println("lokal dosya sayisi:"+listOfFiles.length);
		
//		List<String> bucketFileList =getBucketList(bucketName);
//		System.out.println("bucket dosya sayisi:"+bucketFileList.size());
		
		List<String> trainingList= getTrainingModelsAsString();
		System.out.println("training Model sayisi:"+trainingList.size());
		
		for (int i = 0; i < listOfFiles.length; i++) {
			
		File file = listOfFiles[i];
			if(!trainingList.contains(file.getName())){
				try {
					//googlePredictionDao.insertTrainingModel(file.getName(),bucketName+"/"+file.getName());	
					System.out.println(file.getName()+" tekrar insert edildi.");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	 public void prepareTrainingModels(String bucketName) throws Exception{
	    	//deleteAllTrainingModels();
	    	traingNewModels(bucketName);
	    }
	    
	    public void traingNewModels(String bucketName) throws Exception{
	    	List<StorageObject> objectList = googlePredictionDao.listBucket(bucketName);
			for (Iterator iterator = objectList.iterator(); iterator.hasNext();) {
				StorageObject storageObject = (StorageObject) iterator.next();
				if (!trainingModelPresents(storageObject.getName())) {
					try {
						googlePredictionDao.insertTrainingModel(storageObject.getName(),storageObject.getBucket()+"/"+storageObject.getName());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					System.out.println(storageObject.getName()+" model insert yapildi.");
				}else{
					System.out.println(storageObject.getName()+" model mevcut.");
				}
				
			}
			
			System.out.println("insert training bitti");
	    }
	    
	    public void traingNewModelsByLocal(String bucketName,String directoryPath) throws Exception{
	    	File folder = new File(directoryPath);
	    	File[] listOfFiles = folder.listFiles();
			
	    	for (int i = 0; i < listOfFiles.length; i++) {
				if (!trainingModelPresents(listOfFiles[i].getName())) {
					try {
						googlePredictionDao.insertTrainingModel(listOfFiles[i].getName(),bucketName+"/"+listOfFiles[i].getName());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					System.out.println(listOfFiles[i].getName()+" model insert yapildi."+i);
				}else{
					System.out.println(listOfFiles[i].getName()+" model mevcut."+i);
				}
				
			}
	    	System.out.println("insert training bitti");
	    }
	    
	    public Boolean trainingModelPresents(String name) throws Exception{
	    	try {
				Insert2 get = googlePredictionDao.getTrainingModel(name);
				return false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return false;
			}
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
	    
	    public List<Insert2> getTrainingModels() throws Exception{
	    	List<Insert2> list = googlePredictionDao.listTrainingModel();
	    	//System.out.println("size " +list.size());
	    	return list;
	    }
	    
	    public List<String> getTrainingModelsAsString() throws Exception{
	    	List<String> resultList = new ArrayList<String>();
	    	List<Insert2> list = getTrainingModels();
	    	//System.out.println("size " +list.size());
	    	for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Insert2 insert2 = (Insert2) iterator.next();
				resultList.add(insert2.getId());
			}
	    	
	    	return resultList;
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
	    	File file=new File("test");
	    	for (int i = 0; i < listOfFiles.length; i++) {
				try {
					file = listOfFiles[i];
					if(!currentBucketList.contains(file.getName()))
						googlePredictionDao.uploadFile(bucketName, file.getPath());
				} catch (Exception e) {
					System.out.println("upload file hata:"+file.getName()+" " +e.getMessage());
				}
			}
	    	System.out.println("upload file tamamlandi");
	    }
	 
	  public List<String> getBucketList(String bucketName) throws Exception{
	    	List<String> currentBucketList= new ArrayList<String>();
	    	List<StorageObject> currentFileList =googlePredictionDao.listBucket(bucketName);
			if(currentFileList==null)
				return new ArrayList<String>();
	    	for (Iterator iterator = currentFileList.iterator(); iterator.hasNext();) {
				StorageObject storageObject = (StorageObject) iterator.next();
				currentBucketList.add(storageObject.getName());
			}
	    	
	    	return currentBucketList;
	    }

	
}
