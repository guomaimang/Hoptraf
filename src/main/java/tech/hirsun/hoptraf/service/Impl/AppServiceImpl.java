package tech.hirsun.hoptraf.service.Impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hirsun.hoptraf.config.TimeConfig;
import tech.hirsun.hoptraf.dao.EventReportDao;
import tech.hirsun.hoptraf.redis.RedisService;
import tech.hirsun.hoptraf.service.AppService;
import tech.hirsun.hoptraf.service.databean.RegisteredDrivers;

@Service
public class AppServiceImpl implements AppService {
    @Autowired
    private RedisService redisService;

    @Autowired
    private RegisteredDrivers registeredDrivers;

    @Autowired
    private TimeConfig timeConfig;

    @Autowired
    private EventReportDao eventReportDao;

    @PostConstruct
    @Override
    public void initApp() {
        // reset redis
        redisService.clear();

        // reset mysql
        eventReportDao.deleteAll();
    }

    @Override
    public void resetAll() {
        // reset user registration
        registeredDrivers.removeAllDrivers();

        // reset app time
        timeConfig.resetTime();

        // reset redis
        redisService.clear();

        // reset mysql
        eventReportDao.deleteAll();
    }
}
