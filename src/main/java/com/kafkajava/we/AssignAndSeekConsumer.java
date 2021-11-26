package com.kafkajava.we;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

public class AssignAndSeekConsumer {

    static Logger logger = LoggerFactory.getLogger(AssignAndSeekConsumer.class);
    public static void main(String[] args) {

        receive("SampleApp12", "earliest","topic14",4);

    }

    public static void receive(String partition, String offSetConf, String topic, int readEvents){

        Properties consumerProperties = new Properties();
        consumerProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        consumerProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        consumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,offSetConf);

        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(consumerProperties);

        TopicPartition topicPartition = new TopicPartition(topic,0);

        consumer.assign(Arrays.asList(topicPartition));

        consumer.seek(topicPartition,4);


        while (true){

           ConsumerRecords<String,String> consumerRecords =  consumer.poll(Duration.ofMillis(100l));

            for (ConsumerRecord<String,String> consumerRecord: consumerRecords) {
                if(readEvents == 0)
                    break;

                logger.info("Event -> {} " ,consumerRecord.key());
                logger.info("Partition -> {} " , consumerRecord.partition());
                logger.info("Payload -> {} " , consumerRecord.value());
                logger.info("Headers -> {} " , consumerRecord.headers());
                logger.info("Offset -> {} " , consumerRecord.offset());

                readEvents--;
            }
        }



    }

}
