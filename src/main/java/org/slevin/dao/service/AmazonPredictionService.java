package org.slevin.dao.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slevin.dao.AmazonPredictionDao;
import org.slevin.util.EmlakQueryItem;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.machinelearning.AmazonMachineLearningClient;
import com.amazonaws.services.machinelearning.model.CreateDataSourceFromRDSRequest;
import com.amazonaws.services.machinelearning.model.CreateDataSourceFromS3Request;
import com.amazonaws.services.machinelearning.model.CreateDataSourceFromS3Result;
import com.amazonaws.services.machinelearning.model.CreateEvaluationRequest;
import com.amazonaws.services.machinelearning.model.CreateEvaluationResult;
import com.amazonaws.services.machinelearning.model.CreateMLModelRequest;
import com.amazonaws.services.machinelearning.model.CreateMLModelResult;
import com.amazonaws.services.machinelearning.model.CreateRealtimeEndpointRequest;
import com.amazonaws.services.machinelearning.model.CreateRealtimeEndpointResult;
import com.amazonaws.services.machinelearning.model.DescribeMLModelsResult;
import com.amazonaws.services.machinelearning.model.MLModel;
import com.amazonaws.services.machinelearning.model.MLModelType;
import com.amazonaws.services.machinelearning.model.PredictRequest;
import com.amazonaws.services.machinelearning.model.PredictResult;
import com.amazonaws.services.machinelearning.model.RDSDataSpec;
import com.amazonaws.services.machinelearning.model.RDSDatabase;
import com.amazonaws.services.machinelearning.model.RDSDatabaseCredentials;
import com.amazonaws.services.machinelearning.model.S3DataSpec;

@Component
@Transactional
public class AmazonPredictionService implements AmazonPredictionDao{

	static String ACCESS_KEY_ID="AKIAIZJOELRN4IN5VKKA";
	static String SECRET_ACCESS_KEY="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";
	
	//File schema= new File("C:\\Users\\ETR00529\\Desktop\\sahibinden\\cloudData\\istanbul-Beylikduzu-segment_withheader_schema.schema");
	File schema= new File("C:\\Users\\ETR00529\\Desktop\\sahibinden\\cloudData\\istanbul-Beylikduzu-segment_schema.schema");
	
	File recipe =new File("C:\\Users\\ETR00529\\Desktop\\sahibinden\\analiz\\recipe.json");
	File recipewithoutheader =new File("C:\\Users\\ETR00529\\Desktop\\sahibinden\\analiz\\recipe.json");
	
	
	@Override
	public String predict(EmlakQueryItem emlakQueryItem,String name) throws Exception {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY);
		AmazonMachineLearningClient client = new AmazonMachineLearningClient(awsCreds);
		client.getServiceName();
		client.describeDataSources();
		// Get list of prediction models
		DescribeMLModelsResult models = client.describeMLModels();

//		CreateDataSourceFromRDSRequest createDataSourceFromRDSRequest = new CreateDataSourceFromRDSRequest();
//		RDSDataSpec rDSData = new RDSDataSpec();
//		rDSData.set
//		createDataSourceFromRDSRequest.setRDSData(rDSData);
//		client.createDataSourceFromRDS(createDataSourceFromRDSRequest);

		
		
		//		// Iterate over all models and show basic information about each one
//		for (MLModel m : models.getResults()) {
//			System.out.println("Model name: " + m.getName());
//			System.out.println("Model id: " + m.getMLModelId());
//			System.out.println("Model status: " + m.getStatus());
//			RealtimeEndpointInfo endpoint = m.getEndpointInfo();
//			System.out.println("Endpoint URL: " + endpoint.getEndpointUrl());
//			System.out.println("Endpoint status: "
//					+ endpoint.getEndpointStatus());
//		}

		// Select first model 
		MLModel model =null; 
		for (Iterator iterator = models.getResults().iterator(); iterator.hasNext();) {
			MLModel type = (MLModel) iterator.next();
			if(type.getName().equals(name))
				model =type;
		}		

		// Build a prediction request
		PredictRequest request = new PredictRequest();
		// Select prediction model
		request.setMLModelId(model.getMLModelId());
		// Select realtime endpoint
		request.setPredictEndpoint(model.getEndpointInfo().getEndpointUrl());

		
		// Build data to be predicted
		request
		.addRecordEntry("Var01", emlakQueryItem.getSehir())
		.addRecordEntry("Var02", emlakQueryItem.getIlce())
		.addRecordEntry("Var03", emlakQueryItem.getMah())
		.addRecordEntry("Var04", emlakQueryItem.getKrediyeUygun())
		.addRecordEntry("Var05", emlakQueryItem.getEmlakTipi())
		.addRecordEntry("Var06", emlakQueryItem.getYil())
		.addRecordEntry("Var07", emlakQueryItem.getM2())
		.addRecordEntry("Var08", emlakQueryItem.getOdaSayisi())
		.addRecordEntry("Var09", emlakQueryItem.getBanyoSayisi())
		.addRecordEntry("Var10", emlakQueryItem.getBinaYasi())
		.addRecordEntry("Var11", emlakQueryItem.getBinaKatSayisi())
		.addRecordEntry("Var12", emlakQueryItem.getBulunduguKat())
		.addRecordEntry("Var13", emlakQueryItem.getIsitma())
		.addRecordEntry("Var14", emlakQueryItem.getKullanimDurumu())
		.addRecordEntry("Var15", emlakQueryItem.getSiteIcinde())
		.addRecordEntry("Var16", emlakQueryItem.getKimden());


		System.out.println("Sending prediction request for: "
				+ request.getRecord());

		// Send prediction request
		PredictResult result;
		try {
                        long start = System.currentTimeMillis();
                        result = client.predict(request);
                        long end = System.currentTimeMillis();
                        System.out.println((end - start) + " ms");
		} catch (Exception e) {
			throw new AmazonClientException("Prediction failed", e);
		}

		// Display predicted value
		System.out.println("Predicted value:"
				+ result.getPrediction().getPredictedValue());
		
		return result.getPrediction().getPredictedValue().toString();
	}


	@Override
	public void testCloud() throws IOException {
		
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY);
		AmazonMachineLearningClient client = new AmazonMachineLearningClient(awsCreds);
		String friendlyEntiytName ="8batchtestdatasource";
		
//		String 	traningDatasourceId ="f9eef8f3-bb10-4fb8-b1eb-568a8b5129b7";
//		String 	   testDatasourceId ="7956cfbc-88c1-404f-aeba-5adf2e54b68d";
//		String              modelId ="8fca47ae-269a-407f-b073-5778462065ce";
		
		String traningDatasourceId = createDataSource(client,friendlyEntiytName + " - training data", 0, 70);
		System.out.println("traningDatasourceId="+traningDatasourceId);
		String testDatasourceId = createDataSource(client, friendlyEntiytName + " - testing data", 70, 100);
		System.out.println("testDatasourceId="+testDatasourceId);
		
		String modelId = createModel(client, traningDatasourceId,friendlyEntiytName);
		System.out.println("modelId="+modelId);

		
		String evaluationId = createEvaluation(client, testDatasourceId, friendlyEntiytName, modelId);
			System.out.println("evaluationId="+evaluationId);
		
		createRealtimePrediction(client,modelId);			
		System.out.println("bitti");
	}

	public String createDataSource(AmazonMachineLearningClient client,String datasourceName, int percentBegin, int percentEnd) throws IOException{
		String dataRearrangementString = "{\"splitting\":{\"percentBegin\":"+percentBegin+",\"percentEnd\":"+percentEnd+"}}";
		
		CreateDataSourceFromS3Request createDataSourcetFromS3Request = new CreateDataSourceFromS3Request();
		createDataSourcetFromS3Request.setDataSourceId(UUID.randomUUID().toString());
		createDataSourcetFromS3Request.setDataSourceName(datasourceName);
		
		
		S3DataSpec dataSpec = new S3DataSpec();
		dataSpec.setDataLocationS3("s3://predictivedata/istanbul-Beylikduzu-segment.csv");
		dataSpec.setDataSchema(FileUtils.readFileToString(schema));
		dataSpec.setDataRearrangement(dataRearrangementString);
		createDataSourcetFromS3Request.setDataSpec(dataSpec);
		createDataSourcetFromS3Request.setComputeStatistics(true);
	
		
		CreateDataSourceFromS3Result result= client.createDataSourceFromS3(createDataSourcetFromS3Request);
		System.out.println(result);
		return result.getDataSourceId();
	}
	
	public String createModel(AmazonMachineLearningClient client,String trainDataSourceId,String friendlyEntiytName) throws IOException{
		
		 Map<String, String> parameters = new HashMap<String,String>();
	        parameters.put("sgd.maxPasses", "100");
	        parameters.put("sgd.maxMLModelSizeInBytes", "104857600");  // 100 MiB
	        parameters.put("sgd.l2RegularizationAmount", "1e-6");
	        
	        String mlModelId = UUID.randomUUID().toString();
	        
	        CreateMLModelRequest request = new CreateMLModelRequest()
	            .withMLModelId(UUID.randomUUID().toString())
	            .withMLModelName(friendlyEntiytName + " model")
	            .withMLModelType(MLModelType.REGRESSION)
	            .withParameters(parameters)
	            //.withRecipe(FileUtils.readFileToString(new File("C:\\Users\\ETR00529\\Desktop\\sahibinden\\analiz\\recipe.json")))
	            .withTrainingDataSourceId(trainDataSourceId);
	        CreateMLModelResult result= client.createMLModel(request);
	        System.out.printf("Created ML Model with id %s\n", result.getMLModelId());
	        return result.getMLModelId();
		
		
		
	}
	
	private String createEvaluation(AmazonMachineLearningClient client,String testDataSourceId,String friendlyEntityName,String mlModelId) {
        String evaluationId = UUID.randomUUID().toString();
        // evaluationId = "ev-" + UUID.randomUUID().toString();  // simpler, a bit more ugly
        CreateEvaluationRequest request = new CreateEvaluationRequest()
            .withEvaluationDataSourceId(testDataSourceId)
            .withEvaluationName(friendlyEntityName + " evaluation")
            .withMLModelId(mlModelId);
        CreateEvaluationResult result =client.createEvaluation(request);
        System.out.printf("Created Evaluation with id %s\n", result.getEvaluationId());
        return result.getEvaluationId();
        
    }
	
	private String createRealtimePrediction(AmazonMachineLearningClient client,String modeId){
		CreateRealtimeEndpointRequest endpointRequest = new CreateRealtimeEndpointRequest();
		endpointRequest.setMLModelId(modeId);
		CreateRealtimeEndpointResult  result= client.createRealtimeEndpoint(endpointRequest);
		String endpoint = result.getRealtimeEndpointInfo().getEndpointUrl();
		System.out.println(endpoint);
		return endpoint;
	}


	@Override
	public void testCloudFromRDS() throws IOException {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY);
		AmazonMachineLearningClient client = new AmazonMachineLearningClient(awsCreds);
		
		CreateDataSourceFromRDSRequest createDataSourceFromRDSRequest = new CreateDataSourceFromRDSRequest();
		RDSDataSpec rDSData = new RDSDataSpec();
		
		RDSDatabase databaseInformation = new RDSDatabase();
		databaseInformation.setDatabaseName("cbsIstanbul");
		databaseInformation.setInstanceIdentifier("cbsdata2");
		rDSData.setDatabaseInformation(databaseInformation);
		
		RDSDatabaseCredentials credentials = new RDSDatabaseCredentials();
		credentials.setUsername("ramazan");
		credentials.setPassword("ra5699mo");
		rDSData.setDatabaseCredentials(credentials);
		
		//rDSData.setDataRearrangement(dataRearrangement);
//		rDSData.setDataSchema(dataSchema);
//		rDSData.set
		
		createDataSourceFromRDSRequest.setRDSData(rDSData);
		client.createDataSourceFromRDS(createDataSourceFromRDSRequest);
	}

	

}
