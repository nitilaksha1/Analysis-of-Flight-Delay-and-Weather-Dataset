package storagewarriors.datapipeline.conf;

import lombok.Getter;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import storagewarriors.datapipeline.processor.WeatherDataProcessor;

/**
 * Configures spark.
 * @author team: StorageWarriors (Ambuj, Abhinay, Niti, Rahul)
 * @version 1.0
 */
public class SparkConfiguration {
    /**
     * An instance of {@link SparkConf} class.
     */
    private SparkConf sparkConf;

    /**
     * An instance of {@link JavaSparkContext} class.
     */
    private JavaSparkContext javaSparkContext;

    /**
     * An instance of {@link SparkSession} class.
     */
    private SparkSession sparkSession;

    public JavaSparkContext getJavaSparkContext() {
		return javaSparkContext;
	}

	public SparkSession getSparkSession() {
		return sparkSession;
	}

	/**
     * Application name.
     */
    private String appName;

    /**
     * Constructor for {@link SparkConfiguration} class.
     * @param appName Application name.
     */
    public SparkConfiguration(String appName) {
        this.appName = appName;
        this.setSparkConf();
        this.setJavaSparkContext();
        this.setSparkSession();
    }

    /**
     * Sets spark configuration.
     */
        private void setSparkConf() {
        sparkConf = new SparkConf()
                .setAppName(appName)
                .setMaster("local[*]")
                .set("spark.default.parallelism", "8")
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        sparkConf.registerKryoClasses(new Class[] {WeatherDataProcessor.class});
    }

    /**
     * Sets Java spark context.
     */
    private void setJavaSparkContext() {
        javaSparkContext = new JavaSparkContext(sparkConf);
    }

    /**
     * Sets spark session.
     */
    private void setSparkSession() {
        sparkSession = SparkSession
                .builder()
                .appName(appName)
                .config(sparkConf)
                .getOrCreate();
    }

    /**
     * Closes the open resources.
     */
    public void cleanUp() {
        javaSparkContext.close();
    }
}
