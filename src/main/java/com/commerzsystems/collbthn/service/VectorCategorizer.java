package com.commerzsystems.collbthn.service;

import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

public class VectorCategorizer implements ICathegorizer{

	private final static Logger logger = LoggerFactory.getLogger(VectorCategorizer.class);
	private final ParagraphVectors paragraphVectors;

	public VectorCategorizer(ParagraphVectors paragraphVectors) {
		this.paragraphVectors = paragraphVectors;
	}

	@Override
	public String categorize(String rawText) {
		if(rawText == null) {
			throw new IllegalArgumentException("Input text is empty");
		}
		return paragraphVectors.predict(rawText);
	}

}
