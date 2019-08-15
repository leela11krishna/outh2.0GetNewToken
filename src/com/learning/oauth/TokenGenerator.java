package com.learning.oauth;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class TokenGenerator {

	private static String getNewToken() {

		//PASS THE INPUT PARAMETERS HERE
		String tokenUrl = "URL HERE";
		HttpURLConnection httpConnection = null;
		DataOutputStream dos = null;
		StringBuilder responseBuffer = null;
		try {
			
			//PASS THE INPUT PARAMETERS HERE
			StringBuilder sb = new StringBuilder("grant_type=grant type here");
			sb.append("&client_id=").append("client id value here");
			sb.append("&client_secret=").append("client secret key here");
			sb.append("&scope=").append("scope value here");

			// open the connection for mutli-part POST for the first chunk
			httpConnection = (HttpURLConnection) new URL(tokenUrl).openConnection();
			httpConnection.setRequestMethod("POST");
			httpConnection.setConnectTimeout(300000); // 300s = 5mins
			httpConnection.setReadTimeout(300000); // 300s = 5mins

			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConnection.setRequestProperty("Accept", "*/*");

			dos = new DataOutputStream(httpConnection.getOutputStream());
			dos.writeBytes(sb.toString());
			dos.flush();

			responseBuffer = readServerResponse(httpConnection);
			System.out.println("SFDC replied access token: " + responseBuffer.toString());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpConnection != null) {
				httpConnection.disconnect();
			}

			if (dos != null) {
				// close the stream
				try {
					dos.close();
				} catch (IOException e) {
					// NOTHING TO DO
				}
			}
		}
		return responseBuffer != null ? responseBuffer.toString() : null;

	}

	private static StringBuilder readServerResponse(HttpURLConnection httpConnection) {
		StringBuilder response = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(), "utf-8"))) {
			response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	public static void main(String args[]) {
		getNewToken();
	}
}
