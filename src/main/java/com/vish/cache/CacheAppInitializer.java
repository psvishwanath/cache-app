package com.vish.cache;

import org.springframework.beans.factory.annotation.Autowired;
import com.vish.kafka.CacheConsumer;
import org.apache.log4j.Logger;

public class CacheAppInitializer {

	@Autowired
	Cache cache;

	@Autowired
	CacheConsumer consumer;

	final static Logger logger = Logger.getLogger(CacheAppInitializer.class);

	private Thread consumerThread;

	public CacheAppInitializer() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * Method initializes the cache from DB and start kafka consumer thread
	 */
	public void init() throws Exception {
		logger.info("Initializing the cache application ");
		
		// Initialize the chache from DB
		cache.init();

		// start the cache kafka consumer thread
		consumerThread = new Thread(consumer);
		consumerThread.start();
		logger.info("Cache initialized successfully.");

	}

	public void cleanUp() throws Exception {
		if (consumerThread != null)
			consumerThread.stop();

	}

}
