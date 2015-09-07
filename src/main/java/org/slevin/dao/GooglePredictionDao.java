package org.slevin.dao;

import java.io.IOException;
import java.util.List;

import com.google.api.services.prediction.Prediction;


public interface GooglePredictionDao {
	
	public  void analyze(Prediction prediction) throws IOException;
	public  String predict(List<Object> list) throws Exception;
	public  String train(Prediction prediction) throws IOException;

}
