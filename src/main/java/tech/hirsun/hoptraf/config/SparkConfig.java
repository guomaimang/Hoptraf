package tech.hirsun.hoptraf.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {

    @Value("${spark.app.name}")
    private String appName;
    @Value("${spark.master.uri}")
    private String sparkMasterUri;

    @Bean
    public SparkConf sparkConf() {

        return new SparkConf()
                .set("spark.ui.enabled","false")
                .setAppName(appName)
                .setMaster(sparkMasterUri);
    }

    @Bean
    @ConditionalOnMissingBean(JavaSparkContext.class)
    public JavaSparkContext javaSparkContext() {
        return new JavaSparkContext(sparkConf());
    }

    @Bean
    public SparkSession sparkSession() {
        SparkSession sparkSession = SparkSession
                .builder()
                .sparkContext(javaSparkContext().sc())
                .getOrCreate();

        // Define the schema we want.
        StructType schema = new StructType()
                .add("driverID", DataTypes.StringType)
                .add("carPlateNumber", DataTypes.StringType)
                .add("latitude", DataTypes.DoubleType)
                .add("longitude", DataTypes.DoubleType)
                .add("speed", DataTypes.IntegerType)
                .add("direction", DataTypes.IntegerType)
                .add("siteName", DataTypes.StringType)
                .add("time", DataTypes.TimestampType)
                .add("isRapidlySpeedup", DataTypes.IntegerType)
                .add("isRapidlySlowdown", DataTypes.IntegerType)
                .add("isNeutralSlide", DataTypes.IntegerType)
                .add("isNeutralSlideFinished", DataTypes.IntegerType)
                .add("neutralSlideTime", DataTypes.IntegerType)
                .add("isOverspeed", DataTypes.IntegerType)
                .add("isOverspeedFinished", DataTypes.IntegerType)
                .add("overspeedTime", DataTypes.IntegerType)
                .add("isFatigueDriving", DataTypes.IntegerType)
                .add("isHthrottleStop", DataTypes.IntegerType)
                .add("isOilLeak", DataTypes.IntegerType);

        Dataset<Row> df = sparkSession.read()
                .format("csv")
                .option("header", "false")
                .schema(schema)
                .csv("src/main/resources/datarecords/*");

        // Create a temporary view
        df.createOrReplaceTempView("driving");

        return sparkSession;
    }


}
