package org.slevin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

public class HttpClientUtil {

	public static String parse(String url) throws ClientProtocolException, IOException {
	    // Create an instance of HttpClient.
		//String url = "http://www.google.com/search?q=httpClient";
		 
		HttpHost proxy = new HttpHost("localhost", 8888, "http");
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
		
		CloseableHttpClient client = HttpClientBuilder.create().build();
		//CloseableHttpClient client = HttpClients.custom().setRoutePlanner(routePlanner).build();
		
		HttpGet request = new HttpGet(url);
	 
		// add request header
		request.addHeader("User-Agent", "Sahibinden-Android/2.4.0 (102; Android 5.0; generic Custom Phone - 5.0.0 - API 21 - 768x1280)");
		request.addHeader("x-client-profile","Generic_v1.5");
		request.addHeader("Content-Type","application/json; charset=utf-8");
		request.addHeader("x-api-key","e91092ad5ea2e030c201ce9ac4373f6b565a7842");
		request.addHeader("x-timestamp","1439302586453");
		request.addHeader("x-api-hash","3CDA808BCF62F14A56FD3C3B991EE3262409E835");
		request.addHeader("x-device-id","421cde1c282e4065b5ca932f99c1dc9f");
		request.addHeader("Connection","Keep-Alive");
		request.addHeader("Accept-Encoding","gzip");
		
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
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		String url = "https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_country=1&sorting=bm&language=tr&pagingOffset=0&pagingSize=100";
		String result = parse(url);
		System.out.println(result);
	}
	
}
