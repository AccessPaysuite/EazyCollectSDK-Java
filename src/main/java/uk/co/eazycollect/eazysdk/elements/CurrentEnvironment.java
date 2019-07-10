package uk.co.eazycollect.eazysdk.elements;

import java.util.Properties;

public class CurrentEnvironment {
    private final Properties configuration;
    private String environment;

    public CurrentEnvironment(Properties config) {
        configuration = config;
    }

    public String getEnvironment() {
        environment = configuration.getProperty("currentEnvironment.Environment");
        return environment;
    }

}
