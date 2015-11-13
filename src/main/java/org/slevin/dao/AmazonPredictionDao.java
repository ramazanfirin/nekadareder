package org.slevin.dao;

import java.io.IOException;

import org.slevin.util.EmlakQueryItem;


public interface AmazonPredictionDao {
	public  String predict(EmlakQueryItem emlakQueryItem,String name) throws Exception;
	public void testCloud() throws IOException;
	public void testCloudFromRDS() throws IOException;
}
