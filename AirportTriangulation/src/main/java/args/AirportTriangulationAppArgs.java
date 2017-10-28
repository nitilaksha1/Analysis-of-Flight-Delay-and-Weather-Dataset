package args;

import app.AirportTriangulationApp;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * Command line arguments for {@link AirportTriangulationApp} class.
 * @author team: StorageWarriors (Ambuj, Abhinay, Niti, Rahul)
 * @version 1.0
 */
public class AirportTriangulationAppArgs {
    private JCommander commander;

    @Parameter(names = "-weather",
            required = true,
            description = "weather file name")
    private String weatherSource;

    @Parameter(names = "-flight",
            required = true,
            description = "flight file name")
    private String flightSource;

    @Parameter(names = "-out",
            required = true,
            description = "output file name")
    private String output;

    public String getWeatherSource() {
        return weatherSource;
    }

    public String getFlightSource() {
        return flightSource;
    }

    public String getOutput() {
        return output;
    }
}





















