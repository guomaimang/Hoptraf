package tech.hirsun.hoptraf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrafRecord {

    private String driverID;
    private String carPlateNumber;
    private double latitude;
    private double longitude;
    private int speed;
    private int direction;
    private String siteName;
    private Date time;
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

}
