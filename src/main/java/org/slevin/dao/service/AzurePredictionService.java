package org.slevin.dao.service;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.slevin.dao.AzurePredictionDao;
import org.slevin.prime.faces.bean.QueryMB;
import org.slevin.util.EmlakQueryItem;
import org.slevin.util.ParseUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class AzurePredictionService  implements AzurePredictionDao {

	static String pythonCommand = "C:/Users/ETR00529/git/predicitivesahibinden/PredictionSahibinden/key.p12";
	String url = "https://ussouthcentral.services.azureml.net/workspaces/4448f01b579546e39b07a730a0774600/services/af85449a300345f1a0ddcb9f205093a5/execute?api-version=2.0&details=true"; 
	
	  @PostConstruct
	  public void init() throws Exception {
		  Properties prop = new Properties();
		  prop.load(QueryMB.class.getClassLoader().getResourceAsStream("META-INF/spring/database.properties"));
		  pythonCommand = prop.getProperty("pythonCommand");
	  }
	  

	@Override
	public String predict(EmlakQueryItem emlakQueryItem) throws Exception {
//		String pythonData=preparePythonData(emlakQueryItem);
//		System.out.println("pythonData="+pythonData );
//		Process p = Runtime.getRuntime().exec(pythonData);
//		String result = ParseUtil.getStringFromInputStream(p.getInputStream());
//		System.out.println("pyhton result="+result);
//		String a = ParseUtil.getValueFromAzureResponse(result);
//		return a;
		String output = predict2(emlakQueryItem);
		String result= ParseUtil.getValueFromAzureResponse(output);
		return result;
	}
	
	
	public String preparePythonData(EmlakQueryItem emlakQueryItem){
		String data = pythonCommand;
		data = data +" \""+emlakQueryItem.getSehir()+"\"";
		data = data +" \""+emlakQueryItem.getIlce()+"\"";
		data = data +" \""+emlakQueryItem.getMah()+"\"";
		data = data +" \""+emlakQueryItem.getKrediyeUygun()+"\"";
		data = data +" \""+emlakQueryItem.getEmlakTipi()+"\"";
		data = data +" \""+emlakQueryItem.getYil()+"\"";
		data = data +" \""+emlakQueryItem.getM2()+"\"";
		data = data +" \""+emlakQueryItem.getOdaSayisi()+"\"";
		data = data +" \""+emlakQueryItem.getBanyoSayisi()+"\"";
		data = data +" \""+emlakQueryItem.getBinaYasi()+"\"";
		data = data +" \""+emlakQueryItem.getBinaKatSayisi()+"\"";
		data = data +" \""+emlakQueryItem.getBulunduguKat()+"\"";
		data = data +" \""+emlakQueryItem.getIsitma()+"\"";
		data = data +" \""+emlakQueryItem.getKullanimDurumu()+"\"";
		data = data +" \""+emlakQueryItem.getSiteIcinde()+"\"";
		data = data +" \""+emlakQueryItem.getKimden()+"\"";
		return data;
	}
	
public String predict2(EmlakQueryItem emlak) throws ClientProtocolException, IOException{
	HttpHost proxy = new HttpHost("localhost", 8888);
	DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
	
	CloseableHttpClient client = HttpClientBuilder.create().build();
	CloseableHttpClient client3 = HttpClients.custom().setRoutePlanner(routePlanner).build();
	

	
	
	//HttpGet request = new HttpGet(url);
	HttpPost request = new HttpPost(url);
	
 
	// add request header
	//request.addHeader("User-Agent","Python-urllib/3.4");
	request.addHeader("Content-Type","application/json;charset=UTF-8");
	request.addHeader("Connection","keep-alive");
	request.addHeader("Accept-Encoding","identity");
	//request.addHeader("Accept","application/json");
	request.addHeader("Authorization","Bearer "+ "grh2aawJgjFBrt0/YmQNHOE9zzbDROE5Ft3RSnG9snjZEZzAYexNESEaIykG+YLdAvx3q+rEolHKP1WufYqy6g==");											

	
	//String  params ="{\"Id\": \"score00001\", \"Instance\": { \"FeatureVector\": { \"fiyat\": \"0\",                                                                                                                                                                                                          \"il\": \"sehirvalue\", \"ilce\": \"ilcevalue\", \"mahalle\": \"mahallevalue\", \"krediyeUygun\": \"krediyeUygunvalue\",\"emlaktipi\": \"emlaktipivalue\", \"yil\": \"yilvalue\", \"m2\": \"m2value\",\"odasayisi\":\"odasayisivalue\",\"banyosayisi\": \"banyosayisivalue\",\"binayasi\": \"binayasivalue\",\"binakatsayisi\": \"binakatsayisivalue\",\"bulundugukat\": \"bulundugukatvalue\",\"isitma\": \"isitmavalue\",\"kullanimdurumu\": \"kullanimdurumuvalue\",\"siteicinde\": \"siteicindevalue\",\"kimden\": \"kimdenvalue\"},\"GlobalParameters\": {\"Append score columns to output\": \"True\"}}}";

	String params = "{\"Inputs\": {\"input1\": {\"ColumnNames\": [\"fiyat\", \"il\", \"ilce\", \"mahalle\", \"krediyeUygun\", \"emlaktipi\", \"yil\", \"m2\", \"odasayisi\", \"banyosayisi\", \"binayasi\", \"binakatsayisi\", \"bulundugukat\", \"isitma\", \"kullanimdurumu\", \"siteicinde\", \"kimden\"], \"Values\": [[\"0\", \"sehirvalue\", \"ilcevalue\", \"mahallevalue\", \"krediyeUygunvalue\", \"emlaktipivalue\", \"yilvalue\", \"m2value\", \"odasayisivalue\", \"banyosayisivalue\", \"binayasivalue\", \"binakatsayisivalue\", \"bulundugukatvalue\", \"isitmavalue\", \"kullanimdurumuvalue\", \"siteicindevalue\", \"kimdenvalue\"]]}}, \"GlobalParameters\": {\"Append score columns to output\": \"True\"}}";
	
	params = params.replace("sehirvalue", emlak.getSehir());
	params = params.replace("ilcevalue", emlak.getIlce());
	params = params.replace("mahallevalue", emlak.getMah());
	params = params.replace("krediyeUygunvalue", "0");
	params = params.replace("emlaktipivalue", emlak.getEmlakTipi());
	params = params.replace("yilvalue", emlak.getYil());
	params = params.replace("m2value", emlak.getM2());
	params = params.replace("odasayisivalue", emlak.getOdaSayisi());
	params = params.replace("banyosayisivalue", emlak.getBanyoSayisi());
	params = params.replace("binayasivalue", emlak.getBinaYasi());
	params = params.replace("binakatsayisivalue", emlak.getBinaKatSayisi());
	params = params.replace("bulundugukatvalue", emlak.getBulunduguKat());
	params = params.replace("isitmavalue", emlak.getIsitma());
	params = params.replace("kullanimdurumuvalue", emlak.getKullanimDurumu());
	params = params.replace("siteicindevalue", emlak.getSiteIcinde());
	params = params.replace("kimdenvalue", emlak.getKimden());
	
	

	String aa ="{\"Inputs\": {\"input1\": {\"ColumnNames\": [\"fiyat\", \"il\", \"ilce\", \"mahalle\", \"krediyeUygun\", \"emlaktipi\", \"yil\", \"m2\", \"odasayisi\", \"banyosayisi\", \"binayasi\", \"binakatsayisi\", \"bulundugukat\", \"isitma\", \"kullanimdurumu\", \"siteicinde\", \"kimden\"], \"Values\": [[\"0\", \"value\", \"value\", \"value\", \"0\", \"value\", \"0\", \"0\", \"0\", \"0\", \"0\", \"0\", \"0\", \"value\", \"value\", \"value\", \"value\"]]}}, \"GlobalParameters\": {\"Append score columns to output\": \"True\"}}";
	StringEntity paramsEntity =new StringEntity(params,"UTF-8");
	paramsEntity.setContentType("application/json");
	
	request.setEntity(paramsEntity);
	
	
	
	HttpResponse response = client.execute(request);
 
	System.out.println("Response Code : " 
                + response.getStatusLine().getStatusCode());
 
	BufferedReader rd = new BufferedReader(
		new InputStreamReader(response.getEntity().getContent(),"UTF8"));
 
	StringBuffer result = new StringBuffer();
	String line = "";
	
	
	
	while ((line = rd.readLine()) != null) {
		result.append(line);
	}
	//System.out.println(result);
	
	byte[] ptext = result.toString().getBytes();
	String d = new String(ptext,"ISO-8859-9");
	//String e = new String(ptext,"UTF_8");
	System.out.println(result.toString());
	return result.toString();

}
}

