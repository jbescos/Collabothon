package com.commerzsystems.collbthn.service;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GiniServiceTest {

	private final static Logger LOG = LoggerFactory.getLogger(TextAnalyzerTest.class);

	private GiniService undertest;

	@Ignore
	@Test
	public void test() {

		undertest = new GiniService();

		LOG.debug(undertest.loginToGini());

	}

}
