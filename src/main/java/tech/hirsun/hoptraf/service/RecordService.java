package tech.hirsun.hoptraf.service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.List;

public interface RecordService {

    public void processRecord(Dataset<Row> records);

}
