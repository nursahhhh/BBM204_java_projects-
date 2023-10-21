import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IOClass {
    /**
     *The OutputName attribute stores the name of the output file.
     */
public static String OutputName;
    /**

     *The readFile method reads the contents of a file and returns an array of Strings.
     *@param path The path of the file to be read.
     * @return An array of Strings representing the contents of the file.
     */
    public static String[] readFile(String path) {
        try {
            int i = 0;
            int length = Files.readAllLines(Paths.get(path)).size();
            String[] results = new String[length];
            for (String line : Files.readAllLines(Paths.get(path))) {
                results[i++] = line;
            }
            return results;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     *The writeToOutputFile method writes a String value to the output file.
     *@param value The String value to be written to the output file.
     */
    public static void writeToOutputFile(String value) {

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(OutputName, true));
            bw.write(value);
            bw.write("\n");
            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    /**
     *The getOutputName method sets the name of the output file.
     *@param name The name of the output file.
     *@return The name of the output file.
     */
    public static String  getOutputName(String name){
        OutputName = name;
        return OutputName;
    }
}

