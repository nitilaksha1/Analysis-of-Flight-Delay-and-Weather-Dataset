package storagewarriors.datapipeline.datamodel;

import lombok.Getter;
import lombok.Setter;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Data access object for the input parameters.
 * @author team: StorageWarriors (Ambuj, Abhinay, Niti, Rahul)
 * @version 1.0
 */
@Getter
@Setter
public class InputParams {
    private String inputFile;
    private String quorum;
    private String port;
    private String tableName;
    private String rowKey;
    private ArrayList<HashMap<String,String>> rowValues;
}