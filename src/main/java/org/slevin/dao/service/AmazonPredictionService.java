package org.slevin.dao.service;

import java.util.List;

import org.slevin.dao.AmazonPredictionDao;
import org.slevin.util.EmlakQueryItem;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.machinelearning.AmazonMachineLearningClient;
import com.amazonaws.services.machinelearning.model.DescribeMLModelsResult;
import com.amazonaws.services.machinelearning.model.MLModel;
import com.amazonaws.services.machinelearning.model.PredictRequest;
import com.amazonaws.services.machinelearning.model.PredictResult;
import com.amazonaws.services.machinelearning.model.RealtimeEndpointInfo;

@Component
@Transactional
public class AmazonPredictionService implements AmazonPredictionDao{

	static String ACCESS_KEY_ID="AKIAIZJOELRN4IN5VKKA";
	static String SECRET_ACCESS_KEY="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";
	
	
	@Override
	public String predict(EmlakQueryItem emlakQueryItem) throws Exception {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY);
		AmazonMachineLearningClient client = new AmazonMachineLearningClient(awsCreds);
		client.getServiceName();
		client.describeDataSources();
		// Get list of prediction models
		DescribeMLModelsResult models = client.describeMLModels();

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
		MLModel model = models.getResults().get(1);

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


	

}
