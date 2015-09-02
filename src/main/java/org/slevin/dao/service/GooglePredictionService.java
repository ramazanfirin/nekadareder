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

import org.slevin.dao.GooglePredictionDao;
import org.slevin.prime.faces.bean.QueryMB;
import org.slevin.util.Output2;
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
import com.google.api.services.prediction.model.Output;
import com.google.api.services.prediction.model.Update;
import com.google.common.io.Closeables;


@Component
@Transactional
public class GooglePredictionService  implements GooglePredictionDao {

	static String privateKey = "C:/Users/ETR00529/git/predicitivesahibinden/PredictionSahibinden/key.p12";
	 // static String privateKey = "/mnt/ebs1/key.p12";

	  //static final String MODEL_ID = "160087228600";
	  static final String MODEL_ID = "kgsahibindenallv2";
	  static final String PROJECT_NAME = "nth-suprstate-560";
	  static final String APPLICATION_NAME = "cloudStorqge";
	  static final String STORAGE_DATA_LOCATION = "gurkandata/prediction.cvs";
	  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	  private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	  @PostConstruct
	  public void init() throws Exception {
		  Properties prop = new Properties();
		  prop.load(QueryMB.class.getClassLoader().getResourceAsStream("META-INF/spring/database.properties"));
		  privateKey = prop.getProperty("googleKeyFileLocation");
	  }
	  

	public GoogleCredential getCredencial() throws Exception {
		  URL url = GooglePredictionService.class.getResource("key.p12");
		  
		  GoogleCredential credential = new GoogleCredential.Builder()
				    .setTransport(HTTP_TRANSPORT)
					.setJsonFactory(JSON_FACTORY)
					.setServiceAccountId("160087228600-j055cknmb6jbks7ma92bjejl2ig98gdo@developer.gserviceaccount.com")
					.setServiceAccountScopes(Collections.singleton(PredictionScopes.PREDICTION))
					.setServiceAccountPrivateKeyFromP12File(new File(privateKey)).build();
		 
		  return credential;
	}
	@Override
	public void analyze(Prediction prediction) throws IOException {
		Analyze a =prediction.trainedmodels().analyze(PROJECT_NAME,MODEL_ID).execute();
		System.out.println("analiz bitti");
		
	}

	public String makePrediction(Prediction prediction, List<Object> list)	throws IOException {
		 Input input = new Input();
		 InputInput inputInput = new InputInput();
		 inputInput.setCsvInstance(list);
		 input.setInput(inputInput);
		 
		 //Output output = prediction.trainedmodels().predict(PROJECT_NAME,MODEL_ID, input).execute();
		 // String result =output.getOutputValue().toString();
		 String result = makePredictionWorkAround(prediction, input);
		 System.out.println("prediction = "+result);
		 return result;
	}
	
	public String makePredictionWorkAround(Prediction prediction, Input input) throws IOException{
		InputStream is = prediction.trainedmodels().predict(PROJECT_NAME,MODEL_ID, input).executeAsInputStream();
        try {
            Output2 out2 = JSON_FACTORY.fromInputStream(is, Charsets.UTF_8, Output2.class);
            return out2.outputValue.toString();
        } finally {
            Closeables.closeQuietly(is);
        }
	}
	
	
	@Override
	public String train(Prediction prediction) throws IOException {
		// TODO Auto-generated method stub
//		Update update = new Update();
//		update.setCsvInstance(csvInstance)
//		prediction.trainedmodels().update(PROJECT_NAME, MODEL_ID, update);
		return null;
	}
	
	
	public Prediction getPredictionModel() throws Exception{
		 Credential credential = getCredencial();
		 Prediction prediction = new Prediction.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
		 return prediction;
	}
	
	public void logPredictionParameters(List<Object> list){
		System.out.println(MODEL_ID+"-----query parametreleri ----");
		  for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			System.out.println(object);
			
		}
		  System.out.println("-----query parametreleri ----");
	}
	
	@Override
	public String predict(List<Object> list)	throws Exception {
		 Prediction prediction = getPredictionModel();
		 logPredictionParameters(list);
		 return makePrediction(prediction, list);
	}
}

