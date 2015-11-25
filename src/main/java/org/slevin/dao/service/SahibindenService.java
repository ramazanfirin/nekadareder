package org.slevin.dao.service;


import java.io.File;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.slevin.common.BinaQueryItem;
import org.slevin.common.Emlak;
import org.slevin.common.Ilce;
import org.slevin.common.SahibindenItem;
import org.slevin.common.Sehir;
import org.slevin.common.Semt;
import org.slevin.dao.BinaQueryDao;
import org.slevin.dao.EmlakDao;
import org.slevin.dao.IlceDao;
import org.slevin.dao.SahibindenDao;
import org.slevin.dao.SehirDao;
import org.slevin.util.ConstantsUtil;
import org.slevin.util.FileUtil;
import org.slevin.util.HttpClientUtil;
import org.slevin.util.MapUtil;
import org.slevin.util.ParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;


@Component
public class SahibindenService extends EntityService<SahibindenItem> implements SahibindenDao {

	static Logger log = Logger.getLogger(SahibindenService.class.getName());
	@Autowired
	BinaQueryDao binaQueryDao;
	
	@Autowired
	SehirDao sehirDao;
	
	@Autowired
	IlceDao ilceDao;
	
	@Autowired
	EmlakDao emlakDao;
	
//	@Autowired
//	SahibindenDao sahibindenDao;
	
int artisMiktari = 100;


	
	public long complatedCount() throws Exception {
		return binaQueryDao.complatedCount();
	}
	
	public BinaQueryItem getDataForCreditSuitable(String urlTemp,Boolean creditSuitable,Long itemIndex) throws Exception{
		
			Thread.sleep(1000);
			
//			List<BinaQueryItem> itemList= binaQueryDao.findByProperty("itemIndex", itemIndex);
//			if(itemList.size()>0){
//				System.out.println("found");
//				return;
//			}
				
			
			String result = HttpClientUtil.parse(urlTemp);
			JsonValue value = Json.parse(result);
			JsonObject object  = value.asObject();
			
			Boolean success = object.get("success").asBoolean();
			if(!success){
				System.out.println("fail");
				return null;
			}else{
				System.out.println("success");
			}
			

			JsonObject response = object.get("response").asObject();
			JsonObject totalResult =response.get("paging").asObject();
			
			BinaQueryItem item = new BinaQueryItem();
			item.setUrl(urlTemp);
			item.setContent(result);
			item.setComplated(true);
			item.setCreditSutitable(creditSuitable);
			item.setItemIndex(itemIndex);
			item.setTotalCount(totalResult.get("totalResults").asLong());
			return item;
			//binaQueryDao.persist(item);
	}

	@Override
	public void insertSehir() throws Exception {
		String url = "https://api.sahibinden.com/sahibinden-ral/rest/countries/1/cities?language=tr";
		String result=HttpClientUtil.parse(url);
		List<Sehir> sehirList = ParseUtil.parseSehirList(result);
		sehirDao.saveAll(sehirList);
		
	}

	@Override
	public void insertIlce() throws Exception {
		String url = "https://api.sahibinden.com/sahibinden-ral/rest/cities/_parameter_/towns?language=tr";
		List<Sehir> sehirList = sehirDao.findAll();
		//sehirList = sehirDao.findByProperty("itemId", "34");
		for (Iterator iterator = sehirList.iterator(); iterator.hasNext();) {
			Sehir sehir = (Sehir) iterator.next();
			String urlTemp = url.replace("_parameter_", sehir.getItemId());
			String result=HttpClientUtil.parse(urlTemp);
			List<Ilce> ilceList = ParseUtil.parseIlceList(result);
			sehir.setIlceList(ilceList);
			sehirDao.merge(sehir);
			
		}
		
	}

	@Override
	public void insertSemt() throws Exception {
		String url = "https://api.sahibinden.com/sahibinden-ral/rest/towns/_parameter_/districts?language=tr";
		List<Ilce> ilceList = ilceDao.findAll();
		//ilceList = ilceDao.findByProperty("itemId", "448");
		for (Iterator iterator = ilceList.iterator(); iterator.hasNext();) {
			Ilce ilce = (Ilce) iterator.next();
			String urlTemp = url.replace("_parameter_", ilce.getItemId());
			String result=HttpClientUtil.parse(urlTemp);
			List<Semt> semtList = ParseUtil.parseSemtList(result);
			ilce.setSemtList(semtList);
			ilceDao.merge(ilce);
			
		}
		
	}
	
	
	public void baseParser(String queryString){
		Boolean creditSuitable=true;
		String[] results = queryString.split("&");
		for (int i = 0; i < results.length; i++) {
			String a = results[i];
			if(a.contains("a1966")){
				String[] b = a.split("=");
				if(b[1].equals("true"))
					creditSuitable= true;
				else
					creditSuitable = false;
			}
		}
		for(int i=0;i<=1000;i=i+artisMiktari){
			try {
				String urlTemp = queryString.replace("__parameterPaging__", String.valueOf(i));
				urlTemp = urlTemp.replace("__parameterPagingSize__", String.valueOf(artisMiktari));
				
				BinaQueryItem item = getDataForCreditSuitable(urlTemp,creditSuitable,new Long(0));
				if(item==null)
					return;
				emlakDao.parseBinaQUeryItem(item);
				binaQueryDao.saveRawData(item);
				
				
				if(i>item.getTotalCount())
					break;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public String generateQueryString(String queryString,Map<String,String> values){
		String resultQueryString  =queryString;
		for (Iterator iterator = values.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			String value = values.get(key);
			resultQueryString =resultQueryString.replace(key, value);
		}
		return resultQueryString;
	}
	
	public void sehirBaseParse(HashMap<String, String> values,String query) throws Exception{
		List<Sehir> sehirList = sehirDao.findAll();
		for (Iterator iterator = sehirList.iterator(); iterator.hasNext();) {
			Sehir sehir = (Sehir) iterator.next();
			sehirBasedParse(sehir,values,query);
		}
	}
	
	public void sehirBasedParse(Sehir sehir,HashMap<String, String> values,String query) throws Exception{
		values.put("__sehirId__", sehir.getItemId());
		String queryString = generateQueryString(query, values);
		baseParser(queryString);
	}
	
	public void ilceBasedParse(Collection<Ilce> ilceList,HashMap<String, String> values,String query) throws Exception{
		for (Iterator iterator = ilceList.iterator(); iterator.hasNext();) {
			Ilce ilce = (Ilce) iterator.next();
			if(ilce.getComplated())
				continue;
			values.put("__ilceId__", ilce.getItemId());
			String queryString = generateQueryString(query, values);
			log.info(ilce.getName()+ " basladi");
			baseParser(queryString);
			ilce.setComplated(true);
			ilceDao.merge(ilce);
		}
		
	}
	
	public void ilceBasedParse(Ilce ilce,HashMap<String, String> values){
		values.put("__ilceId__", ilce.getItemId());
		String queryString = generateQueryString(ConstantsUtil.ilceBasedQuery, values);
		baseParser(queryString);
	}
	
	public void daireTapuluLast24Hours() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_DAIRE, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_EVET);
		sehirBaseParse(map,ConstantsUtil.lastDaySehirBasedTapuluQuery);
		
		//Sehir istanbul = sehirDao.findByProperty("itemId", "34").get(0);
		Collection<Ilce> ilceList = ilceDao.findByProperty("sehir.id", new Long(1)) ;
		log.info("son 24 saat, ilce listesi sayisi "+ilceList.size());
		ilceBasedParse(ilceList, map,ConstantsUtil.lastDayIlceBasedTapuluQuery);
	}
	
	public void daireTapuluIlceUpdate(String sorting) throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_DAIRE,sorting, ConstantsUtil.TAPU_DURUMU_EVET);
		Collection<Ilce> ilceList = ilceDao.findAll();
		ilceBasedParse(ilceList,map,ConstantsUtil.ilceBasedQuery);
	}
	
	public void daireTapusuzUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_DAIRE, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_HAYIR);
//		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
		
		//Sehir istanbul = sehirDao.findByProperty("itemId", "34").get(0);
		Collection<Ilce> ilceList = ilceDao.findByProperty("sehir.id", new Long(1)) ;
		//istanbul.getIlceList().size();
		ilceBasedParse(ilceList, map,ConstantsUtil.ilceBasedQuery);
		
		//Sehir ankara = sehirDao.findByProperty("itemId", "6").get(0);
		ilceList = ilceDao.findByProperty("sehir.id", new Long(2)) ;
		ilceBasedParse(ilceList, map,ConstantsUtil.ilceBasedQuery);
		
		//Sehir izmir = sehirDao.findByProperty("itemId", "34").get(0);
		ilceList = ilceDao.findByProperty("sehir.id", new Long(3)) ;
		ilceBasedParse(ilceList, map,ConstantsUtil.ilceBasedQuery);
	}
	
	
	public void daireTapuluUpdate(String sorting) throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_DAIRE, sorting, ConstantsUtil.TAPU_DURUMU_EVET);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
		
		Sehir istanbul = sehirDao.findByProperty("itemId", "34").get(0);
		ilceBasedParse(istanbul.getIlceList(), map,ConstantsUtil.ilceBasedQuery);
	}
	
	
	public void rezidansTapuluUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_RESIDANS, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_EVET);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}
	public void rezidansTapusuzUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_RESIDANS, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_HAYIR);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}
	
	public void mustakilEvTapusuzUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_MUSTAKIL_EV, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_HAYIR);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}
	public void mustakilEvTapuluUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_MUSTAKIL_EV, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_EVET);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}
	
	public void villaTapusuzUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_VILLA, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_HAYIR);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}
	public void villaTapuluUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_VILLA, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_EVET);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}
	
	
	public void ciftlikEviTapusuzUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_CIFTLIK_EVI, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_HAYIR);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}
	public void ciftlikEviTapuluUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_CIFTLIK_EVI, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_EVET);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}
	
	
	public void koskTapusuzUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_KOSK, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_HAYIR);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}
	public void koskTapuluUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_KOSK, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_EVET);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}
	
	
	public void yaliTapusuzUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_YALI, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_HAYIR);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}
	public void yaliTapuluUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_YALI, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_EVET);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}
	
	
	public void yaliDairesiTapusuzUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_YALI_DAIRESI, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_HAYIR);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}
	public void yaliDairesiTapuluUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_YALI_DAIRESI, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_EVET);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}
	
	
	public void yazlikDairesiTapusuzUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_YAZLIK, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_HAYIR);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}
	public void yazlikDairesiTapuluUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_YAZLIK, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_EVET);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}
	
	public void prefabrikTapusuzUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_PREFABRIK_EV, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_HAYIR);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}
	public void prefabrikTapuluUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_PREFABRIK_EV, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_EVET);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}
	
	public void kooperatifTapusuzUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_KOOPERATIF, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_HAYIR);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}
	public void kooperatifTapuluUpdate() throws Exception{
		HashMap<String,String> map = MapUtil.getQueryMap(ConstantsUtil.CATEGORY_KOOPERATIF, ConstantsUtil.SORTING_DESC, ConstantsUtil.TAPU_DURUMU_EVET);
		sehirBaseParse(map,ConstantsUtil.sehirBasedQuery);
	}

	public void exportToFile() throws Exception{
			FileUtil.resetFile();
		Date startDate = new Date();
		for (int i = 0; i < 50; i++) {
			
		
		List<Emlak> emlakList = emlakDao.findunExportedRecords(10000);
			for (Iterator iterator = emlakList.iterator(); iterator.hasNext();) {
				Emlak emlak = (Emlak) iterator.next();
				try {
					FileUtil.appendToFile(emlak);
					emlak.setExportComplated(true);
					emlakDao.merge(emlak);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Date endDate = new Date();
			long duration = endDate.getTime() -startDate.getTime();
			System.out.println("bitti "+ duration + " ms");
		}
		
	}	
	public void testList(List<Emlak> emlakList){
		for (Iterator iterator = emlakList.iterator(); iterator.hasNext();) {
			Emlak emlak = (Emlak) iterator.next();
			if(emlak.getFiyatLong().longValue()<150000 || emlak.getFiyatLong().longValue()>350000){
				System.out.println("dikkat");
			}
		}
	}
	
	
	public void exportToFileByIlce(String directoryPath) throws Exception{
		//FileUtil.resetFile();
		Date startDate = new Date();
		List<Emlak> emlakList ;
		List<Ilce> ilceList = ilceDao.findByProperty("sehir.name", "İstanbul");
		ilceList = ilceDao.findAll();
		for (Iterator iterator = ilceList.iterator(); iterator.hasNext();) {
			Ilce ilce = (Ilce) iterator.next();

			emlakList = emlakDao.exportByIlce(ilce.getName(), new BigDecimal(10000),new BigDecimal(150000));
			writeToFile(emlakList, ilce.getId()+"_"+ilce.getName() + "_0.cvs",directoryPath);
			
			emlakList = emlakDao.exportByIlce(ilce.getName(), new BigDecimal(150000),new BigDecimal(350000));
			writeToFile(emlakList, ilce.getId()+"_"+ilce.getName() + "_1.cvs",directoryPath);
			
			emlakList = emlakDao.exportByIlce(ilce.getName(), new BigDecimal(350000),new BigDecimal(550000));
			writeToFile(emlakList, ilce.getId()+"_"+ilce.getName() + "_2.cvs",directoryPath);
			
			emlakList = emlakDao.exportByIlce(ilce.getName(), new BigDecimal(550000),new BigDecimal(800000));
			writeToFile(emlakList, ilce.getId()+"_"+ilce.getName() + "_3.cvs",directoryPath);
			
			emlakList = emlakDao.exportByIlce(ilce.getName(), new BigDecimal(800000),new BigDecimal(10000000));
			writeToFile(emlakList, ilce.getId()+"_"+ilce.getName() + "_4.cvs",directoryPath);
			
			emlakList = emlakDao.exportByIlce(ilce.getName(), new BigDecimal(10000),new BigDecimal(10000000));
			writeToFile(emlakList, ilce.getId()+"_"+ilce.getName() + "_5.cvs",directoryPath);
			
//			emlakList = emlakDao.exportByIlce(ilce.getName(), 0,100);
//			writeToFile(emlakList, ilce.getName() + "_1");
//			
			
			Date endDate = new Date();
			long duration = endDate.getTime() - startDate.getTime();
			System.out.println("bitti " + duration + " ms");
		}
	
	
}	

	
	public void writeToFile(List<Emlak> emlakList,String fileName,String directoryPath){
		File file =new File(directoryPath+ File.separator+fileName);
		for (int i = 0; i < 10; i++) {
			
		
		for (Iterator iterator = emlakList.iterator(); iterator.hasNext();) {
			Emlak emlak = (Emlak) iterator.next();
			try {
				
				FileUtil.appendToFile(emlak,file);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
	}
	
	
	
}

