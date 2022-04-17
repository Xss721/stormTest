package com.stormtest;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.Map;
import java.util.Random;

public class SentenceSpout  extends BaseRichSpout {
    private static final long serialVersionUid =6786786l;
    private SpoutOutputCollector spoutOutputCollector;
    Random random;
    private String[] sentences={"Youdao translation provides instant and free full-text translation, web page translation and document translation services in Chinese, English, Japanese, Korean, French, German, Russian, Spanish, Portuguese, Vietnamese, Indonesian, Italian, Dutch and Thai.",
            "Sogou translation can support the translation function between more than 50 languages such as Chinese, English, French and Japan, and provide you with word, phrase and text translation services immediately and free of charge","" +
            "ICIBA English provides Jinshan iciba, online dictionary, online translation, English learning materials, English songs, online test of English real questions, Chinese word search and other services for the majority of English learning lovers"};

    /**
     * 初始化工作
     * @param map
     * @param topologyContext
     * @param spoutOutputCollector
     */
    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.spoutOutputCollector=spoutOutputCollector;
        random=new Random();
    }

    /**
     * 发射数据的方法
     */
    @Override
    public void nextTuple() {
        Utils.sleep(2000);
        String message =sentences[random.nextInt(sentences.length)];
        this.spoutOutputCollector.emit(new Values(message));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("sentence"));
    }
}
