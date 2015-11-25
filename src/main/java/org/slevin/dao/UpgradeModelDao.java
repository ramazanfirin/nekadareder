package org.slevin.dao;

public interface UpgradeModelDao {

	public void upgrade(String bucketName) throws Exception;
	public void checkUpgradeComplated(String bucketName) throws Exception;
	
}
