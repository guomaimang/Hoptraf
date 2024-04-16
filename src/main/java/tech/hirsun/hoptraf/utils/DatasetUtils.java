package tech.hirsun.hoptraf.utils;

import com.alibaba.fastjson2.JSON;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.List;

public class DatasetUtils {

    public static List DatasetToPojoList(Dataset<Row> dataset, Class clazz){
        // to json first
        List<String> jsonList = dataset.toJSON().collectAsList();

        // json to pojo
        List pojoList = jsonList.stream()
                .map(jsonString -> JSON.parseObject(jsonString, clazz))
                .toList();
        return pojoList;
    }

}
