package org.slevin.prime.faces.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;

import org.slevin.common.BinaQueryItem;
import org.slevin.common.Emlak;
import org.slevin.common.Mahalle;
import org.slevin.common.Sehir;
import org.slevin.dao.BinaQueryDao;
import org.slevin.dao.EmlakDao;
import org.slevin.dao.MahalleDao;
import org.slevin.dao.SahibindenDao;
import org.slevin.dao.SehirDao;
import org.slevin.util.ConstantsUtil;
import org.slevin.util.FileUtil;
import org.slevin.util.ParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value="IstanbulMB")
@ApplicationScoped
public class IstanbulSahibindenBinaMB {
	
	static Logger log = Logger.getLogger(IstanbulSahibindenBinaMB.class.getName());
	
	@Autowired
	SahibindenDao sahibindenDao;
	
	@Autowired
	BinaQueryDao binaDao;
	
	@Autowired
	EmlakDao emlakDao;

	@Autowired
	SehirDao sehirDao;
	
	@Autowired
	MahalleDao mahalleDao;
	
	@Autowired
	BinaQueryDao binaQueryDao;
	
	long complatedCount;
	
	Long insertYapilan= new Long(0);
	Long tekrarlanan=new Long(0);
	int artisMiktari=15;
	
	String sehirTapulu = "https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_country=1&address_city=__parameterSehir__&sorting=date_desc&a1966=true&language=tr&pagingOffset=__parameter__&pagingSize=15";
	String sehirTapuluASC = "https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_country=1&address_city=__parameterSehir__&sorting=date_asc&a1966=true&language=tr&pagingOffset=__parameter__&pagingSize=15";
	
	String mahalleTapulu="https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_quarter=__parameterMahalleId__&sorting=date_desc&a1966=true&language=tr&pagingOffset=__parameterPaging__&pagingSize=100"; 
	String mahalleTapuluASC="https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_quarter=__parameterMahalleId__&sorting=date_asc&a1966=true&language=tr&pagingOffset=__parameterPaging__&pagingSize=100"; 
	
	String ilceTapulu = "";
	
	
	
	
	String ilceBasedQuery = "https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_town=__ilceId__&sorting=__sorting__&a1966=__tapuDurumu__language=tr&pagingOffset=__parameterPaging__&pagingSize=__parameterPagingSize__";
	String sehirBasedQuery ="https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_country=1&address_city=__sehirId__&sorting=__sorting__&a1966=__tapuDurumu__language=tr&pagingOffset=__parameterPaging__&pagingSize=__parameterPagingSize__";
	
	@PostConstruct
    public void init() throws Exception {
		complatedCount  = sahibindenDao.complatedCount();
		
    }
	
	public void fiyatMigrate() throws Exception{
		List<Emlak> emlakList = emlakDao.findunMigretedFiyat(10);
		for (Iterator iterator = emlakList.iterator(); iterator.hasNext();) {
			Emlak emlak = (Emlak) iterator.next();
			BigDecimal decimal = new BigDecimal(emlak.getFiyat());
			emlak.setFiyatLong(decimal);
			emlakDao.merge(emlak);
		}
		//emlakDao.saveAll(emlakList);
	}
	
	public void update(){
		insertYapilan = emlakDao.getInsertYapilan().longValue();
		tekrarlanan = emlakDao.getTekrarlanan().longValue();
	}
	public void ilceBazliHepsi() throws Exception{
		log.info("ilce basladi");
		sahibindenDao.daireTapuluIlceUpdate(ConstantsUtil.SORTING_DESC);
		log.info("ilce bitti");
	}
	
	public void testJob() throws Exception{
		sahibindenDao.daireTapuluLast24Hours();
	}
	
	public void tapusuzUpdate() throws Exception{
		sahibindenDao.daireTapusuzUpdate();;
	}
	
	public void exportToFile() throws Exception{
		sahibindenDao.exportToFile();
	}
	
	
	public void migrateBinaQueryItem() throws Exception{
		for (int i = 0; i < 200; i++) {
			List<BinaQueryItem> list=binaQueryDao.findByProperty("complated", false, 100);
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				BinaQueryItem binaQueryItem = (BinaQueryItem) iterator.next();
				binaQueryDao.saveRawData(binaQueryItem);
			}
		}
	}
	public void startMigrate() throws Exception{
		System.out.println("Migrate basladi");
//		sahibindenDao.daireTapuluUpdate(ConstantsUtil.SORTING_ASC);
//		System.out.println("sehir bazli tapulu bitti");
//		sahibindenDao.daireTapuluIlceUpdate(ConstantsUtil.SORTING_ASC);
//		System.out.println("ilce bazli tapulu s bitti");
		migrateTapulu();
		System.out.println("Migrate baitti");
	}
	
	public void migrateTapulu() throws Exception{
		List<Sehir> sehirList = sehirDao.findAll();;
		sehirList = sehirDao.findByProperty("itemId", "34");
		migrate(sehirList);
		sehirList = sehirDao.findByProperty("itemId", "35");
		migrate(sehirList);
		sehirList = sehirDao.findByProperty("itemId", "6");
		migrate(sehirList);
	}
	
	public void migrate(List<Sehir> sehirList) throws Exception{
		
		for (Iterator iterator = sehirList.iterator(); iterator.hasNext();) {
			Sehir sehir = (Sehir) iterator.next();
			List<Mahalle> mahalleList = mahalleDao.findByProperty("semt.ilce.sehir.id", sehir.getId());
			for (Iterator iterator2 = mahalleList.iterator(); iterator2.hasNext();) {
				Mahalle mahalle = (Mahalle) iterator2.next();
				if(mahalle.getComplated()!=null) 
					if(mahalle.getComplated()==true)
						continue;
				
				String urlTemp =mahalleTapulu.replace("__parameterMahalleId__", mahalle.getItemId());
				for(int i=0;i<=1000;i=i+100){
					try {
						System.out.println(i+" "+tekrarlanan+" "+ insertYapilan);
						urlTemp = urlTemp.replace("__parameterPaging__", String.valueOf(i));
						BinaQueryItem item = sahibindenDao.getDataForCreditSuitable(urlTemp,true,new Long(i));
						if(item==null)
							continue;
						emlakDao.parseBinaQUeryItem(item);
						binaDao.saveRawData(item);
						if(i>item.getTotalCount())
							break;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
			    mahalle.setComplated(true);
			    mahalleDao.merge(mahalle);
			
			}
		}
	}
	
//	public void cronJob() throws Exception{
//		List<Sehir> sehirList = sehirDao.findAll();;
//		sehirList = sehirDao.findByProperty("itemId", "34");
//		
//		for (Iterator iterator = sehirList.iterator(); iterator.hasNext();) {
//			Sehir sehir = (Sehir) iterator.next();
//			if(sehir.getItemId().equals("34")||sehir.getItemId().equals("6")||sehir.getItemId().equals("35")){
//				ilceListBasedParse(sehir.getIlceList());
//			}else{
//				sehirBasedParse(sehir,null);
//			}
//		}
//	}
	
	
	
//	public void sehirBasedParse(Sehir sehir,HashMap<String, String> values) throws Exception{
//		values.put("__sehirId__", sehir.getItemId());
//		String queryString = generateQueryString(ilceBasedQuery, values);
//		baseParser(queryString);
//	}
	
	
//	public void ilceListBasedParse(Collection<Ilce> ilceList) throws Exception{
//		for (Iterator iterator2 = ilceList.iterator(); iterator2.hasNext();) {
//				Ilce ilce = (Ilce) iterator2.next();
//				ilceBasedParse(ilce,null);
//		}
//	}
	
//	public void ilceBasedParse(Ilce ilce,HashMap<String, String> values){
//		values.put("__ilceId__", ilce.getItemId());
//		String queryString = generateQueryString(ilceBasedQuery, values);
//		baseParser(queryString);
//	}
	
	
	
	
	
	
	
	
//	public void baseParser(String queryString){
//		for(int i=0;i<=1000;i=i+artisMiktari){
//			try {
//				String urlTemp = queryString.replace("__parameterPaging__", String.valueOf(i));
//				urlTemp = urlTemp.replace("__parameterPagingSize__", String.valueOf(artisMiktari));
//				
//				BinaQueryItem item = sahibindenDao.getDataForCreditSuitable(urlTemp,true,new Long(0));
//				save(item);
//				if(i>item.getTotalCount())
//					break;
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//	}
	
//	public String generateQueryString(String queryString,Map<String,String> values){
//		String resultQueryString  =queryString;
//		for (Iterator iterator = values.keySet().iterator(); iterator.hasNext();) {
//			String key = (String) iterator.next();
//			String value = values.get(key);
//			resultQueryString.replace(key, value);
//		}
//		return resultQueryString;
//	}
	
//	public void save(BinaQueryItem item){
//		if(item==null)
//			return;
//		parseBinaQUeryItem(item);
//		
//	}
	
//	public void migrateTapusuz() throws Exception{
//		String url = "https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_country=1&sorting=bm&a1966=false&language=tr&pagingOffset=__parameter__&pagingSize=100";
//		for(int i=0;i<=200000;i=i+100){
//			String urlTemp = url.replace("__parameter__", String.valueOf(i));
//			sahibindenDao.getDataForCreditSuitable(urlTemp,false,new Long(i));
//		}
//	}
	
	
//	public void parseBinaData() throws Exception{
//		for(int i=0;i<650;i++){
//			List<BinaQueryItem> list=binaDao.getUnparsedList();
//			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
//				BinaQueryItem binaQueryItem = (BinaQueryItem) iterator.next();
//				try {
//					List<Emlak> listEmlak = ParseUtil.parseRawData(binaQueryItem);
//					
//					for (Iterator iterator2 = listEmlak.iterator(); iterator.hasNext();) {
//						Emlak emlak = (Emlak) iterator2.next();
//						if(emlakDao.findByProperty("ilanNo", emlak.getIlanNo()).size()>0){
//							continue;
//						}else{
//							emlakDao.persist(emlak);
//							insertYapilan++;
//						}
//					}
//					
//					
//					emlakDao.saveAll(listEmlak);
//					binaQueryItem.setParsingComplated(true);
//					binaDao.updateProcessed(binaQueryItem.getId());
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//	}
	
	public void parseBinaQUeryItem(BinaQueryItem binaQueryItem){
		try {
			List<Emlak> listEmlak = ParseUtil.parseRawData(binaQueryItem);
			for (Iterator iterator = listEmlak.iterator(); iterator.hasNext();) {
				Emlak emlak = (Emlak) iterator.next();
				if(emlakDao.findByProperty("ilanNo", emlak.getIlanNo()).size()>0){
					//tekrarlanan++;
					continue;
				}else{
					emlak.setInsertDate(new Date());
					emlakDao.persist(emlak);
					//insertYapilan++;
				}
			}
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
