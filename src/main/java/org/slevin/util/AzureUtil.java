package org.slevin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

public class AzureUtil {

	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		
		
		String url= "https://ussouthcentral.services.azureml.net/workspaces/4448f01b579546e39b07a730a0774600/services/af85449a300345f1a0ddcb9f205093a5/score";															
	       url ="https://ussouthcentral.services.azureml.net/workspaces/4448f01b579546e39b07a730a0774600/services/af85449a300345f1a0ddcb9f205093a5/execute?api-version=2.0&details=true";
	EmlakQueryItem emlakQueryItem = new EmlakQueryItem();
	emlakQueryItem.setSehir("İstanbul");
	emlakQueryItem.setIlce("Kadıköy");
	emlakQueryItem.setMah("Feneryolu Mah.");
	emlakQueryItem.setKrediyeUygun("true");
	emlakQueryItem.setEmlakTipi("Satılık Daire");
	emlakQueryItem.setYil("2015");
	emlakQueryItem.setM2("161");
	emlakQueryItem.setOdaSayisi("4");
	emlakQueryItem.setBanyoSayisi("2");
	emlakQueryItem.setBinaYasi("0");
	emlakQueryItem.setBinaKatSayisi("13");
	emlakQueryItem.setBulunduguKat("11");
	emlakQueryItem.setIsitma("Doğalgaz (Kombi)");
	emlakQueryItem.setKullanimDurumu("Boş");
	emlakQueryItem.setSiteIcinde("Hayır");
	emlakQueryItem.setKimden("İnşaat Firmasından");
	testPython(emlakQueryItem);
	//parse(url,emlakQueryItem);
}
	
	public static void testPython(EmlakQueryItem emlakQueryItem) throws IOException{
		String pythonData=preparePythonData(emlakQueryItem);
		System.out.println(pythonData);
		Process p = Runtime.getRuntime().exec(pythonData);
		String result = ParseUtil.getStringFromInputStream(p.getInputStream());
		System.out.println(result);
		String a = ParseUtil.getValueFromAzureResponse(result);
		
//		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream(),Charset.forName("UTF-8")));
//		String s = null;
//		while ((s=in.readLine())!=null)
//	    {
//
//	           System.out.println(s);
//	    }
		
		//int ret = new Integer(in.readLine()).intValue();
		//System.out.println("value is : "+ret);
		
		//System.out.println(a);
	}
	
	
	
	
	public static String preparePythonData(EmlakQueryItem emlakQueryItem){
		String data = "C:\\Python34\\python.exe C:\\Users\\ETR00529\\azure2.py";
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
	
	public static String parse(String url,EmlakQueryItem emlak) throws ClientProtocolException, IOException {
	    // Create an instance of HttpClient.
		//String url = "http://www.google.com/search?q=httpClient";
		 
		HttpHost proxy = new HttpHost("localhost", 8888);
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
		
		//CloseableHttpClient client = HttpClientBuilder.create().build();
		CloseableHttpClient client = HttpClients.custom().setRoutePlanner(routePlanner).build();
		
		
		
		
		//HttpGet request = new HttpGet(url);
		HttpPost request = new HttpPost(url);
		
	 
		// add request header
		//request.addHeader("User-Agent","Python-urllib/3.4");
		request.addHeader("Content-Type","application/json");
		request.addHeader("Connection","close");
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
		
		StringEntity paramsEntity =new StringEntity(params);
		
		
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
		
		return result.toString();
		//return d;
	}
}
