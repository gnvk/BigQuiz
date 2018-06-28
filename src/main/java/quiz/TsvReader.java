package quiz;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TsvReader {
    public static List<List<String>> readTsv(InputStream inputStream) {
        List<List<String>> rows = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream, "UTF-8"))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                List<String> columns = Arrays.asList(line.split("\t"));
                rows.add(columns);
                line = bufferedReader.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }
}
