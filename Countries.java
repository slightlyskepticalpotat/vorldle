/*
* This Countries class loads a country database, and provides utility methods
* to get a random country, the latitude of a specific country, the longitude
* of a specific country, and convert between a country name and short code.
*
* @author  Anthony Chen, Tony Zhang
* @version 1.0
* @since   2022-06-19
*/

import java.util.*;
import java.io.*;

public class Countries {
    public List<List<String>> allCountries = new ArrayList<>();

    public Countries(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/" + filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                allCountries.add(Arrays.asList(values));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getRandomCountry() {
        int i = (int) (Math.random() * allCountries.size());
        return allCountries.get(i).get(0);
    }

    public double getLat(String code) {
        for (List<String> country : allCountries) {
            if (country.get(0).equals(code)) {
                return Double.parseDouble(country.get(1));
            }
        }
        return 0.0;
    }

    public double getLon(String code) {
        for (List<String> country : allCountries) {
            if (country.get(0).equals(code)) {
                return Double.parseDouble(country.get(2));
            }
        }
        return 0.0;
    }

    public String getName(String code) {
        for (List<String> country : allCountries) {
            if (country.get(0).equals(code)) {
                return country.get(3);
            }
        }
        return "No Country Found";
    }

    public String getCode(String name) {
        for (List<String> country : allCountries) {
            if (country.get(3).equals(name)) {
                return country.get(0);
            }
        }
        return "No Country Found";
    }
}
