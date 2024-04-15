package tech.hirsun.hoptraf.demo;

import jakarta.annotation.Resource;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class WordCount {
    @Resource
    private SparkSession sparkSession;

    @Resource
    private JavaSparkContext javaSparkContext;

    public String testSparkText() {
        String file = "src/main/resources/word.txt";
        JavaRDD<String> fileRDD =  javaSparkContext.textFile(file);

        JavaRDD<String> wordsRDD = fileRDD.flatMap(line -> Arrays.asList(line.split(" ")).iterator());
        JavaPairRDD<String, Integer> wordAndOneRDD = wordsRDD.mapToPair(word -> new Tuple2<>(word, 1));
        JavaPairRDD<String, Integer> wordAndCountRDD = wordAndOneRDD.reduceByKey((a, b) -> a + b);

        //输出结果
        List<Tuple2<String, Integer>> result = wordAndCountRDD.collect();
        return result.toString();
    }

}
