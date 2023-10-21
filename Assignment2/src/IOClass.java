import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IOClass {
public static String OutputName;
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
    public static String  getOutputName(String name){
        OutputName = name;
        return OutputName;
    }
}

