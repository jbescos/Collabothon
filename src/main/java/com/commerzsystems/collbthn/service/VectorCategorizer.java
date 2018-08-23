package com.commerzsystems.collbthn.service;

import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VectorCategorizer {

	private final static Logger logger = LoggerFactory.getLogger(VectorCategorizer.class);
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
