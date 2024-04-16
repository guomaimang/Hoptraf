package tech.hirsun.hoptraf.config;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TimeConfig {
    public static final Date initTime = new Date("2017/01/01 08:00:00");

    // calculate the second between lastReadTime and new
    public static final long deltaSeconds = new Date().getTime() - initTime.getTime();

}
