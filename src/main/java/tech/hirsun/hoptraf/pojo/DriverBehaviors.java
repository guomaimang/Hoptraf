package tech.hirsun.hoptraf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DriverBehaviors {

    private String driverID;

    // Driving behavior in slot
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

}
