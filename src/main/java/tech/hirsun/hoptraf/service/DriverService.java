package tech.hirsun.hoptraf.service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.List;

public interface DriverService {
    public void processRecord(Dataset<Row> records);

    public void getDriverInfo(String driverId);

    public void getDriverDiagram(String driverId);

    public List getDriverList();



}
