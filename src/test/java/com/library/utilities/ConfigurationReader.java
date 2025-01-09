package com.library.utilities;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigurationReader {

    /**
     * reads the properties file configuration.properties
     */

    // Create properties object
    // we make this private to be inaccessible from outside
    // make static because static runs first and before everything else and
    // we will use this object under static method
    private static Properties properties;

    // having static block because static runs first
    static {

        try{
            String path = "configuration.properties";
            // Create FileInputStream object to open file as a stream in Java memory
            FileInputStream input = new FileInputStream(path);
            properties = new Properties();
            // Load "properties" object with the "input" we opened using FileInputStream
            properties.load(input);

            input.close();
        }    catch (Exception e){
            System.out.println("File not found in ConfigurationReader class");
            e.printStackTrace();
        }
    }

    public static String getProperty(String keyName) {
        return properties.getProperty(keyName);
    }

}
