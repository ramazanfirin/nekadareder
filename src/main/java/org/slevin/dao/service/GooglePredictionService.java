package org.slevin.dao.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
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
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Charsets;
import com.google.api.services.prediction.Prediction;
import com.google.api.services.prediction.PredictionScopes;
import com.google.api.services.prediction.model.Analyze;
import com.google.api.services.prediction.model.Input;
import com.google.api.services.prediction.model.Input.InputInput;
import com.google.api.services.prediction.model.Insert;
import com.google.api.services.prediction.model.Insert2;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.StorageObject;
import com.google.common.io.Closeables;

@Component
@Transactional
public class GooglePredictionService implements GooglePredictionDao {

	static String privateKey = "C:/Users/ETR00529/git/predicitivesahibinden/PredictionSahibinden/key.p12";
	// static String privateKey = "/mnt/ebs1/key.p12";

	// static final String MODEL_ID = "160087228600";
	static final String MODEL_ID = "kgsahibindenallv2";
	static final String MODEL_ID_BEYLIKDUZU = "İstanbul-Beylikdüzü";
	static final String MODEL_ID_BEYLIKDUZU_SEGMENT = "istanbul-Beylikduzu-segment.csv";
	static final String MODEL_ID_BEYLIKDUZU_SEGMENT_BATCH = "testRamazan";
	static final String PROJECT_NAME = "nth-suprstate-560";
	static final String APPLICATION_NAME = "cloudStorqge";
	static final String STORAGE_DATA_LOCATION = "gurkandata/prediction.cvs";
	static final String BUCKET_NAME ="nekadareder";
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	@PostConstruct
	public void init() throws Exception {
		Properties prop = new Properties();
		prop.load(QueryMB.class.getClassLoader().getResourceAsStream(
				"META-INF/spring/database.properties"));
		privateKey = prop.getProperty("googleKeyFileLocation");
	}

	public GoogleCredential getCredencial() throws Exception {
		URL url = GooglePredictionService.class.getResource("key.p12");
		Collection<String> a = new ArrayList<String>();
		a.add(PredictionScopes.PREDICTION);
		a.add(PredictionScopes.DEVSTORAGE_FULL_CONTROL);
		
		GoogleCredential credential = new GoogleCredential.Builder()
				.setTransport(HTTP_TRANSPORT)
				.setJsonFactory(JSON_FACTORY)
				.setServiceAccountId(
						"160087228600-j055cknmb6jbks7ma92bjejl2ig98gdo@developer.gserviceaccount.com")
				.setServiceAccountScopes(a)
				//.setServiceAccountScopes(Collections.singleton(PredictionScopes.PREDICTION))
				.setServiceAccountPrivateKeyFromP12File(new File(privateKey))
				.build();

		return credential;
	}
	
	public Storage getStorage() throws Exception{
		
		Storage storage = new Storage.Builder(HTTP_TRANSPORT, JSON_FACTORY,getCredencial()).setApplicationName("").build();
		return storage;
	}
	@Override
	public void analyze(Prediction prediction) throws IOException {
		Analyze a = prediction.trainedmodels().analyze(PROJECT_NAME, MODEL_ID).execute();
		System.out.println("analiz bitti");

	}
	
	

	public String makePrediction(Prediction prediction, List<Object> list,
			String all) throws IOException {
		Input input = new Input();
		InputInput inputInput = new InputInput();
		inputInput.setCsvInstance(list);
		input.setInput(inputInput);

		// Output output =
		// prediction.trainedmodels().predict(PROJECT_NAME,MODEL_ID,
		// input).execute();
		// String result =output.getOutputValue().toString();
		String result = makePredictionWorkAround(prediction, input, all);
		System.out.println("prediction = " + result);
		return result;
	}

	public String makePredictionWorkAround(Prediction prediction, Input input,
			String all) throws IOException {

		String model = "";
		if (all.equals("all"))
			model = MODEL_ID;
		else if (all.equals("beylikduzu"))
			model = MODEL_ID_BEYLIKDUZU;
		else if (all.equals("beylikduzuSegment"))
			model = MODEL_ID_BEYLIKDUZU_SEGMENT;
		else if (all.equals("beylikduzuSegmentBatch"))
			model = MODEL_ID_BEYLIKDUZU_SEGMENT_BATCH;
		else
			model = all;
		
		InputStream is = prediction.trainedmodels()
				.predict(PROJECT_NAME, model, input).executeAsInputStream();
		try {
			Output2 out2 = JSON_FACTORY.fromInputStream(is, Charsets.UTF_8,
					Output2.class);
			return out2.outputValue.toString();
		} finally {
			Closeables.closeQuietly(is);
		}
	}

	@Override
	public String train(Prediction prediction) throws IOException {
		// prediction.
		return null;
	}

	public Prediction getPredictionModel() throws Exception {
		Credential credential = getCredencial();
		Prediction prediction = new Prediction.Builder(HTTP_TRANSPORT,
				JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
				.build();
		return prediction;
	}

	public void logPredictionParameters(List<Object> list) {
		System.out.println(MODEL_ID + "-----query parametreleri ----");
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			System.out.println(object);

		}
		System.out.println("-----query parametreleri ----");
	}

	@Override
	public String predict(List<Object> list, String all) throws Exception {
		Prediction prediction = getPredictionModel();
		//logPredictionParameters(list);
		return makePrediction(prediction, list, all);
	}
	
	
	
	
	
	
	
	public void insertTrainingModel(String id,String location) throws Exception{
		Prediction prediction = getPredictionModel();
		Insert content = new Insert();
		content.setStorageDataLocation(location);
		content.setId(id);
		Insert2 insert = prediction.trainedmodels().insert(PROJECT_NAME, content).execute();
		//System.out.println("bitti");
	}
	
	public Insert2 getTrainingModel(String name) throws Exception{
		Prediction prediction = getPredictionModel();
		//Get get = 
		Insert2 insert2=prediction.trainedmodels().get(PROJECT_NAME, name).execute();
		//System.out.println("bitti");
		return insert2;
	}
	
	public void deleteTrainingModel(String name) throws Exception{
		Prediction prediction = getPredictionModel();
		prediction.trainedmodels().delete(PROJECT_NAME, name).execute();
	}

	public List<Insert2> listTrainingModel() throws Exception{
		Prediction prediction = getPredictionModel();
		com.google.api.services.prediction.model.List list=prediction.trainedmodels().list(PROJECT_NAME).execute();
		return list.getItems();
	}
	
	
	
	
	
	@Override
	public List<Bucket> listBuckets() throws Exception {
		List<Bucket> buckets = getStorage().buckets().list(PROJECT_NAME).execute().getItems();
		return buckets;
	}

	@Override
	public List<StorageObject> listBucket(String bucketName) throws Exception {
		List<StorageObject> objects = getStorage().objects().list(bucketName).execute().getItems();
		return objects;
	}

	@Override
	public void deleteBucket(String bucketName) throws Exception {
		Storage storage = getStorage();
		
		storage.buckets().delete(bucketName).execute();
		
	}

	@Override
	public void createBucket(String bucketName) throws Exception {
		Storage storage = getStorage();

		Bucket bucket = new Bucket();
		bucket.setName(bucketName);

		storage.buckets().insert(PROJECT_NAME, bucket).execute();
		
	}

	@Override
	public void deleteFile(String bucketName, String fileName) throws Exception {
Storage storage = getStorage();
		
		storage.objects().delete(bucketName, fileName).execute();
		
	}

	@Override
	public void uploadFile(String bucketName, String filePath) throws Exception {
		Storage storage = getStorage();

		StorageObject object = new StorageObject();
		object.setBucket(bucketName);

		File file = new File(filePath);

		InputStream stream = new FileInputStream(file);
		try {
			String contentType = URLConnection
					.guessContentTypeFromStream(stream);
			InputStreamContent content = new InputStreamContent(contentType,
					stream);

			Storage.Objects.Insert insert = storage.objects().insert(
					bucketName, null, content);
			insert.setName(file.getName());

			insert.execute();
		} finally {
			stream.close();
		}
		
	}
	
}
