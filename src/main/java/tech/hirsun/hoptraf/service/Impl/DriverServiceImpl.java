package tech.hirsun.hoptraf.service.Impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hirsun.hoptraf.config.TimeConfig;
import tech.hirsun.hoptraf.pojo.Driver;
import tech.hirsun.hoptraf.pojo.DriverBehaviors;
import tech.hirsun.hoptraf.pojo.EventReport;
import tech.hirsun.hoptraf.pojo.TrafRecord;
import tech.hirsun.hoptraf.redis.DriverBehaviorsKey;
import tech.hirsun.hoptraf.redis.DriverKey;
import tech.hirsun.hoptraf.redis.RedisService;
import tech.hirsun.hoptraf.service.DriverService;
import tech.hirsun.hoptraf.service.EventReportService;
import tech.hirsun.hoptraf.service.databean.RegisteredDrivers;
import tech.hirsun.hoptraf.utils.DatasetUtils;

import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RegisteredDrivers registeredDrivers;

    @Autowired
    private EventReportService eventReportService;

    @Resource
    private SparkSession sparkSession;

    @Autowired
    private TimeConfig timeConfig;

    @Override
    public void processRecord(String lastReadTimePrint, String cutOffDatePrint) {

        log.info("lastReadTimePrint: {}, cutOffDatePrint: {}", lastReadTimePrint, cutOffDatePrint);
        String sqlText = "select * from driving where time >= '" + lastReadTimePrint + "' and time < '" + cutOffDatePrint + "'";
        List<TrafRecord> recordList = DatasetUtils.DatasetToPojoList(sparkSession.sql(sqlText), TrafRecord.class);

        for (TrafRecord record : recordList) {

            registeredDrivers.addDriver(record.getDriverID());

            // check if driver exists in redis
            Driver driver = redisService.get(DriverKey.byId, record.getDriverID(), Driver.class);

            // if not exists, create a new driver
            if (driver == null) {
                driver = new Driver();
                driver.setDriverID(record.getDriverID());
                driver.setCarPlateNumber(record.getCarPlateNumber());
            }

            // update driver real-time info
            driver.setUpdateTime(record.getTime());

            driver.setLatitude(record.getLatitude());
            driver.setLongitude(record.getLongitude());

            driver.setSpeed(record.getSpeed());
            driver.setDirection(record.getDirection());

            // update driver behavior info
            if (record.getSiteName() != null){
                redisService.set(DriverBehaviorsKey.attributeById, record.getDriverID() + "_" + "siteName", record.getSiteName());
            }
            if (record.getIsRapidlySpeedup() == 1) {
                redisService.set(DriverBehaviorsKey.attributeById, record.getDriverID() + "_" + "isRapidlySpeedup", "1");
            }
            if (record.getIsRapidlySlowdown() == 1) {
                redisService.set(DriverBehaviorsKey.attributeById, record.getDriverID() + "_" + "isRapidlySlowdown", "1");
            }
            if (record.getIsNeutralSlide() == 1) {
                redisService.set(DriverBehaviorsKey.attributeById, record.getDriverID() + "_" + "isNeutralSlide", "1");
            }
            if (record.getIsOverspeed() == 1) {
                redisService.set(DriverBehaviorsKey.attributeById, record.getDriverID() + "_" + "isOverspeed", "1");
            }
            if (record.getIsFatigueDriving() == 1) {
                redisService.set(DriverBehaviorsKey.attributeById, record.getDriverID() + "_" + "isFatigueDriving", "1");
            }
            if (record.getIsHthrottleStop() == 1) {
                redisService.set(DriverBehaviorsKey.attributeById, record.getDriverID() + "_" + "isHthrottleStop", "1");
            }
            if (record.getIsOilLeak() == 1) {
                redisService.set(DriverBehaviorsKey.attributeById, record.getDriverID() + "_" + "isOilLeak", "1");
            }

            // update history data
            driver.setRapidlySpeedupTimes(record.getIsRapidlySpeedup() == 1 ? driver.getRapidlySpeedupTimes() + 1 : driver.getRapidlySpeedupTimes());
            driver.setRapidlySlowdownTimes(record.getIsRapidlySlowdown() == 1 ? driver.getRapidlySlowdownTimes() + 1 : driver.getRapidlySlowdownTimes());
            driver.setNeutralSlideTimes(record.getIsNeutralSlideFinished() == 1 ? driver.getNeutralSlideTimes() + 1 : driver.getNeutralSlideTimes());
            driver.setOverspeedTimes(record.getIsOverspeedFinished() == 1 ? driver.getOverspeedTimes() + 1 : driver.getOverspeedTimes());
            driver.setFatigueDrivingTimes(record.getIsFatigueDriving() == 1 ? driver.getFatigueDrivingTimes() + 1 : driver.getFatigueDrivingTimes());
            driver.setHthrottleStopTimes(record.getIsHthrottleStop() == 1 ? driver.getHthrottleStopTimes() + 1 : driver.getHthrottleStopTimes());
            driver.setOilLeakTimes(record.getIsOilLeak() == 1 ? driver.getOilLeakTimes() + 1 : driver.getOilLeakTimes());

            // write in redis
            redisService.set(DriverKey.byId, driver.getDriverID(), driver);

            // organize the event report
            EventReport eventReport = new EventReport();
            if (record.getIsRapidlySpeedup() == 1) {
                eventReport.setDriverId(record.getDriverID());
                eventReport.setCarPlateNumber(record.getCarPlateNumber());
                eventReport.setBehavior("The driver rapidly speeds up.");
                eventReport.setReportTime(record.getTime());
                eventReportService.addReport(eventReport);
            }

            if (record.getIsRapidlySlowdown() == 1) {
                eventReport.setDriverId(record.getDriverID());
                eventReport.setCarPlateNumber(record.getCarPlateNumber());
                eventReport.setBehavior("The driver rapidly slows down.");
                eventReport.setReportTime(record.getTime());
                eventReportService.addReport(eventReport);
            }

            if (record.getIsNeutralSlideFinished() == 1) {
                eventReport.setDriverId(record.getDriverID());
                eventReport.setCarPlateNumber(record.getCarPlateNumber());
                eventReport.setBehavior("The driver slides neutrally for " + record.getNeutralSlideTime() + " seconds.");
                eventReport.setReportTime(record.getTime());
                eventReportService.addReport(eventReport);
            }

            if (record.getIsOverspeedFinished() == 1) {
                eventReport.setDriverId(record.getDriverID());
                eventReport.setCarPlateNumber(record.getCarPlateNumber());
                eventReport.setBehavior("The driver overspeeds for " + record.getOverspeedTime() + " seconds.");
                eventReport.setReportTime(record.getTime());
                eventReportService.addReport(eventReport);
            }

            if (record.getIsFatigueDriving() == 1) {
                eventReport.setDriverId(record.getDriverID());
                eventReport.setCarPlateNumber(record.getCarPlateNumber());
                eventReport.setBehavior("The driver is fatigue driving.");
                eventReport.setReportTime(record.getTime());
                eventReportService.addReport(eventReport);
            }

            if (record.getIsHthrottleStop() == 1) {
                eventReport.setDriverId(record.getDriverID());
                eventReport.setCarPlateNumber(record.getCarPlateNumber());
                eventReport.setBehavior("The driver is hthrottle stop.");
                eventReport.setReportTime(record.getTime());
                eventReportService.addReport(eventReport);
            }

            if (record.getIsOilLeak() == 1) {
                eventReport.setDriverId(record.getDriverID());
                eventReport.setCarPlateNumber(record.getCarPlateNumber());
                eventReport.setBehavior("The driver is oil leak.");
                eventReport.setReportTime(record.getTime());
                eventReportService.addReport(eventReport);
            }

            if (record.getSiteName() != null) {
                eventReport.setDriverId(record.getDriverID());
                eventReport.setCarPlateNumber(record.getCarPlateNumber());
                eventReport.setBehavior("The driver is at " + record.getSiteName() + ".");
                eventReport.setReportTime(record.getTime());
                eventReportService.addReport(eventReport);
            }

        }
    }

    @Override
    public Driver getDriverInfo(String driverId) {
        return redisService.get(DriverKey.byId, driverId, Driver.class);
    }

    @Override
    public DriverBehaviors getDriverBehaviors(String driverId) {
        DriverBehaviors driverBehaviors = new DriverBehaviors();

        driverBehaviors.setSiteName(redisService.get(DriverBehaviorsKey.attributeById, driverId + "_" + "siteName", String.class));
        if (redisService.get(DriverBehaviorsKey.attributeById, driverId + "_" + "isRapidlySpeedup", String.class) != null) {
            driverBehaviors.setIsRapidlySpeedup(1);
        }
        if (redisService.get(DriverBehaviorsKey.attributeById, driverId + "_" + "isRapidlySlowdown", String.class) != null) {
            driverBehaviors.setIsRapidlySlowdown(1);
        }
        if (redisService.get(DriverBehaviorsKey.attributeById, driverId + "_" + "isNeutralSlide", String.class) != null) {
            driverBehaviors.setIsNeutralSlide(1);
        }
        if (redisService.get(DriverBehaviorsKey.attributeById, driverId + "_" + "isOverspeed", String.class) != null) {
            driverBehaviors.setIsOverspeed(1);
        }
        if (redisService.get(DriverBehaviorsKey.attributeById, driverId + "_" + "isFatigueDriving", String.class) != null) {
            driverBehaviors.setIsFatigueDriving(1);
        }
        if (redisService.get(DriverBehaviorsKey.attributeById, driverId + "_" + "isHthrottleStop", String.class) != null) {
            driverBehaviors.setIsHthrottleStop(1);
        }
        if (redisService.get(DriverBehaviorsKey.attributeById, driverId + "_" + "isOilLeak", String.class) != null) {
            driverBehaviors.setIsOilLeak(1);
        }
        return driverBehaviors;
    }

    @Override
    public Map getDriverDiagram(String driverId) {
        Date initTime = timeConfig.getInitTime();
        Date cutOffTime = timeConfig.getCurrentTime();
        log.info("getDriverDiagram: {}, initTime: {}, cutOffTime: {}", driverId, initTime, cutOffTime);

        // Calculate the duration of each time slot in seconds
        long durationInSeconds = (cutOffTime.getTime() - initTime.getTime()) / 1000 / 10;

        // Create a Spark session
        SparkSession spark = SparkSession.builder().appName("Java Spark SQL").getOrCreate();

        // Execute the SQL query
        Dataset<Row> df = spark.sql("SELECT window(time, '" + durationInSeconds + " seconds') as time_window, AVG(speed) as avg_speed " +
                "FROM driving " +
                "WHERE driverID = '" + driverId + "' " +
                "AND time >= '" + new Timestamp(initTime.getTime()) + "' " +
                "AND time <= '" + new Timestamp(cutOffTime.getTime()) + "' " +
                "GROUP BY time_window " +
                "ORDER BY time_window");

        // Convert the result to a map
        TreeMap<Timestamp, Double> result = new TreeMap<>();
        for (Row row : df.collectAsList()) {
            Timestamp windowStart = row.getStruct(0).getAs("start");
            Double avgSpeed = row.getDouble(1);
            result.put(windowStart, avgSpeed);
        }

        return result;
    }

    @Override
    public List getDriverList() {
        List<Driver> driverList = new ArrayList<>();
        Set<String> driverIdList = registeredDrivers.getAllDrivers();
        for (String driverId : driverIdList) {
            Driver driver = redisService.get(DriverKey.byId, driverId, Driver.class);
            driverList.add(driver);
        }
        return driverList;
    }



}
