package tech.hirsun.hoptraf.dao;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import tech.hirsun.hoptraf.pojo.EventReport;

import java.util.List;

@Mapper
public interface EventReportDao {

    @Insert("insert into event_report(driver_id, car_plate_number, behavior, report_time, details) values(#{driverId}, #{carPlateNumber}, #{behavior}, #{reportTime}, #{details})")
    public void insert(EventReport eventReport);

    public List<tech.hirsun.hoptraf.pojo.EventReport> getByDriverId(String driverId);

    @Delete("delete  from event_report")
    public void deleteAll();

}
