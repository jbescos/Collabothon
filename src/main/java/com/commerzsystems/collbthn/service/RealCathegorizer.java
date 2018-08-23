package com.commerzsystems.collbthn.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.commerzsystems.collbthn.utils.ZipUtils;
import com.google.common.io.Files;

@Component("realCathegorizer")
public class RealCathegorizer implements ICathegorizer {
	
	private final static Logger log = LoggerFactory.getLogger(RealCathegorizer.class);
	private final static String TRAIN_DATA_1 = "/data/data.zip";
	private final URL zipFileURL = getClass().getResource(TRAIN_DATA_1);
	private VectorCategorizer categorizer;
	
	@Override
	public String categorize(String rawText) {
		if(categorizer == null) {
			synchronized (this) {
				if(categorizer == null) {
					File unzipped = Files.createTempDir();
					unzipped.deleteOnExit();
					try(InputStream inputStream = zipFileURL.openStream()){
						ZipUtils.unzip(inputStream, unzipped);
						ParagraphVectors paragraphVectors = CategorizerModel.createFromFile(unzipped);
						categorizer = new VectorCategorizer(paragraphVectors);
					}catch(IOException e) {
						log.error("Cannot load the model", e);
						throw new RuntimeException("Cannot load the model", e);
					}
				}
			}
		}
		return categorizer.categorize(rawText);
	}

}
