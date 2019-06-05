package com.vish.kafka;

import java.util.Collections;
import java.util.Properties;

import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;

import com.vish.cache.Cache;

public class CacheConsumer implements Runnable {
	
	@Autowired
	Cache cache;
 
	final static Logger logger = Logger.getLogger(CacheConsumer.class);
	private final String KAFKA_BROKERS = "kafkahost:9092";
	private final Integer MESSAGE_COUNT = 1000;
	private final String CLIENT_ID = "cacheclient1";
	private final String TOPIC_NAME = "cache-service";
	private final String GROUP_ID_CONFIG = "consumerGroup1";
	private final Integer MAX_NO_MESSAGE_FOUND_COUNT = 100;
	private final String OFFSET_RESET_LATEST = "latest";
	private final String OFFSET_RESET_EARLIER = "earliest";
	private final Integer MAX_POLL_RECORDS = 1;

	private final String CACHE_UPDATE_NOTIFICATION="CACHE_UPDATED";
	
	public CacheConsumer() {		
		// TODO Auto-generated constructor stub
	}
	/*
	public void init() throws Exception {
		logger.info("Initializing the consumer......");
		new Thread(this).start();
		logger.info("Initializing the consumer......");
	}
    */
	public void cleanUp() throws Exception {
		
	}

	/*
	 * Method used to create kafka consumer
	 */
	public Consumer<Long, String> createConsumer() 
	{
		logger.info("Creating kafka consumer......");
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKERS);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_CONFIG);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OFFSET_RESET_EARLIER);
		Consumer<Long, String> consumer = new KafkaConsumer<Long, String>(props);
		consumer.subscribe(Collections.singletonList(TOPIC_NAME));
		return consumer;
	}

	@Override
	public void run() {
		Consumer<Long, String> consumer = createConsumer();

		while (true) {
			logger.info("polling the records......");
			ConsumerRecords<Long, String> consumerRecords = consumer.poll(5000);

			if (consumerRecords.count() > 0) {
				logger.info("Received kafka notification. Invoking cache reinitialization.....");
				try{
                                	cache.init();
				}catch(Exception e )
				{
					logger.error("Exception while reinitializing th ecache: "+ e.getMessage());
				}
				for (ConsumerRecord<Long, String> record : consumerRecords) {
					logger.info("Received Message with key =%s, message =%s %s\n," + record.key() + "," + record.value());
					/*
					if(record.value.toString().equals(CACHE_UPDATE_NOTIFICATION))
					{
						cache.int();
					}
					*/
				}				
				// commits the offset of record to broker.
				consumer.commitAsync();			}
		}
		// consumer.close();
		}
}
