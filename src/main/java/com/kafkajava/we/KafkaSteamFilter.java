package com.kafkajava.we;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class KafkaSteamFilter {


    public static void main(String[] args) {

        filterStream();

    }

    public static void filterStream(){

        Properties streamPoperties = new Properties();
        streamPoperties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        streamPoperties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "Steams_Fileter_App12");
        streamPoperties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        streamPoperties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        KStream<String,String> inputTopic =   streamsBuilder.stream("topic27");
        KStream<String,String> filteredStream = inputTopic.filter(


                (key,value) -> evanValues(key,value) == true
        );

        filteredStream.to("topic17");

        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(),streamPoperties);


        kafkaStreams.start();



    }

    private static boolean evanValues(String key,String value) {

        System.out.println("*****************" +value +" "+key);

        Integer payloadNum = Integer.valueOf(key.substring(key.indexOf("_") + 1, key.length()));


        if ((payloadNum % 2) == 0)
         return true;

        return false;
    }

}
