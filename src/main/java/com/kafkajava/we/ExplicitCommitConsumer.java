package com.kafkajava.we;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

public class ExplicitCommitConsumer {

    static Logger logger = LoggerFactory.getLogger(ExplicitCommitConsumer.class);
    public static void main(String[] args) {

        receive("SampleApp12", "earliest", Arrays.asList("topic14"));

    }

    public static void receive(String application, String offSetConf, Collection topics){

        Properties consumerProperties = new Properties();
        consumerProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        consumerProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        consumerProperties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,application);
        consumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,offSetConf);

        //disable auto commit of offsets
        consumerProperties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");


        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(consumerProperties);


        consumer.subscribe(topics);

        while (true){

           ConsumerRecords<String,String> consumerRecords =  consumer.poll(Duration.ofMillis(100l));

            for (ConsumerRecord<String,String> consumerRecord: consumerRecords) {

                logger.info("Event -> {} " ,consumerRecord.key());
                logger.info("Partition -> {} " , consumerRecord.partition());
                logger.info("Payload -> {} " , consumerRecord.value());
                logger.info("Headers -> {} " , consumerRecord.headers());
                logger.info("Offset -> {} " , consumerRecord.offset());

                // sync offsets after reading record and doing something
                consumer.commitSync();
            }
        }



    }

}
