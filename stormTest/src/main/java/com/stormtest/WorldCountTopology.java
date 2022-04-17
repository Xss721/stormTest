package com.stormtest;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

public class WorldCountTopology {

    public static void main(String[] args) {
        SentenceSpout sentenceSpout = new SentenceSpout();
        SpitSentenceBolt spitSentenceBolt = new SpitSentenceBolt();
        WordCountBolt wordCountBolt = new WordCountBolt();
        ReportBlot reportBlot = new ReportBlot();
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        //  通过编写的spout 数据源和bolt 数据路径进行构建拓扑图  最小数据传输单元为tuple
        // 类似与mapreduce spout将数据采集发出，流经多个bolt执行不同的业务逻辑，可以对其进行分组 主要方式是对指定字段进行操作进行的。
        topologyBuilder.setSpout("sentence-spout",sentenceSpout,2).setNumTasks(4);

        topologyBuilder.setBolt("spit-blot",spitSentenceBolt,2).setNumTasks(4).shuffleGrouping("sentence-spout");

        topologyBuilder.setBolt("count-blot",wordCountBolt,2).setNumTasks(4).fieldsGrouping("spit-blot",new Fields("word"));

        topologyBuilder.setBolt("report-blot",reportBlot,2).setNumTasks(4).globalGrouping("count-blot");

        Config config =new Config();

        LocalCluster localCluster =new LocalCluster();

        localCluster.submitTopology("word-count-test",config,topologyBuilder.createTopology());
    }
}
