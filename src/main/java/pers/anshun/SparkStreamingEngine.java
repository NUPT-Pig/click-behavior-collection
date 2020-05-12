package pers.anshun;


import com.google.common.base.Preconditions;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.anshun.handler.NginxLogHandler;
import pers.anshun.receiver.KafkaReceiver;

public class SparkStreamingEngine {
    private static final Logger log = LoggerFactory.getLogger(SparkStreamingEngine.class);
    private JavaStreamingContext jssc;

    private SparkStreamingEngine(String appName) {
        SparkConf sparkConf = new SparkConf().setAppName(appName);
        this.jssc = new JavaStreamingContext(sparkConf, Durations.seconds(20));
    }

    private void StartSpark() {
        KafkaReceiver kafkaReceiver = new KafkaReceiver(jssc);
        JavaDStream<String> nginxLogs = kafkaReceiver.GetLogs();
        NginxLogHandler.Handler(nginxLogs);
        jssc.start();
    }

    private void AwaitSpark() throws InterruptedException {
        Preconditions.checkState(jssc != null);
        log.warn("Spark Streaming is running");
        jssc.awaitTermination();
    }

    private void StopSpark() {
        if (jssc != null) {
            log.warn("Shutting down Spark Streaming; this may take some time");
            jssc.stop(true, true);
            jssc = null;
        } else {
            log.warn("jssc is null");
        }
    }

    public static void main(String[] args){
        SparkStreamingEngine engine = new SparkStreamingEngine("click-behavior-collection");
        try {
            engine.StartSpark();
            engine.AwaitSpark();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            engine.StopSpark();
        }
    }
}
