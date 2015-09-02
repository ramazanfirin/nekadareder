package org.slevin.dao;

import java.io.IOException;
import java.util.List;

import org.slevin.common.Semt;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.prediction.Prediction;


public interface GooglePredictionDao {
	
	public  void analyze(Prediction prediction) throws IOException;
	public  String predict(List<Object> list) throws Exception;
	public  String train(Prediction prediction) throws IOException;

}
