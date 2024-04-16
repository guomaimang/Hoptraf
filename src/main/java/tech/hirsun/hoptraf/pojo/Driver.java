package tech.hirsun.hoptraf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Driver {
    // User Info
    private String driverID;
    private String carPlateNumber;

    // Update Time
    private Date updateTime;

    // Real-time data
    private double latitude;
    private double longitude;
    private int speed;

    private int direction;
    private String siteName;

    private int isRapidlySpeedup;
    private int isRapidlySlowdown;
    private int isNeutralSlide;
    private int isNeutralSlideFinished;
    private int neutralSlideTime;
    private int isOverspeed;
    private int isOverspeedFinished;
    private int overspeedTime;
    private int isFatigueDriving;
    private int isHthrottleStop;
    private int isOilLeak;

    // History data
    private int rapidlySpeedupTimes;
    private int rapidlySlowdownTimes;
    private int neutralSlideTimes;
    private int overspeedTimes;
    private int fatigueDrivingTimes;
    private int hthrottleStopTimes;
    private int oilLeakTimes;

}
