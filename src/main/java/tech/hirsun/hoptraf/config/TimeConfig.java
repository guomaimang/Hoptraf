package tech.hirsun.hoptraf.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Date;

@Getter
@Component
public class TimeConfig {
    private final Date initTime = new Date("2017/01/01 08:00:00");

    // calculate the second between lastReadTime and new
    private long deltaSeconds = new Date().getTime() - initTime.getTime();

    public Date getCurrentTime() {
        return new Date(new Date().getTime() - deltaSeconds);
    }

    public void resetTime() {
        deltaSeconds = new Date().getTime() - initTime.getTime();
    }


}
