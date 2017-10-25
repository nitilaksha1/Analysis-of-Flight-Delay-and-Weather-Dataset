package storagewarriors.datapipeline.processor;

import storagewarriors.datapipeline.args.HBaseIngestionArgs;

/**
 * Factory to choose between different data processors.
 * @author team: StorageWarriors (Ambuj, Abhinay, Niti, Rahul)
 * @version 1.0
 */
public class DataProcessFactory {
    /**
     * Returns the required type of data processor based on input parameters.
     * @param hBaseIngestionArgs An instance of {@link HBaseIngestionArgs} class.
     * @return The required instance of {@link DataProcessor} class.
     */
    public static DataProcessor getDataProcessor(HBaseIngestionArgs hBaseIngestionArgs) {
        String dataType = hBaseIngestionArgs.getDataType();

        switch (dataType) {
            case "WEATHER":
                return new WeatherDataProcessor();
            case "FLIGHT":
                return new FlightDataProcessor();
            default:
                return null;
        }
    }
}
