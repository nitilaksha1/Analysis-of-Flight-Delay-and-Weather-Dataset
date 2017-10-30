package storagewarriors.datapipeline.conf;

import lombok.Getter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import storagewarriors.datapipeline.datamodel.InputParams;

import java.io.IOException;

/**
 * Configures hadoop job.
 * @author team: StorageWarriors (Ambuj, Abhinay, Niti, Rahul)
 * @version 1.0
 */
public class JobConfiguration {
    private Configuration configuration;
    private InputParams inputParams;
    private Job job;

    public Job getJob() {
		return job;
	}

	public JobConfiguration(Configuration configuration, InputParams inputParams) {
        this.configuration = configuration;
        this.inputParams = inputParams;
        try {
            this.setJobConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setJobConfiguration() throws IOException {
        this.job = Job.getInstance(this.configuration);
        job.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE, inputParams.getTableName());
        job.setOutputFormatClass(org.apache.hadoop.hbase.mapreduce.TableOutputFormat.class);
    }
}
