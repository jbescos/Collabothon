package com.commerzsystems.collbthn.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GiniService {

	private String getClientToken() {

		RestTemplate restTemplate = new RestTemplate();
		final String uri = "https://user.gini.net/oauth/token?grant_type=client_credentials";

		// encode password and username
		String userpass = "collabothon_XX:<client_password>";
		String encoding = Base64.encodeBase64String(userpass.getBytes());

		// set headers
		HttpHeaders headers = new HttpHeaders();
		List<MediaType> array = new ArrayList<>();
		array.add(MediaType.APPLICATION_JSON);
		headers.setAccept(array);
		headers.set("Authorization", "Basic " + encoding);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		System.out.println(result.getBody());

		return null;
	}

	private void createGiniUser(String Token) {

	}

	public String loginToGini() {
		createGiniUser(getClientToken());
		return null;
	}

	public String uploadDocumentToGini() {
		return null;
	}

	public String pollExtraction() {
		return null;
	}

}
