package storagewarriors.datapipeline.app;

import com.beust.jcommander.JCommander;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import storagewarriors.datapipeline.args.HBaseIngestionArgs;
import storagewarriors.datapipeline.conf.DBConfiguration;
import storagewarriors.datapipeline.conf.JobConfiguration;
import storagewarriors.datapipeline.conf.SparkConfiguration;
import storagewarriors.datapipeline.datamodel.InputParams;
import storagewarriors.datapipeline.processor.DataProcessFactory;
import storagewarriors.datapipeline.processor.DataProcessor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * This application reads input data and loads them to HBase tables.
 * @author team: StorageWarriors (Ambuj, Abhinay, Niti, Rahul)
 * @version 1.0
 */
public class HBaseIngestionApp {
    /**
     * Main method.
     * @param args Command line arguments.
     */
    public static void main(String[] args) throws FileNotFoundException {
        HBaseIngestionArgs hBaseIngestionArgs = new HBaseIngestionArgs();
        new JCommander(hBaseIngestionArgs, args);
        String appName = "ImportCsvToHBase";
        String dataType = hBaseIngestionArgs.getDataType();

        InputParams inputParams = null;
        if(dataType.equals("WEATHER")) {
            InputStream input = new FileInputStream(new File("/home/nayanambuj/StorageWarriors/weather.yml"));
            Yaml yaml = new Yaml(new Constructor(InputParams.class));
            inputParams = (InputParams) yaml.load(input);
        }

        if(dataType.equals("FLIGHT")) {
            InputStream input = new FileInputStream(new File("/home/nayanambuj/StorageWarriors/flight.yml"));
            Yaml yaml = new Yaml(new Constructor(InputParams.class));
            inputParams = (InputParams) yaml.load(input);
        }

        SparkConfiguration sparkConfiguration = new SparkConfiguration(appName);
        JavaSparkContext javaSparkContext = sparkConfiguration.getJavaSparkContext();
        SparkSession sparkSession = sparkConfiguration.getSparkSession();

        DBConfiguration dbConfiguration = new DBConfiguration(inputParams);
        Configuration configuration = dbConfiguration.getConfiguration();
        JobConfiguration jobConfiguration = new JobConfiguration(configuration, inputParams);
        Job job = jobConfiguration.getJob();

        DataProcessor dataProcessor = DataProcessFactory.getDataProcessor(hBaseIngestionArgs);
        dataProcessor.processData(inputParams, javaSparkContext, sparkSession, job);

        sparkConfiguration.cleanUp();
    }
}
