import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import input.Input;
import input.InputLoader;
import turn.Turn;

import java.io.File;
import java.io.IOException;

/**
 * Entry point to the simulation
 */
public final class Main {

    /**
     * Constructor
     */
    private Main() {
    }

    /**
     * Main function which reads the input file and starts simulation
     *
     * @param args input and output files
     * @throws Exception might error when reading/writing/opening files, parsing JSON
     */
    public static void main(final String[] args) throws Exception {
        simulate(args[0], args[1]);
       // simulate("checker/resources/in/basic_5.json","test.out");
    }

    /**
     * @param inPath for input file
     * @param outPath for output file
     */
    public static void simulate(final String inPath,
                                final String outPath) throws IOException {
        // Read data from input file
        InputLoader inputLoader = new InputLoader(inPath);
        Input input = inputLoader.readData();
     //   System.out.println(input);

        // Do turns
        Turn turn = new Turn();
        turn.doTurns(input);
        //System.out.println();
       // System.out.println(input);

        // Write data to output file
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.writeValue(new File(outPath), input);

    }
}