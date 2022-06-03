import java.util.*;
import java.io.*;

public class Countries {
    public List<List<String>> allCountries = new ArrayList<>();

    public Countries(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                allCountries.add(Arrays.asList(values));
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
