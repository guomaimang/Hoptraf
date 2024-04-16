package tech.hirsun.hoptraf.service;

import org.apache.hadoop.util.hash.Hash;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import tech.hirsun.hoptraf.pojo.Driver;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface DriverService {
    public void processRecord(String lastReadTimePrint, String cutOffDatePrint);

    public Driver getDriverInfo(String driverId);

    public Map getDriverDiagram(String driverId);

    public List getDriverList();



}
