package storagewarriors.datapipeline.args;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Parses the command line arguments provided to HBaseIngestionApp.
 * @author team: StorageWarriors (Ambuj, Abhinay, Niti, Rahul)
 * @version 1.0
 */
public class HBaseIngestionArgs {
    private JCommander commander;

    @Parameter(names = "-dataType",
            required = true,
            description = "Different types of data - weather, flight")
    private String dataType;

	public String getDataType() {
		return dataType;
	}
}
