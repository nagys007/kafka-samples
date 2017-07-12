package com.example.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.TimeZone;

public class KafkaProducerSample {

    public static void main (String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "confluent:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("key.serializer", "io.confluent.kafka.serializers.KafkaJsonSerializer");
        props.put("value.serializer", "io.confluent.kafka.serializers.KafkaJsonSerializer");
        props.put("enable.auto.commit", "false");

        props.put("kafka.topic", "com.example.kafka.producer.sample.topic");
        props.put("number.of.events", "10");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);

        String topic = props.getProperty("kafka.topic");

        long numEvents = Long.valueOf(props.getProperty("number.of.events"));

        Random rnd = new Random();
        for (long id = 0; id < numEvents; id++) {
            Date dt = new Date();
            long epochtime = dt.getTime();
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            df.setTimeZone(tz);
            String isodatetime = df.format(new Date());

            String key = "";

            JSONObject json = new JSONObject();
            json.put("id", id);
            json.put("CreatedTimestamp", epochtime);
            json.put("CreatedDatetime", isodatetime);

            String value = json.toString();
            System.out.println(value);

            ProducerRecord<String, String> data = new ProducerRecord<String, String> (
                    topic, key, value );

            producer.send(data);
        }

        producer.close();
    }
}
