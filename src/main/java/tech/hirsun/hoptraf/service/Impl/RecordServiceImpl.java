package tech.hirsun.hoptraf.service.Impl;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.stereotype.Service;
import tech.hirsun.hoptraf.service.RecordService;
import tech.hirsun.hoptraf.utils.DatasetUtils;
import tech.hirsun.hoptraf.pojo.TrafRecord;

import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {

    @Override
    public void processRecord(Dataset<Row> records) {
        List<TrafRecord> recordList = DatasetUtils.DatasetToPojoList(records, TrafRecord.class);
        // print recordList
        recordList.forEach(System.out::println);
    }
}
