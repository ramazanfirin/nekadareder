package org.slevin.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.HibernateException;
import org.slevin.common.BinaQueryItem;
import org.slevin.common.Emlak;


public interface EmlakDao extends EntityDao<Emlak> {
	public void  saveAll(List<Emlak> emlakLİst) throws HibernateException, Exception;
	public void  parseBinaQUeryItem(BinaQueryItem binaQueryItem);
	public void  reset();
	
	public Integer getTekrarlanan();
	public Integer getInsertYapilan();
	
	public List<Emlak> findunExportedRecords(int limit) throws HibernateException, Exception;
	
	public List<Emlak> findunMigretedFiyat(int limit) throws HibernateException, Exception;
	
	public List<Emlak> exportByIlce(String name,BigDecimal minimum,BigDecimal maximum) throws HibernateException, Exception;
}
