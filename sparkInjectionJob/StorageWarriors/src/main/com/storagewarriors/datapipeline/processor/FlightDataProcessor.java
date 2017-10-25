package storagewarriors.datapipeline.processor;

import org.apache.hadoop.mapreduce.Job;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import storagewarriors.datapipeline.datamodel.InputParams;

/**
 * Processes flight data.
 * @author team: StorageWarriors (Ambuj, Abhinay, Niti, Rahul)
 * @version 1.0
 */
public class FlightDataProcessor implements DataProcessor {
    /**
     * Processes flight data.
     * @param inputParams Input parameters for the data type.
     * @param javaSparkContext An instance of {@link JavaSparkContext} class.
     * @param sparkSession An instance of {@link SparkSession} class.
     * @param job An instance of {@link Job} class.
     */
    @Override
    public void processData(InputParams inputParams, JavaSparkContext javaSparkContext, SparkSession sparkSession, Job job) {
        // add code
    }
}
