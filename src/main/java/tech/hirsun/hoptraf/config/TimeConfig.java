package tech.hirsun.hoptraf.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TimeConfig {
    @Getter
    private static final Date initTime = new Date("2017/01/01 08:00:00");

    // calculate the second between lastReadTime and new
    @Getter
    private static final long deltaSeconds = new Date().getTime() - initTime.getTime();

    public static Date getCurrentTime() {
        return new Date(new Date().getTime() - deltaSeconds);
    }

}
