package org.slevin.dao;

import org.slevin.util.EmlakQueryItem;


public interface AzurePredictionDao {
	public  String predict(EmlakQueryItem emlakQueryItem) throws Exception;
}
