package tech.hirsun.hoptraf.dao;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.hirsun.hoptraf.pojo.EventReport;

import java.util.List;

@Mapper
public interface EventReportDao {

    // By Annotation

    @Insert("insert into event_report(driver_id, car_plate_number, behavior, report_time, details) values(#{driverId}, #{carPlateNumber}, #{behavior}, #{reportTime}, #{details})")
    public void insert(EventReport eventReport);

    @Delete("delete  from event_report")
    public void deleteAll();

    // By XML
    public List<EventReport> list(@Param("start") int start,
                                   @Param("pageSize") int pageSize,
                                   @Param("driverId") String driverId,
                                   @Param("keyword") String keyword);


    public Integer count(@Param("driverId") String driverId,
                         @Param("keyword") String keyword);
}
