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
    
    public List<List<String>> allCountries = new ArrayList<>(); // store all data in nested list (like 2d array)

    public Countries(String filename) { // reads the data in from a file
        try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/" + filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // splits each line on commas
                allCountries.add(Arrays.asList(values));
            }
        } catch (Exception e) { // prints any errors to help with debugging
            System.out.println(e);
        }
    }

    public String getRandomCountry() { // returns the country code of a random country
        int i = (int) (Math.random() * allCountries.size());
        return allCountries.get(i).get(0);
    }

    public double getLat(String code) { // return the latitude value of a country code
        for (List<String> country : allCountries) { // searches through all countries until code matches
            if (country.get(0).equals(code)) {
                return Double.parseDouble(country.get(1));
            }
        }
        return 0.0;
    }

    public double getLon(String code) { // return the longitude value of a country code
        for (List<String> country : allCountries) {
            if (country.get(0).equals(code)) {
                return Double.parseDouble(country.get(2));
            }
        }
        return 0.0;
    }

    public String getName(String code) { // converts country code (ca) to country name (Canada)
        for (List<String> country : allCountries) {
            if (country.get(0).equals(code)) {
                return country.get(3);
            }
        }
        return "No Country Found";
    }

    public String getCode(String name) { // converts country name (Canada) to country code (ca)
        for (List<String> country : allCountries) {
            if (country.get(3).equals(name)) {
                return country.get(0);
            }
        }
        return "No Country Found";
    }
}
