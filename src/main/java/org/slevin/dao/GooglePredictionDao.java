package org.slevin.dao;

import java.io.IOException;
import java.util.List;

import com.google.api.services.prediction.Prediction;
import com.google.api.services.prediction.model.Insert2;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.StorageObject;


public interface GooglePredictionDao {
	
	public  void analyze(Prediction prediction) throws IOException;
	public  String predict(List<Object> list,String all) throws Exception;
	public  String train(Prediction prediction) throws IOException;
	public  void insertTrainingModel(String id,String location) throws Exception ;
	public Insert2 getTrainingModel(String name) throws Exception;
	public void deleteTrainingModel(String name) throws Exception;
	public List<Insert2> listTrainingModel() throws Exception;

	
	public List<Bucket> listBuckets() throws Exception;
	public List<StorageObject> listBucket(String bucketName) throws Exception;
	public void deleteBucket(String bucketName) throws Exception;
	public void createBucket(String bucketName) throws Exception;
	public void deleteFile(String bucketName, String fileName) throws Exception;
	public void uploadFile(String bucketName, String filePath) throws Exception;
}
