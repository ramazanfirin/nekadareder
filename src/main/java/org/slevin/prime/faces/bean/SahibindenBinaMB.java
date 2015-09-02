package org.slevin.prime.faces.bean;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;

import org.slevin.common.BinaQueryItem;
import org.slevin.common.Emlak;
import org.slevin.common.Sehir;
import org.slevin.dao.BinaQueryDao;
import org.slevin.dao.EmlakDao;
import org.slevin.dao.SahibindenDao;
import org.slevin.dao.SehirDao;
import org.slevin.util.ParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value="sahibindenMB")
@ApplicationScoped
public class SahibindenBinaMB {
	
	@Autowired
	SahibindenDao sahibindenDao;
	
	@Autowired
	BinaQueryDao binaDao;
	
	@Autowired
	EmlakDao emlakDao;

	@Autowired
	SehirDao sehirDao;
	
	long complatedCount;
	
	Long insertYapilan= new Long(0);
	Long tekrarlanan=new Long(0);
	
	String sehirTapulu = "https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_country=1&address_city=__parameterSehir__&sorting=date_desc&a1966=true&language=tr&pagingOffset=__parameter__&pagingSize=15";
	String sehirTapuluASC = "https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_country=1&address_city=__parameterSehir__&sorting=date_asc&a1966=true&language=tr&pagingOffset=__parameter__&pagingSize=15";
	
	String ilceTapulu = "";
	@PostConstruct
    public void init() throws Exception {
		complatedCount  = sahibindenDao.complatedCount();
		
    }
	
	
	public void sehirInsert() throws Exception{
//		sahibindenDao.insertSehir();
//		sahibindenDao.insertIlce();
		sahibindenDao.insertSemt();
		
	}
	
	public void migrateTapulu() throws Exception{
		
		List<Sehir> sehirList = sehirDao.findAll();;
		
		for (Iterator iterator = sehirList.iterator(); iterator.hasNext();) {
			Sehir sehir = (Sehir) iterator.next();
			String url = sehirTapuluASC.replace("__parameterSehir__", sehir.getItemId());
			
			for(int i=0;i<=1000;i=i+15){
				try {
					System.out.println(i+" "+tekrarlanan+" "+ insertYapilan);
					String urlTemp = url.replace("__parameter__", String.valueOf(i));
					BinaQueryItem item = sahibindenDao.getDataForCreditSuitable(urlTemp,true,new Long(i));
					if(item==null)
						continue;
					parseBinaQUeryItem(item);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		
		
		
		
	}
	
	public void migrateTapusuz() throws Exception{
		String url = "https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_country=1&sorting=bm&a1966=false&language=tr&pagingOffset=__parameter__&pagingSize=100";
		for(int i=0;i<=200000;i=i+100){
			String urlTemp = url.replace("__parameter__", String.valueOf(i));
			sahibindenDao.getDataForCreditSuitable(urlTemp,false,new Long(i));
		}
	}
	
	
	public void parseBinaData() throws Exception{
		for(int i=0;i<650;i++){
			List<BinaQueryItem> list=binaDao.getUnparsedList();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				BinaQueryItem binaQueryItem = (BinaQueryItem) iterator.next();
				try {
					List<Emlak> listEmlak = ParseUtil.parseRawData(binaQueryItem);
					
					for (Iterator iterator2 = listEmlak.iterator(); iterator.hasNext();) {
						Emlak emlak = (Emlak) iterator2.next();
						if(emlakDao.findByProperty("ilanNo", emlak.getIlanNo()).size()>0){
							continue;
						}else{
							emlakDao.persist(emlak);
							insertYapilan++;
						}
					}
					
					
					emlakDao.saveAll(listEmlak);
					binaQueryItem.setParsingComplated(true);
					binaDao.updateProcessed(binaQueryItem.getId());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void parseBinaQUeryItem(BinaQueryItem binaQueryItem){
		try {
			List<Emlak> listEmlak = ParseUtil.parseRawData(binaQueryItem);
			for (Iterator iterator = listEmlak.iterator(); iterator.hasNext();) {
				Emlak emlak = (Emlak) iterator.next();
				if(emlakDao.findByProperty("ilanNo", emlak.getIlanNo()).size()>0){
					tekrarlanan++;
					continue;
				}else{
					emlak.setInsertDate(new Date());
					emlakDao.persist(emlak);
					insertYapilan++;
				}
			}
//			binaQueryItem.setParsingComplated(true);
//			binaDao.updateProcessed(binaQueryItem.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SahibindenDao getSahibindenDao() {
		return sahibindenDao;
	}

	public void setSahibindenDao(SahibindenDao sahibindenDao) {
		this.sahibindenDao = sahibindenDao;
	}

	public long getComplatedCount() {
		return complatedCount;
	}

	public void setComplatedCount(long complatedCount) {
		this.complatedCount = complatedCount;
	}

	public Long getInsertYapilan() {
		return insertYapilan;
	}

	public void setInsertYapilan(Long insertYapilan) {
		this.insertYapilan = insertYapilan;
	}

	public Long getTekrarlanan() {
		return tekrarlanan;
	}

	public void setTekrarlanan(Long tekrarlanan) {
		this.tekrarlanan = tekrarlanan;
	}
}
