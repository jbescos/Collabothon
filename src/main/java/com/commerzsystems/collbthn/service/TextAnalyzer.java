package com.commerzsystems.collbthn.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.ml.AbstractTrainer;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class TextAnalyzer {

	private final Logger logger = LoggerFactory.getLogger(TextAnalyzer.class);
	
	public void trainCategorizer(File trainingFile, File writeFileModel) throws IOException{
		try(OutputStream modelOut = new FileOutputStream(writeFileModel)){
	       ObjectStream<String> lineStream = new PlainTextByLineStream(new MarkableFileInputStreamFactory(trainingFile), "UTF-8");
	       ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);
	       TrainingParameters parameters = new TrainingParameters();
	       parameters.put(AbstractTrainer.CUTOFF_PARAM, 2);
	       parameters.put(AbstractTrainer.ITERATIONS_PARAM, 30);
	       DoccatFactory factory = new DoccatFactory();
	       DoccatModel model = DocumentCategorizerME.train("en", sampleStream, parameters, factory);
	       model.serialize(modelOut);  
		}
	}
	
	public String categorize(File readFileModel, String ... sentences) throws IOException {
		try (InputStream modelIn = new FileInputStream(readFileModel)) {
			DoccatModel model = new DoccatModel(modelIn);
			DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);
			double[] outcomes = myCategorizer.categorize(sentences);
			logger.debug("Result: ", Arrays.asList(outcomes));
			String category = myCategorizer.getBestCategory(outcomes);
			return category;
		}
	}
	
}
