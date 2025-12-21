package vn.bluemoon.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * Database configuration
 */
public class DbConfig {
    private static DbConfig instance;
    private Properties properties;

    private DbConfig() {
        loadProperties();
    }

    public static DbConfig getInstance() {
        if (instance == null) {
            instance = new DbConfig();
        }
        return instance;
    }

    private void loadProperties() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("application.properties not found");
            }
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Error loading application.properties", e);
        }
    }

    public String getUrl() {
        return getProperty("db.url", "jdbc:mysql://localhost:3306/blue_moon");
    }

    public String getUsername() {
        return getProperty("db.username", "root");
    }

    public String getPassword() {
        return getProperty("db.password", "");
    }

    public String getDriver() {
        return getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
    }

    private String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}


