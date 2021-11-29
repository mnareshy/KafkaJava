package com.kafkajava.we;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class IdempotentProducer {

    static Logger logger = LoggerFactory.getLogger(IdempotentProducer.class);

    public static void main(String[] args) {


    for (int i=0 ; i<=20 ; i++){

        sendEvent("topic14","CREATE_"+i, "PayLoad in Jason");
    }


    }

    public static void sendEvent(String topic,  String event, String payload){


        Properties producerProperties = new Properties();
        producerProperties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        producerProperties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProperties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        producerProperties.setProperty("EVENT","Create");


        // These configurations make the producer idempotent / remember safe produser comes with the cost of performance

        producerProperties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,"true");
        producerProperties.setProperty(ProducerConfig.ACKS_CONFIG,"all");
        producerProperties.setProperty(ProducerConfig.RETRIES_CONFIG,Integer.toString(Integer.MAX_VALUE));

        producerProperties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,"5");

        // END - These configurations make the producer idempotent / remember safe produser comes with the cost of performance
        ProducerRecord<String,String> producerRecord = new ProducerRecord<>(topic,event,payload);

        KafkaProducer producer = new KafkaProducer(producerProperties);
        // producer.send(producerRecord);

        producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                logger.info("Topic : "+recordMetadata.topic()+"\n"+
                        "TimeStamp : "+recordMetadata.hasTimestamp()+"\n"+
                        "Partition : "+recordMetadata.partition()+"\n"+
                        "Offset : "+recordMetadata.offset());

            }
        });

        producer.flush();
        producer.close();



    }

}
