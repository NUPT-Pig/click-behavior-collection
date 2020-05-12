package pers.anshun.handler;

import org.apache.spark.streaming.api.java.JavaDStream;

import java.util.Arrays;

public class NginxLogHandler {
    public static void Handler(JavaDStream<String> logs){
        logs.map(x -> x.split(" ")).foreachRDD(rdd -> rdd.foreachPartition(
                x -> {
                    while(x.hasNext()){
                        System.out.println(Arrays.toString(x.next()));
                    }
                }
        ));
    }
}
