package com.example.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

public class KafkaConsumerSample {

    public static void main (String[] args) {

        Properties props = new Properties();
        props.put("bootstrap.servers", "confluent:9092");
        props.put("group.id", "sample-group-id");
        props.put("key.deserializer", "io.confluent.kafka.serializers.KafkaJsonDeserializer");
        props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaJsonDeserializer");

        props.put("kafka.topic", "com.example.kafka.producer.sample.topic");
        props.put("number.of.events", "10");

        Consumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        String topic = props.getProperty("kafka.topic");

        long numEvents = Long.valueOf(props.getProperty("number.of.events"));

        consumer.subscribe(Arrays.asList(topic));

        consumer.poll(10000 );

        List<TopicPartition> partitions = new ArrayList<>();
        for (PartitionInfo partition : consumer.partitionsFor(topic))
            partitions.add(new TopicPartition(topic, partition.partition()));

        consumer.seekToBeginning(partitions);

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records)
                    System.out.println(record.topic() + "-" + record.partition()
                            + "+" + record.offset() + ": " + record.value());
            }
        } finally {
            consumer.close();
        }
    }
}
