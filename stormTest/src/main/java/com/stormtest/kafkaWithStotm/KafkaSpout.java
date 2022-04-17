package com.stormtest.kafkaWithStotm;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class KafkaSpout  extends BaseRichSpout {
    private static final long uuid =124145l;

    private KafkaConsumer<String,String> consumer;

    SpoutOutputCollector spoutOutputCollector;

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.spoutOutputCollector=spoutOutputCollector;

        //配置Kafka配置信息
        Properties properties=new Properties();
        //.....
        // 省略配置信息
        //.....
        this.consumer=new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList("myTopic"));
    }

    @Override
    public void nextTuple() {
        ConsumerRecords<String, String> poll = consumer.poll(10000);
        for (ConsumerRecord<String, String> record : poll) {
            String key = record.key();
            String value = record.value();
            spoutOutputCollector.emit(new Values(key,value));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("key","value"));
    }
}
