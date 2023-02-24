package api.base.helpers;

import api.base.reporter.ExtentLogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CommonMethods {
    public static Properties prop;
    public static Properties dataProp;

    // this method loads the API endpoints from the env.properties file
    public void usePropertyFileForEndpoints() {
        try {
            InputStream ip = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/api/tests/config/env.properties");
            prop = new Properties();
            prop.load(ip);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // this method loads the data from property file for PC API or Tools API
    public void usePropertyFileData(String apiEndPtType){
        InputStream ip = null;
        try {
            switch (apiEndPtType) {
                case "TOOLS-API" ->
                        ip = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/api/tests/config/data-toolsApi.properties");
                case "PC-API" ->
                        ip = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/api/tests/config/data-pcApi.properties");
                default ->
                    ExtentLogger.fail("Unknown API End point type. Valid values are PC-API or TOOLS-API");
            }
            dataProp = new Properties();
            dataProp.load(ip);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}