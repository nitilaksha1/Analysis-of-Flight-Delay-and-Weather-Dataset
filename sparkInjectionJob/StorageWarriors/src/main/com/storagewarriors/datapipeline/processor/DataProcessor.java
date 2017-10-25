package storagewarriors.datapipeline.processor;

import org.apache.hadoop.mapreduce.Job;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import storagewarriors.datapipeline.datamodel.InputParams;

/**
 * Specifies the format of data processors.
 */
public interface DataProcessor {
    /**
     * Function to process input data and insert to DB.
     * @param inputParams Input parameters for the data type.
     * @param javaSparkContext An instance of {@link JavaSparkContext} class.
     * @param sparkSession An instance of {@link SparkSession} class.
     * @param job An instance of {@link Job} class.
     */
    void processData(InputParams inputParams,
                     JavaSparkContext javaSparkContext,
                     SparkSession sparkSession,
                     Job job);
}
