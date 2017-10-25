package storagewarriors.datapipeline.conf;

import com.google.protobuf.ServiceException;
import lombok.Getter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import storagewarriors.datapipeline.datamodel.InputParams;
import java.io.IOException;

/**
 * Configures HBase.
 * @author team: StorageWarriors (Ambuj, Abhinay, Niti, Rahul)
 * @version 1.0
 */
public class DBConfiguration {
    /**
     * An instance of {@link Configuration} class.
     */
    @Getter
    private Configuration configuration;

    /**
     * The input parameters for the file type.
     */
    private InputParams inputParams;

    /**
     * Constructs {@link DBConfiguration} class.
     * @param inputParams The input parameters for the file type.
     */
    public DBConfiguration(InputParams inputParams) {
        this.inputParams = inputParams;
        this.setConfiguration();
    }

    /**
     * Sets HBase configuration.
     */
    private void setConfiguration() {
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", inputParams.getQuorum());
        configuration.set("hbase.zookeeper.property.clientPort",inputParams.getPort());
        configuration.set("mapreduce.output.fileoutputformat.outputdir", "/home/nayanambuj/tmp");
        //configuration.set("zookeeper.znode.parent","/hbase");
        try {
            HBaseAdmin.checkHBaseAvailable(configuration);
            System.out.printf("\nHBase DB is ACTIVE");
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
