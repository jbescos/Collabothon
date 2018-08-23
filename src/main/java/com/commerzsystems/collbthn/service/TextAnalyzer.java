package com.commerzsystems.collbthn.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

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
	       parameters.put(AbstractTrainer.CUTOFF_PARAM, AbstractTrainer.CUTOFF_DEFAULT);
	       parameters.put(AbstractTrainer.ITERATIONS_PARAM, AbstractTrainer.ITERATIONS_DEFAULT);
	       DoccatFactory factory = new DoccatFactory();
	       DoccatModel model = DocumentCategorizerME.train("en", sampleStream, parameters, factory);
	       model.serialize(modelOut);  
		}
	}
	
	public Map.Entry<Double,String> categorize(File readFileModel, String ... text) throws IOException {
		try (InputStream modelIn = new FileInputStream(readFileModel)) {
			DoccatModel model = new DoccatModel(modelIn);
			DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);
			SortedMap<Double, Set<String>> map = myCategorizer.sortedScoreMap(text);
			logger.debug(map.toString());
			double probability = map.lastKey();
			String category = map.get(probability).iterator().next();
			Map.Entry<Double,String> pair = new AbstractMap.SimpleImmutableEntry<>(probability, category);
			return pair;
		}
	}
	
}
