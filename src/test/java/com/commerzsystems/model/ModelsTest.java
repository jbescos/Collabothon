package com.commerzsystems.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.junit.Ignore;
import org.junit.Test;

import com.commerzsystems.collbthn.model.CategorizerModel;
import com.commerzsystems.collbthn.model.VectorCategorizer;
import com.commerzsystems.collbthn.utils.ZipUtils;
import com.google.common.io.Files;

public class ModelsTest {
	
	private final static Logger log = LogManager.getLogger();
	private final static String TRAIN_DATA_1 = "/data/labeled.zip";
	private final URL zipFileURL = getClass().getResource(TRAIN_DATA_1);
	private final static String SENTENCE = "This is a test example";
	
	@Test
	@Ignore
	public void createModelTestSaveAndLoadTest() throws IOException {
		File unzipped = Files.createTempDir();
		unzipped.deleteOnExit();
		try(InputStream inputStream = zipFileURL.openStream()){
			ZipUtils.unzip(inputStream, unzipped);
		}
		ParagraphVectors paragraphVectors = CategorizerModel.createFromFile(unzipped);
		VectorCategorizer categorizer = new VectorCategorizer(paragraphVectors);
		String category = categorizer.categorize(SENTENCE);
		log.info("Category: {}", category);
		assertNotNull(category);
		File model = File.createTempFile("model", ".zip");
		model.deleteOnExit();
		CategorizerModel.saveModel(model, paragraphVectors);
		paragraphVectors = CategorizerModel.loadModel(model);
		categorizer = new VectorCategorizer(paragraphVectors);
		String category2 = categorizer.categorize(SENTENCE);
		log.info("Category2: {}", category);
		assertEquals(category, category2);
	}

}