package org.slevin.dao;

import java.util.List;

import org.slevin.common.BinaQueryItem;


public interface BinaQueryDao extends EntityDao<BinaQueryItem> {
	public long complatedCount() throws Exception ;
	public List<BinaQueryItem> getUnparsedList() throws Exception;
	public void updateProcessed(Long id) throws Exception;
	public void saveRawData(BinaQueryItem item) throws Exception ;
}

