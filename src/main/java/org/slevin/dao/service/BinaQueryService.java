package org.slevin.dao.service;


import java.util.Iterator;
import java.util.List;

import org.slevin.common.BinaQueryItem;
import org.slevin.common.EmlakRawData;
import org.slevin.dao.BinaQueryDao;
import org.slevin.dao.EmlakRawDataDao;
import org.slevin.util.ParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class BinaQueryService extends EntityService<BinaQueryItem> implements BinaQueryDao {

	@Autowired
	EmlakRawDataDao emlakRawDataDao;
	
	
	public long complatedCount() throws Exception {
        return (Long) getEntityManager().createQuery("Select count(t) from " + getEntityClass().getSimpleName() + " t where t.complated=true").getSingleResult();
	}
		
	public List<BinaQueryItem> getUnparsedList() throws Exception {
		return entityManager.createQuery("Select t from " + getEntityClass().getSimpleName() + " t where t.complated=true and t.parsingComplated is null")
	    .setMaxResults(10)
	    .getResultList();
	}
	
	public void updateProcessed(Long id) throws Exception{
		entityManager.createQuery("update " + getEntityClass().getSimpleName() + " set parsingComplated=true where id="+id).executeUpdate();
	}
	
	public void saveRawData(BinaQueryItem item) throws Exception {
		List<EmlakRawData>  list = ParseUtil.parseRawDataForSave(item);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			EmlakRawData emlakRawData = (EmlakRawData) iterator.next();
			if(emlakRawDataDao.findByProperty("ilanNo", emlakRawData.getIlanNo()).size()>0)
				continue;
			else
				emlakRawDataDao.persist(emlakRawData);
		}
	}
}

