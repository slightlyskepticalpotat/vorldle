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

    public String getRandomCountry() {
        int i = (int) (Math.random() * allCountries.size());
        return allCountries.get(i).get(0);
    }

    public double getLat(String code) {
        for (List<String> country: allCountries) {
            if (country.get(0).equals(code)) {
                return Double.parseDouble(country.get(1));
            }
        }
    return 0.0;
    }

    public double getLon(String code) {
        for (List<String> country: allCountries) {
            if (country.get(0).equals(code)) {
                return Double.parseDouble(country.get(2));
            }
        }
    return 0.0;
    }

    public String getName(String code) {
        for (List<String> country: allCountries) {
            if (country.get(0).equals(code)) {
                return country.get(3);
            }
        }
    return "No Country Found";
    }

}
