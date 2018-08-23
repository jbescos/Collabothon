package com.commerzsystems.collbthn.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commerzsystems.collbthn.utils.FileUtils;



public class TextAnalyzerTest {
	
	private final static Logger logger = LoggerFactory.getLogger(TextAnalyzerTest.class);
	private final static File MODEL;

	static{
		try {
			File trainingFile = FileUtils.loadFileFromClasspath("/data/training.txt");
			MODEL = File.createTempFile("training", ".bin");
			new TextAnalyzer().trainCategorizer(trainingFile, MODEL);
		} catch (IOException e) {
			logger.error("Unexpected error", e);
			throw new ExceptionInInitializerError(e);
		}
	}
	
	@Test
	@Ignore
	public void categories() throws IOException {
		TextAnalyzer txt = new TextAnalyzer();
		String result = txt.categorize(MODEL, "Roof Topic");
		assertEquals("Result: "+result, "mortgage", result);
		result = txt.categorize(MODEL, "Malediven June 2018 1 Week");
		assertEquals("Result: "+result, "no_mortgage", result);
		result = txt.categorize(MODEL, "Building Window");
		assertEquals("Result: "+result, "mortgage", result);
		result = txt.categorize(MODEL, "Window Art Luxi");
		assertEquals("Result: "+result, "mortgage", result);
		result = txt.categorize(MODEL, "Window Art Normi");
		assertEquals("Result: "+result, "mortgage", result);
		
	}
	
}
