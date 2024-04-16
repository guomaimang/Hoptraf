package tech.hirsun.hoptraf.task;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tech.hirsun.hoptraf.config.TimeConfig;
import tech.hirsun.hoptraf.service.DriverService;


import java.util.Date;


@Slf4j
@Component
@EnableScheduling
public class ReadTrafRecordsTask {


    @Resource
    private SparkSession sparkSession;

    @Autowired
    private DriverService driverService;

    private Date lastReadTime = TimeConfig.getInitTime();

    @Scheduled(cron ="*/10 * * * * ?")
    public void readRecords() {

        Date cutOffTime = TimeConfig.getCurrentTime();

        // format: 2017-01-01 08:00:00
        String cutOffDatePrint = String.format("%tF %tT", cutOffTime, cutOffTime);
        String lastReadTimePrint = String.format("%tF %tT", lastReadTime, lastReadTime);
        lastReadTime = cutOffTime;

        log.info("lastReadTimePrint: {}, cutOffDatePrint: {}", lastReadTimePrint, cutOffDatePrint);

        String sqlText = "select * from driving where time >= '" + lastReadTimePrint + "' and time < '" + cutOffDatePrint + "'";
        Dataset<Row> result = sparkSession.sql(sqlText);

        driverService.processRecord(result);
    }

}
