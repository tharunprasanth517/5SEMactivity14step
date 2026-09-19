package com.gdb.domain;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AccountRulesPropertiesLoader {

    private static final String BASE_PATH =
            "src/main/resources/config/rules/";

    public static Properties loadProperties(String accountType) {
        Properties properties = new Properties();

        String filePath = BASE_PATH + accountType.toLowerCase() + ".properties";

        try (InputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        } catch (IOException e) {
            System.out.println("[Config] Could not load rules from " + filePath);
        }

        return properties;
    }

    public static String getProperty(
            Properties properties,
            String key,
            String defaultValue) {

        return properties.getProperty(key, defaultValue);
    }

    public static double getDouble(
            Properties properties,
            String key,
            double defaultValue) {

        String value = properties.getProperty(key);

        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }

        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static int getInt(
            Properties properties,
            String key,
            int defaultValue) {

        String value = properties.getProperty(key);

        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}