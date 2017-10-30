package storagewarriors.datapipeline.processor;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.Serializable;
import scala.Tuple2;
import storagewarriors.datapipeline.datamodel.InputParams;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Processes flight data.
 * @author team: StorageWarriors (Ambuj, Abhinay, Niti, Rahul)
 * @version 1.0
 */
public class FlightDataProcessor implements DataProcessor, Serializable {
    /**
     * Processes flight data.
     * @param inputParams Input parameters for the data type.
     * @param javaSparkContext An instance of {@link JavaSparkContext} class.
     * @param sparkSession An instance of {@link SparkSession} class.
     * @param job An instance of {@link Job} class.
     */
    @Override
    public void processData(InputParams inputParams,
                            JavaSparkContext javaSparkContext,
                            SparkSession sparkSession,
                            Job job) {
        final Broadcast<String> ROW_KEY_B = javaSparkContext.broadcast(inputParams.getRowKey());
        final Broadcast<ArrayList<HashMap<String,String>>> ROW_VALUES_B =
                javaSparkContext.broadcast(inputParams.getRowValues());

        JavaPairRDD<ImmutableBytesWritable, Put> hbasePuts = sparkSession
                .read()
                .format("csv")
                .option("header", "true")
                .option("inferSchema", "true")
                .csv("file:///" + inputParams.getInputFile())
                .javaRDD().mapToPair(new PairFunction<Row, ImmutableBytesWritable, Put>() {
                    @Override
                    public Tuple2<ImmutableBytesWritable, Put> call(Row data)
                            throws Exception {
                        String[] rowKeys =  ROW_KEY_B.value().split(":");
                        String key = "";
                        for(String k : rowKeys) {
                            key = key + data.getAs(k);
                        }
                        key = key.replaceAll("T", "");
                        key = key.substring(0, key.length() - 1);
                        Put put = new Put(Bytes.toBytes(key));

                        for(HashMap<String,String> val : ROW_VALUES_B.value()) {
                            String[] cq = val.get("qualifier").toString().split(":");
                            if(cq[1].equals("TIME")) {
                            	put.add(Bytes.toBytes(cq[0]), Bytes.toBytes(cq[1]),
                                        Bytes.toBytes(data.getAs(val.get("value")).toString().replaceAll("T", "")));
                            } else{
                            	put.add(Bytes.toBytes(cq[0]), Bytes.toBytes(cq[1]),
                                    Bytes.toBytes(data.getAs(val.get("value")).toString()));
                            }
                        }

                        return new Tuple2<ImmutableBytesWritable, Put>(
                                new ImmutableBytesWritable(), put);
                    }
                });

        long countOfRows = hbasePuts.count();

        System.out.printf("\nNumber of rows to be inserted= %d", countOfRows);
        hbasePuts.saveAsNewAPIHadoopDataset(job.getConfiguration());
    }
}
