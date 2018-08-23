package com.commerzsystems.collbthn.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;

public class VectorCategorizer {

	private final static Logger log = LogManager.getLogger();
	private final ParagraphVectors paragraphVectors;

	public VectorCategorizer(ParagraphVectors paragraphVectors) {
		this.paragraphVectors = paragraphVectors;
	}

	public String categorize(String rawText) {
		if(rawText == null) {
			throw new IllegalArgumentException("Input text is empty");
		}
		return paragraphVectors.predict(rawText);
	}

}
