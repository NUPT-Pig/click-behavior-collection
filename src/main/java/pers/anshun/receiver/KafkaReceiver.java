package pers.anshun.receiver;

import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import kafka.serializer.StringDecoder;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class KafkaReceiver {
    private static final Logger logger = LoggerFactory.getLogger(KafkaReceiver.class);
    private static String kafkaIp;
    private static String topics;
    private JavaStreamingContext jssc;
    private Map<String, String> kafkaParams;
    private Set<String> topicsSet;

    static {
        Properties props = new Properties();
        InputStream is = Thread.currentThread().getContextClassLoader().
                getResourceAsStream("spark/kafka.properties");
        try {
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        kafkaIp = props.getProperty("kafkaIp");
        topics = props.getProperty("topics");
    }

    public KafkaReceiver(JavaStreamingContext jssc) {
        this.jssc = jssc;
        this.topicsSet = new HashSet<>(Arrays.asList(topics.split(",")));
        this.kafkaParams = new HashMap<>();
        kafkaParams.put("metadata.broker.list", kafkaIp);
        logger.info("init kafka receiver......");
    }

    public JavaDStream<String> GetLogs(){
        logger.info("get logs from kafka......");
        JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(
                jssc,
                String.class,
                String.class,
                StringDecoder.class,
                StringDecoder.class,
                kafkaParams,
                topicsSet
        );
        return messages.map(Tuple2::_2);
    }
}
