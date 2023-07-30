package common;
//java runner

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class CSVUtils {
    public static String compareCSVs(String sourcefilePath, String destfilePath) throws IOException {
        StringBuilder mismatchReport = new StringBuilder();
        Set<String> lines = new HashSet<>();
        int lineNum = 1;
        // Read all lines from the file into the HashSet
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(destfilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Check if the line to search is in the HashSet

        try (BufferedReader br1 = Files.newBufferedReader(Paths.get(sourcefilePath))) {
            String line;
            while ((line = br1.readLine()) != null) {
                if (!lines.contains(line)) {
                    mismatchReport.append("lineNumber " + lineNum + "in the sourcefile is not found");
                    mismatchReport.append("\n");
                    mismatchReport.append(line);
                    mismatchReport.append("\n");
                }
                lineNum++;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return mismatchReport.toString();
    }
}






