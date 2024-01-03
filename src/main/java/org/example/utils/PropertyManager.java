package org.example.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * This singleton class is responsible to read the properties file(s) from resources folder
 * and the methods will return the string or int properties.
 */
public class PropertyManager {

    private static PropertyManager INSTANCE;
    private static final Properties properties;

    static {
        properties = new Properties();

        try {
            ClassLoader classLoader = PropertyManager.class.getClassLoader();
            InputStream applicationPropertiesStream = classLoader.getResourceAsStream("application.properties");
            properties.load(applicationPropertiesStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized PropertyManager getInstance() {
        if (null == INSTANCE)
            INSTANCE = new PropertyManager();
        return INSTANCE;
    }

    public PropertyManager() {
        INSTANCE = this;
    }

    public String getApplicationVersion(){
        return properties.getProperty("app.version");
    }

    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public int getPropertyAsInt(String propertyName){
        return Integer.parseInt(properties.getProperty(propertyName));
    }

}
