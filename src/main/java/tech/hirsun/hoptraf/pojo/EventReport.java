package tech.hirsun.hoptraf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventReport {
    Integer id;
    String driverId;
    String carPlateNumber;
    String behavior;
    Date reportTime;
    String details;
}
