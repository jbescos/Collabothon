package com.commerzsystems.collbthn.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

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
	public void categories() throws IOException {
		TextAnalyzer txt = new TextAnalyzer();
		Entry<Double, String> result = txt.categorize(MODEL, "Windows for garden");
		assertEquals("Result: "+result, "mortgage", result.getValue());
		result = txt.categorize(MODEL, "Beers in the pub");
		assertEquals("Result: "+result, "no_mortgage", result.getValue());
	}
	
}
