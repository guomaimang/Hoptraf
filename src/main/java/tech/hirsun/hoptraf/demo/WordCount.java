package tech.hirsun.hoptraf.demo;

import jakarta.annotation.Resource;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Component;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.year;

@Component
public class WordCount {
    @Resource
    private SparkSession sparkSession;

    @Resource
    private JavaSparkContext javaSparkContext;

//    public String testSparkText() {
//        String file = "src/main/resources/word.txt";
//        JavaRDD<String> fileRDD =  javaSparkContext.textFile(file);
//
//        JavaRDD<String> wordsRDD = fileRDD.flatMap(line -> Arrays.asList(line.split(" ")).iterator());
//        JavaPairRDD<String, Integer> wordAndOneRDD = wordsRDD.mapToPair(word -> new Tuple2<>(word, 1));
//        JavaPairRDD<String, Integer> wordAndCountRDD = wordAndOneRDD.reduceByKey((a, b) -> a + b);
//
//        //输出结果
//        List<Tuple2<String, Integer>> result = wordAndCountRDD.collect();
//        result.forEach(System.out::println);
//        return result.toString();
//    }

    public String testSparkText() {
        SparkSession ss = SparkSession.builder().appName("TP SPARK SQL").master("local[*]").getOrCreate();
        Dataset<Row> dframe1 = ss.read().option("header", true).option("inferSchema", true).csv("incidents.csv");
        //dframe1.show();
        //dframe1.printSchema();
        //dframe1.select(col("price").plus(2000)).show();


        //1. Afficher le nombre d’incidents par service.
        dframe1.groupBy("service").count().show();
        //2. Afficher les deux années où il a y avait plus d’incidents.
        dframe1.groupBy(year(col("date")).alias("year")).count().orderBy(col("count").desc()).limit(2).show();
        return null;
    }

}
