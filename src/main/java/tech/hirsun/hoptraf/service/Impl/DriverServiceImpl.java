package tech.hirsun.hoptraf.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hirsun.hoptraf.pojo.Driver;
import tech.hirsun.hoptraf.pojo.TrafRecord;
import tech.hirsun.hoptraf.redis.DriverKey;
import tech.hirsun.hoptraf.redis.RedisService;
import tech.hirsun.hoptraf.service.DriverService;
import tech.hirsun.hoptraf.utils.DatasetUtils;

import java.util.List;

@Slf4j
@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private RedisService redisService;

    @Override
    public void processRecord(Dataset<Row> records) {
        List<TrafRecord> recordList = DatasetUtils.DatasetToPojoList(records, TrafRecord.class);

        // write in redis
        for (TrafRecord record : recordList) {

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
            driver.setSiteName(record.getSiteName());
            driver.setIsRapidlySpeedup(record.getIsRapidlySpeedup());
            driver.setIsRapidlySlowdown(record.getIsRapidlySlowdown());
            driver.setIsNeutralSlide(record.getIsNeutralSlide());
            driver.setIsNeutralSlideFinished(record.getIsNeutralSlideFinished());
            driver.setNeutralSlideTime(record.getNeutralSlideTime());
            driver.setIsOverspeed(record.getIsOverspeed());
            driver.setIsOverspeedFinished(record.getIsOverspeedFinished());
            driver.setOverspeedTime(record.getOverspeedTime());
            driver.setIsFatigueDriving(record.getIsFatigueDriving());
            driver.setIsHthrottleStop(record.getIsHthrottleStop());
            driver.setIsOilLeak(record.getIsOilLeak());

            // update history data
            driver.setRapidlySpeedupTimes(record.getIsRapidlySpeedup() == 1 ? driver.getIsRapidlySpeedup() + 1 : driver.getIsRapidlySpeedup());
            driver.setRapidlySlowdownTimes(record.getIsRapidlySlowdown() == 1 ? driver.getIsRapidlySlowdown() + 1 : driver.getIsRapidlySlowdown());
            driver.setNeutralSlideTimes(record.getIsNeutralSlideFinished() == 1 ? driver.getIsNeutralSlide() + 1 : driver.getIsNeutralSlide());
            driver.setOverspeedTimes(record.getIsOverspeedFinished() == 1 ? driver.getIsOverspeed() + 1 : driver.getIsOverspeed());
            driver.setFatigueDrivingTimes(record.getIsFatigueDriving() == 1 ? driver.getIsFatigueDriving() + 1 : driver.getIsFatigueDriving());
            driver.setHthrottleStopTimes(record.getIsHthrottleStop() == 1 ? driver.getIsHthrottleStop() + 1 : driver.getIsHthrottleStop());
            driver.setOilLeakTimes(record.getIsOilLeak() == 1 ? driver.getIsOilLeak() + 1 : driver.getIsOilLeak());

            // write in redis
            log.info("write in redis: " + driver);
            redisService.set(DriverKey.byId, record.getDriverID(), driver);
        }
    }

    @Override
    public void getUserInfo(String driverId) {

    }

    @Override
    public void getUserDiagram(String driverId) {

    }


}
