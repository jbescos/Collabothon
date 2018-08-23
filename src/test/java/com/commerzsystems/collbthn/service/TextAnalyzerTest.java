package com.commerzsystems.collbthn.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commerzsystems.collbthn.utils.ZipUtils;
import com.google.common.io.Files;



public class TextAnalyzerTest {
	
	private final static Logger log = LoggerFactory.getLogger(TextAnalyzerTest.class);
	private final static String TRAIN_DATA_1 = "/data/data.zip";
	private final URL zipFileURL = getClass().getResource(TRAIN_DATA_1);
	
	@Test
//	@Ignore
	public void categories() throws IOException {
		File unzipped = Files.createTempDir();
		unzipped.deleteOnExit();
		try(InputStream inputStream = zipFileURL.openStream()){
			ZipUtils.unzip(inputStream, unzipped);
		}
		ParagraphVectors paragraphVectors = CategorizerModel.createFromFile(unzipped);
		VectorCategorizer categorizer = new VectorCategorizer(paragraphVectors);
		checkSentence("mortgage", "Roof Topic", categorizer);
		checkSentence("mortgage", "Building Window", categorizer);
		checkSentence("mortgage", "Window Art Luxi", categorizer);
		checkSentence("mortgage", "Window Art Normi", categorizer);
		checkSentence("no_mortgage", "Malediven June 2018 1 Week", categorizer);
	}
	
	private void checkSentence(String expectedCategory, String sentence, VectorCategorizer categorizer) {
		String category = categorizer.categorize(sentence);
		log.info("Category: {} for sentence: {}", category, sentence);
		assertNotNull(category);
		assertEquals(expectedCategory, category);
	}
	
}
