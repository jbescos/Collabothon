package com.commerzsystems.collbthn.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("mockCathegorizer")
public class MockCathegorizer implements ICathegorizer {

	private final Map<String, String> MOCK = new HashMap<>();
	
	public MockCathegorizer() {
		MOCK.put("Roof Topic", "mortgage");
		MOCK.put("Building Window", "mortgage");
		MOCK.put("Window Art Luxi", "mortgage");
		MOCK.put("Window Art Normi", "mortgage");
		MOCK.put("Malediven June 2018 1 Week", "no_mortgage");
	}
	
	@Override
	public String categorize(String rawText) {
		if(MOCK.containsKey(rawText)) {
			return MOCK.get(rawText);
		}else {
			return "no_mortgage";
		}
	}

}
