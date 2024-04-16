package tech.hirsun.hoptraf.controller;

import jakarta.annotation.Resource;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {


    @Resource
    private SparkSession sparkSession;



    @RequestMapping("/sparksql")
    public String sparkSql() {
        Dataset<Row> result = sparkSession.sql("select count(*) from driving");
        return result.showString(100, 20, false);
    }

    @RequestMapping("/time")
    public String time() {
        Dataset<Row> result = sparkSession.sql("select * from driving where time > '2017-01-01 08:00:10' and time < '2017-01-01 08:00:20'");
        return result.showString(100, 20, false);
    }



}
