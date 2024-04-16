package tech.hirsun.hoptraf.service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface DriverService {
    public void processRecord(Dataset<Row> records);

    public void getUserInfo(String driverId);

    public void getUserDiagram(String driverId);


}
