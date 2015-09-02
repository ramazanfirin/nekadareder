package org.slevin.dao;

import org.slevin.util.EmlakQueryItem;


public interface AmazonPredictionDao {
	public  String predict(EmlakQueryItem emlakQueryItem) throws Exception;
}
