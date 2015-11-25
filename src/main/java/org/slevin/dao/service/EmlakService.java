package org.slevin.dao.service;


import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.TemporalType;

import org.hibernate.HibernateException;
import org.slevin.common.BinaQueryItem;
import org.slevin.common.Emlak;
import org.slevin.dao.EmlakDao;
import org.slevin.util.ParseUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class EmlakService extends EntityService<Emlak> implements EmlakDao {
	static Logger log = Logger.getLogger(EmlakService.class.getName());
	
	Integer tekrarlanan= new Integer(0);
	Integer insertYapilan= new Integer(0);
	
	public void  reset(){
		tekrarlanan= new Integer(0);
		insertYapilan= new Integer(0);
	}
	
	public void  saveAll(List<Emlak> emlakLİst) throws HibernateException, Exception{
		for (Iterator iterator = emlakLİst.iterator(); iterator.hasNext();) {
			Emlak emlak = (Emlak) iterator.next();
			if(findByProperty("ilanNo", emlak.getIlanNo()).size()>0)
				continue;
			else
				persist(emlak);
		}
	}
	
	
	public void parseBinaQUeryItem(BinaQueryItem binaQueryItem){
			try {
				List<Emlak> listEmlak = ParseUtil.parseRawData(binaQueryItem);
				for (Iterator iterator = listEmlak.iterator(); iterator.hasNext();) {
					Emlak emlak = (Emlak) iterator.next();
					List<Emlak> currentList = findByProperty("ilanNo", emlak.getIlanNo());
					if(currentList.size()>0){
						for (Iterator iterator2 = currentList.iterator(); iterator2.hasNext();) {
							Emlak emlakTemp = (Emlak) iterator2.next();
							if(emlakTemp.getCurrency()==null || emlakTemp.getCurrency().equals("")){
								emlakTemp.setIsitma(emlak.getIsitma());
								emlakTemp.setCurrency(emlak.getCurrency());
								persist(emlakTemp);
								tekrarlanan++;
							}
						}
						continue;
					}else{
						emlak.setInsertDate(new Date());
						persist(emlak);
						insertYapilan++;
					}
				}
				log.info("insert = "+insertYapilan +" tekrar="+tekrarlanan);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	
	
	public List<Emlak> findunExportedRecords(int limit) throws HibernateException, Exception{
		 return (List<Emlak>) getEntityManager().createQuery("select x from " + getEntityClass().getSimpleName() + " x where x.exportComplated=false and x.ilce=' Beylikduzu' and x.currency like '%TL&' and x.fiyatLong < 350000 and fiyatLong>180000").setMaxResults(limit).getResultList();
	}

	public Integer getTekrarlanan() {
		return tekrarlanan;
	}


	public void setTekrarlanan(Integer tekrarlanan) {
		this.tekrarlanan = tekrarlanan;
	}


	public Integer getInsertYapilan() {
		return insertYapilan;
	}


	public void setInsertYapilan(Integer insertYapilan) {
		this.insertYapilan = insertYapilan;
	}

	@Override
	public List<Emlak> findunMigretedFiyat(int limit)
			throws HibernateException, Exception {
		return (List<Emlak>) getEntityManager().createQuery("select x from " + getEntityClass().getSimpleName() + " x where x.fiyatLong=0 and x.fiyat='1.000001E7'").setMaxResults(limit).getResultList();
	}

	@Override
	public List<Emlak> exportByIlce(String name, java.math.BigDecimal minimum,
			java.math.BigDecimal maximum) throws HibernateException, Exception {
		// TODO Auto-generated method stub
		return (List<Emlak>) getEntityManager().createQuery("select x from " + getEntityClass().getSimpleName() + " x where x.exportComplated=false and x.ilce=?1 and x.currency like '%TL%' and x.fiyatLong>"+minimum+" and fiyatLong<"+maximum).setParameter(1, " "+name).getResultList();
	}
	
	@Override
	public List<Emlak> findAllEmlak(Date startDate,Date endDate) {    
		  List<Emlak> allEvents = entityManager.createQuery("SELECT e FROM Emlak e WHERE e.currency like '%TL%' and e.insertDate BETWEEN :startDate AND :endDate")  
		  .setParameter("startDate", startDate, TemporalType.DATE)  
		  .setParameter("endDate", endDate, TemporalType.DATE)  
		  .getResultList();
		        
		  return allEvents ;  
		    }
}

