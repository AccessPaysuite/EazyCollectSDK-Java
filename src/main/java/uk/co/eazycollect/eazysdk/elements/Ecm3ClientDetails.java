package uk.co.eazycollect.eazysdk.elements;

import java.util.Properties;

public class Ecm3ClientDetails {
    private final Properties configuration;
    private String apiKey;
    private String clientCode;

    public Ecm3ClientDetails(Properties config) {
        configuration = config;
    }

    public String getApiKey() {
        apiKey = configuration.getProperty("ecm3ClientDetails.ApiKey");
        return apiKey;
    }

    public String getClientCode() {
        clientCode = configuration.getProperty("ecm3ClientDetails.ClientCode");
        return clientCode;
    }

}
