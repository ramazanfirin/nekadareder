package org.slevin.prime.faces.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

import org.slevin.common.Ilce;
import org.slevin.common.Mahalle;
import org.slevin.common.Sehir;
import org.slevin.dao.AmazonPredictionDao;
import org.slevin.dao.AzurePredictionDao;
import org.slevin.dao.BinaQueryDao;
import org.slevin.dao.EmlakDao;
import org.slevin.dao.GooglePredictionDao;
import org.slevin.dao.IlceDao;
import org.slevin.dao.MahalleDao;
import org.slevin.dao.SahibindenDao;
import org.slevin.dao.SehirDao;
import org.slevin.util.ConvertUtil;
import org.slevin.util.EmlakQueryItem;
import org.slevin.util.HttpClientUtil;
import org.slevin.util.ParseUtil;
import org.slevin.util.SelectItemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value="queryMB")
@ViewScoped
public class QueryMB implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Logger log = Logger.getLogger(QueryMB.class.getName());
	
	@Autowired
	SahibindenDao sahibindenDao;
	
	@Autowired
	BinaQueryDao binaDao;
	
	@Autowired
	EmlakDao emlakDao;

	@Autowired
	SehirDao sehirDao;
	
	@Autowired
	IlceDao ilceDao;
	
	@Autowired
	MahalleDao mahalleDao;
	
	@Autowired
	BinaQueryDao binaQueryDao;

	@Autowired
	GooglePredictionDao googlePredictionDao;
	
	@Autowired
	AmazonPredictionDao amazonPredictionDao;
	
	@Autowired
	AzurePredictionDao azurePredictionDao;
	
	SelectItemUtil selectItemUtil;
	List<Sehir> sehirList;
	List<Ilce> ilceList;
	List<Mahalle> mahalleList;
	List<SelectItem> creditSuitableList;
	List<SelectItem> emlakTipiList;
	List<SelectItem> odaSayisiList;
	List<SelectItem> banyoSayisiList;
	List<SelectItem> binayasiList;
	List<SelectItem> binaKatSayisiList;
	List<SelectItem> bulunduguKatList;
	List<SelectItem> isitmaList;
	List<SelectItem> kullanimDurumuList;
	List<SelectItem> siteIcindeList;
	List<SelectItem> kimdenList;
	
	EmlakQueryItem emlakQueryItem;

	String ilanNo;
	String predictValue;
	
	String amazonPredictValue;
	String azurePredictValue;

	String predictValueBeylikduzu;
	String amazonPredictValueBeylikduzu;
	String azurePredictValueBeylikduzu;
	
	String predictValueBeylikduzuSegment;
	String amazonPredictValueBeylikduzuSegment;
	String azurePredictValueBeylikduzuSegment;
	
	String gifPath;
	

	@PostConstruct
    public void init() throws Exception {
        //FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		selectItemUtil = new SelectItemUtil();
		sehirList = sehirDao.findAll();
		
		creditSuitableList = selectItemUtil.prepareCreditSuitableList();
		emlakTipiList = selectItemUtil.prepareEmlakTipiList();
		odaSayisiList= selectItemUtil.prepareOdaSayisiList();
		banyoSayisiList= selectItemUtil.prepareBanyoSayisiList();
		binayasiList= selectItemUtil.prepareBinayasiList();
		binaKatSayisiList= selectItemUtil.prepareBinaKatSayisiList();
		bulunduguKatList= selectItemUtil.prepareBulunduguKatList();
		isitmaList= selectItemUtil.prepareIsitmaList();
		kullanimDurumuList= selectItemUtil.prepareKullanimDurumuList();
		siteIcindeList= selectItemUtil.prepareSiteIcindeList();
		kimdenList= selectItemUtil.prepareKimdenList();
		
		emlakQueryItem  = new EmlakQueryItem();
		ilceList = new ArrayList<Ilce>();
		mahalleList = new ArrayList<Mahalle>();
		
		predictValue ="";
		ilanNo = "";
		
		
//		ServletContext servletContext = (ServletContext) FacesContext
//			    .getCurrentInstance().getExternalContext().getContext();
//		URL url = servletContext.getResource("/ajax-loader.gif");
//		System.out.println("biit");
	}

	
	public List<SelectItem> prepareSehirList() throws Exception{
		List<Sehir> list= sehirDao.findAll() ;
		return selectItemUtil.prepareSehirList(list);
	}	
	
	 public void onSehirChange() throws Exception {
		 ilceList = ilceDao.findByProperty("sehir.name", emlakQueryItem.getSehir());
		 mahalleList = new ArrayList<Mahalle>();
		 System.out.println("test");
	 }
	
	 public void onIlceChange() throws Exception{
		 mahalleList = mahalleDao.findByProperty("semt.ilce.name", emlakQueryItem.getIlce());
		 System.out.println("test");
	 }
	
	public void getDataFromSahibinden() throws Exception{
		try {
			
			System.out.println("getDataFromSahibinden basladi");
			String url = "https://api.sahibinden.com/sahibinden-ral/rest/classifieds/__parameter__?language=tr";
			url = url.replace("__parameter__", ilanNo);
			String result = HttpClientUtil.parse(url);
			ParseUtil.parseSingleEmlakData(emlakQueryItem,result);
			ilceList = ilceDao.findByProperty("sehir.name", emlakQueryItem.getSehir());
			mahalleList = mahalleDao.findByProperty("semt.ilce.name", emlakQueryItem.getIlce());
			System.out.println(" getDataFromSahibinden bitti");
			addMessage(FacesMessage.SEVERITY_INFO,"İstek Tamamlandi");
			predictValue ="";
			amazonPredictValue ="";
			azurePredictValue="";
			predictValueBeylikduzu ="";
			amazonPredictValueBeylikduzu ="";
			azurePredictValueBeylikduzu="";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("getDataFromSahibinden errro");
			addMessage(FacesMessage.SEVERITY_ERROR,e.getMessage());
			
		}
	}
	
	public void addMessage(Severity severity,String message){
		 FacesMessage messageobject = new FacesMessage(severity,message, message);
		FacesContext.getCurrentInstance().addMessage("form:message", messageobject);
	}
	
	public void predict2() throws Exception{
		prepareEmlakQueryItem();
		Ilce ilce = findIlce();
		String trainingName = ilce.getId()+"_"+ilce.getName() + findSegment();
		predictValue  = googlePredictionDao.predict(ConvertUtil.convertToObjectList(emlakQueryItem),trainingName);
		addMessage(FacesMessage.SEVERITY_INFO,"İstek Tamamlandi");
	}
	
	public Ilce findIlce() throws Exception{
		List<Ilce> ilceList=ilceDao.findByProperty("name",emlakQueryItem.getIlce());
		return ilceList.get(0);

	}
	
	public String findSegment(){
		String segment="";
		BigDecimal fiyat = new BigDecimal(emlakQueryItem.getFiyat());
		if((fiyat.longValue()>10000) && (fiyat.longValue()<=150000))
			segment ="_0.cvs";
		else if((fiyat.longValue()>150000) && (fiyat.longValue()<=350000))
			segment ="_1.cvs";
		else if((fiyat.longValue()>350000) && (fiyat.longValue()<=550000))
			segment ="_2.cvs";
		else if((fiyat.longValue()>550000) && (fiyat.longValue()<=800000))
			segment ="3.cvs";
		else if((fiyat.longValue()>800000) && (fiyat.longValue()<=10000000))
			segment ="_4.cvs";
		else
			segment ="_5.cvs";
		return segment;
	}
	
	public void predict() throws Exception{
		
		//getDataFromSahibinden();
		try {
			prepareEmlakQueryItem();
			predictValue  = googlePredictionDao.predict(ConvertUtil.convertToObjectList(emlakQueryItem),"all");
			amazonPredictValue = amazonPredictionDao.predict(emlakQueryItem,"ML model: Prediction3108_2.csv_all_refactured");//ML model: IstanbulBeylikduzu.cvs/
			try {
				azurePredictValue = azurePredictionDao.predict(emlakQueryItem,"all");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				azurePredictValue = "";
			}
			
			System.out.println("google="+predictValue+",amazon="+amazonPredictValue);
			addMessage(FacesMessage.SEVERITY_INFO,"İstek Tamamlandi");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			addMessage(FacesMessage.SEVERITY_ERROR,e.getMessage());
		}
	}
	
public void predictBeylikduzu() throws Exception{
		
		//getDataFromSahibinden();
		try {
			prepareEmlakQueryItem();
			predictValueBeylikduzu  = googlePredictionDao.predict(ConvertUtil.convertToObjectList(emlakQueryItem),"beylikduzu");
			amazonPredictValueBeylikduzu = amazonPredictionDao.predict(emlakQueryItem,"ML model: IstanbulBeylikduzu.cvs");//ML model: IstanbulBeylikduzu.cvs/
			try {
				azurePredictValueBeylikduzu = azurePredictionDao.predict(emlakQueryItem,"beylikduzu");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				azurePredictValue = "";
			}
			
			System.out.println("google="+predictValue+",amazon="+amazonPredictValue);
			addMessage(FacesMessage.SEVERITY_INFO,"İstek Tamamlandi");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			addMessage(FacesMessage.SEVERITY_ERROR,e.getMessage());
		}
	}
	
public void predictBeylikduzuSegment() throws Exception{
	
	//getDataFromSahibinden();
	try {
		prepareEmlakQueryItem();
		predictValueBeylikduzuSegment  = googlePredictionDao.predict(ConvertUtil.convertToObjectList(emlakQueryItem),"beylikduzuSegment");
		amazonPredictValueBeylikduzuSegment = amazonPredictionDao.predict(emlakQueryItem,"ML model: Istanbul-Beylikduzu-segment.csv");//ML model: IstanbulBeylikduzu.cvs/
		try {
			azurePredictValueBeylikduzuSegment = azurePredictionDao.predict(emlakQueryItem,"beylikduzuSegment");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			azurePredictValue = "";
		}
		
		System.out.println("google="+predictValueBeylikduzuSegment+",amazon="+amazonPredictValueBeylikduzuSegment);
		addMessage(FacesMessage.SEVERITY_INFO,"İstek Tamamlandi");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		addMessage(FacesMessage.SEVERITY_ERROR,e.getMessage());
	}
}

	public void prepareEmlakQueryItem(){
		emlakQueryItem.setOdaSayisi(ConvertUtil.prepareOdaSayisi(emlakQueryItem.getOdaSayisi()));
		emlakQueryItem.setBanyoSayisi(ConvertUtil.prepareBanyoSayisi(emlakQueryItem.getBanyoSayisi()));
		emlakQueryItem.setBinaYasi(ConvertUtil.prepareBinaYasi(emlakQueryItem.getBinaYasi()));
		emlakQueryItem.setBinaKatSayisi(ConvertUtil.prepareBinaKatSayisi(emlakQueryItem.getBinaKatSayisi()));
		emlakQueryItem.setBulunduguKat(ConvertUtil.prepareBulunduguKat(emlakQueryItem.getBulunduguKat()));
	}
	
	
	
	
	
	
	public EmlakQueryItem getEmlakQueryItem() {
		return emlakQueryItem;
	}

	public void setEmlakQueryItem(EmlakQueryItem emlakQueryItem) {
		this.emlakQueryItem = emlakQueryItem;
	}






	public List<SelectItem> getCreditSuitableList() {
		return creditSuitableList;
	}



	public void setCreditSuitableList(List<SelectItem> creditSuitableList) {
		this.creditSuitableList = creditSuitableList;
	}



	public List<SelectItem> getEmlakTipiList() {
		return emlakTipiList;
	}



	public void setEmlakTipiList(List<SelectItem> emlakTipiList) {
		this.emlakTipiList = emlakTipiList;
	}



	public List<SelectItem> getOdaSayisiList() {
		return odaSayisiList;
	}



	public void setOdaSayisiList(List<SelectItem> odaSayisiList) {
		this.odaSayisiList = odaSayisiList;
	}



	public List<SelectItem> getBanyoSayisiList() {
		return banyoSayisiList;
	}



	public void setBanyoSayisiList(List<SelectItem> banyoSayisiList) {
		this.banyoSayisiList = banyoSayisiList;
	}



	public List<SelectItem> getBinayasiList() {
		return binayasiList;
	}



	public void setBinayasiList(List<SelectItem> binayasiList) {
		this.binayasiList = binayasiList;
	}



	public List<SelectItem> getBinaKatSayisiList() {
		return binaKatSayisiList;
	}



	public void setBinaKatSayisiList(List<SelectItem> binaKatSayisiList) {
		this.binaKatSayisiList = binaKatSayisiList;
	}



	public List<SelectItem> getBulunduguKatList() {
		return bulunduguKatList;
	}



	public void setBulunduguKatList(List<SelectItem> bulunduguKatList) {
		this.bulunduguKatList = bulunduguKatList;
	}



	public List<SelectItem> getIsitmaList() {
		return isitmaList;
	}



	public void setIsitmaList(List<SelectItem> isitmaList) {
		this.isitmaList = isitmaList;
	}



	public List<SelectItem> getKullanimDurumuList() {
		return kullanimDurumuList;
	}



	public void setKullanimDurumuList(List<SelectItem> kullanimDurumuList) {
		this.kullanimDurumuList = kullanimDurumuList;
	}



	public List<SelectItem> getSiteIcindeList() {
		return siteIcindeList;
	}



	public void setSiteIcindeList(List<SelectItem> siteIcindeList) {
		this.siteIcindeList = siteIcindeList;
	}



	public List<SelectItem> getKimdenList() {
		return kimdenList;
	}



	public void setKimdenList(List<SelectItem> kimdenList) {
		this.kimdenList = kimdenList;
	}

	public List<Sehir> getSehirList() {
		return sehirList;
	}

	public void setSehirList(List<Sehir> sehirList) {
		this.sehirList = sehirList;
	}

	public List<Ilce> getIlceList() {
		return ilceList;
	}

	public void setIlceList(List<Ilce> ilceList) {
		this.ilceList = ilceList;
	}

	public List<Mahalle> getMahalleList() {
		return mahalleList;
	}

	public void setMahalleList(List<Mahalle> mahalleList) {
		this.mahalleList = mahalleList;
	}

	public String getIlanNo() {
		return ilanNo;
	}

	public void setIlanNo(String ilanNo) {
		this.ilanNo = ilanNo;
	}

	public String getPredictValue() {
		return predictValue;
	}

	public void setPredictValue(String predictValue) {
		this.predictValue = predictValue;
	}

	public String getAmazonPredictValue() {
		return amazonPredictValue;
	}

	public void setAmazonPredictValue(String amazonPredictValue) {
		this.amazonPredictValue = amazonPredictValue;
	}


	public String getGifPath() throws MalformedURLException {
		ServletContext servletContext = (ServletContext) FacesContext
    .getCurrentInstance().getExternalContext().getContext();
		URL url = servletContext.getResource("/ajax-loader.gif");
		gifPath = url.toString();
		return gifPath;
	}


	public void setGifPath(String gifPath) {
		this.gifPath = gifPath;
	}


	public String getAzurePredictValue() {
		return azurePredictValue;
	}


	public void setAzurePredictValue(String azurePredictValue) {
		this.azurePredictValue = azurePredictValue;
	}


	public String getPredictValueBeylikduzu() {
		return predictValueBeylikduzu;
	}


	public void setPredictValueBeylikduzu(String predictValueBeylikduzu) {
		this.predictValueBeylikduzu = predictValueBeylikduzu;
	}


	public String getAmazonPredictValueBeylikduzu() {
		return amazonPredictValueBeylikduzu;
	}


	public void setAmazonPredictValueBeylikduzu(String amazonPredictValueBeylikduzu) {
		this.amazonPredictValueBeylikduzu = amazonPredictValueBeylikduzu;
	}


	public String getAzurePredictValueBeylikduzu() {
		return azurePredictValueBeylikduzu;
	}


	public void setAzurePredictValueBeylikduzu(String azurePredictValueBeylikduzu) {
		this.azurePredictValueBeylikduzu = azurePredictValueBeylikduzu;
	}


	public String getPredictValueBeylikduzuSegment() {
		return predictValueBeylikduzuSegment;
	}


	public void setPredictValueBeylikduzuSegment(
			String predictValueBeylikduzuSegment) {
		this.predictValueBeylikduzuSegment = predictValueBeylikduzuSegment;
	}


	public String getAmazonPredictValueBeylikduzuSegment() {
		return amazonPredictValueBeylikduzuSegment;
	}


	public void setAmazonPredictValueBeylikduzuSegment(
			String amazonPredictValueBeylikduzuSegment) {
		this.amazonPredictValueBeylikduzuSegment = amazonPredictValueBeylikduzuSegment;
	}


	public String getAzurePredictValueBeylikduzuSegment() {
		return azurePredictValueBeylikduzuSegment;
	}


	public void setAzurePredictValueBeylikduzuSegment(
			String azurePredictValueBeylikduzuSegment) {
		this.azurePredictValueBeylikduzuSegment = azurePredictValueBeylikduzuSegment;
	}


}
