package com.comtrade.ilserver.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import android.widget.Toast;

public class HelperClass {
	private static final String LOG = "LOG_HELPER_CLASS";
	public static String OUTPUT;
	
	
	public static synchronized String connect(String URL, String username, String password){
		DefaultHttpClient client = new DefaultHttpClient();
		try {
			StringEntity input = new StringEntity("{\"username\":\""+username+"\",\"password\":\""+password+"\"}");
			input.setContentType("application/json");
			HttpPost postRequest = new HttpPost(URL);
			postRequest.setEntity(input);
			HttpResponse response = client.execute(postRequest);
			if (response.getStatusLine().getStatusCode() != 201) {		
				String result = new String();
				result =  "" + response.getStatusLine().getStatusCode();
				Log.d("PROVERA KODA", result);
				return result;
    	//		throw new RuntimeException("Failed : HTTP error code : "
    		//		+ response.getStatusLine().getStatusCode());
    		}
			
			BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));
    		System.out.println("Post method executed .... \n");
    		String result = new String();
    		while ((OUTPUT = br.readLine()) != null) {
    			Log.i(LOG, OUTPUT);
    			result += OUTPUT;
    		}
			
			return result;
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}
	
	public static synchronized String registerUser(String URL, String username, String password, String firstName, String surname){
		DefaultHttpClient client = new DefaultHttpClient();
		try {
			StringEntity input = new StringEntity("{\"firstName\":\""+firstName+"\",\"surname\":\""+surname+"\",\"username\":\""+username+"\",\"password\":\""+password+"\"}");
			input.setContentType("application/json");
			HttpPost postRequest = new HttpPost(URL);
			postRequest.setEntity(input);
			HttpResponse response = client.execute(postRequest);
			Log.d(LOG+"reg", response.toString());
			if (response.getStatusLine().getStatusCode() != 201) {
    			throw new RuntimeException("Failed : HTTP error code : "
    				+ response.getStatusLine().getStatusCode());
    		}
			BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));
    		System.out.println("Post method executed .... \n");
    		String result = null;
    		while ((OUTPUT = br.readLine()) != null) {
    			Log.i(LOG, OUTPUT);
    			result = OUTPUT;
    		}
			return result;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static synchronized String getBeaconByMacAddress(String URL, String username, String password, String macAddress){
		DefaultHttpClient client = new DefaultHttpClient();
		try {
			StringEntity input = new StringEntity("{\"username\":\""+username+"\",\"password\":\""+password+"\",\"macAddress\":\""+macAddress+"\"}");
			input.setContentType("application/json");
			HttpPost postRequest = new HttpPost(URL);
			postRequest.setEntity(input);
			HttpResponse response = client.execute(postRequest);
			if (response.getStatusLine().getStatusCode() != 201) {
    			throw new RuntimeException("Failed : HTTP error code : "
    				+ response.getStatusLine().getStatusCode());
    		}
			BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));
    		System.out.println("Post method executed .... \n");
    		String result = null;
    		while ((OUTPUT = br.readLine()) != null) {
    			Log.i(LOG, OUTPUT);
    			result = OUTPUT;
    		}
			return result;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static synchronized String getSpaceById(String URL, String username, String password, long spaceId){
		DefaultHttpClient client = new DefaultHttpClient();
		try {
			StringEntity input = new StringEntity("{\"username\":\""+username+"\",\"password\":\""+password+"\",\"spaceId\":\""+spaceId+"\"}");
			input.setContentType("application/json");
			HttpPost postRequest = new HttpPost(URL);
			postRequest.setEntity(input);
			HttpResponse response = client.execute(postRequest);
			if (response.getStatusLine().getStatusCode() != 201) {
    			throw new RuntimeException("Failed : HTTP error code : "
    				+ response.getStatusLine().getStatusCode());
    		}
			BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));
    		System.out.println("Post method executed .... \n");
    		String result = null;
    		while ((OUTPUT = br.readLine()) != null) {
    			Log.i(LOG, OUTPUT);
    			result = OUTPUT;
    		}
			return result;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}