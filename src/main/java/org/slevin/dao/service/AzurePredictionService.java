package org.slevin.dao.service;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slevin.dao.AzurePredictionDao;
import org.slevin.prime.faces.bean.QueryMB;
import org.slevin.util.EmlakQueryItem;
import org.slevin.util.Output2;
import org.slevin.util.ParseUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Charsets;
import com.google.api.services.prediction.Prediction;
import com.google.api.services.prediction.PredictionScopes;
import com.google.api.services.prediction.model.Analyze;
import com.google.api.services.prediction.model.Input;
import com.google.api.services.prediction.model.Input.InputInput;
import com.google.common.io.Closeables;


@Component
@Transactional
public class AzurePredictionService  implements AzurePredictionDao {

	static String pythonCommand = "C:/Users/ETR00529/git/predicitivesahibinden/PredictionSahibinden/key.p12";
	 
	  @PostConstruct
	  public void init() throws Exception {
		  Properties prop = new Properties();
		  prop.load(QueryMB.class.getClassLoader().getResourceAsStream("META-INF/spring/database.properties"));
		  pythonCommand = prop.getProperty("pythonCommand");
	  }
	  

	@Override
	public String predict(EmlakQueryItem emlakQueryItem) throws Exception {
		String pythonData=preparePythonData(emlakQueryItem);
		System.out.println("pythonData="+pythonData );
		Process p = Runtime.getRuntime().exec(pythonData);
		String result = ParseUtil.getStringFromInputStream(p.getInputStream());
		System.out.println("pyhton result="+result);
		String a = ParseUtil.getValueFromAzureResponse(result);
		return a;
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
}

