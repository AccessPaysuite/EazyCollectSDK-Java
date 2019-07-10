package uk.co.eazycollect.eazysdk.elements;

import java.util.Properties;

public class SandboxClientDetails {
    private final Properties configuration;
    private String apiKey;
    private String clientCode;

    public SandboxClientDetails(Properties config) {
        configuration = config;
    }

    public String getApiKey() {
        apiKey = configuration.getProperty("sandboxClientDetails.ApiKey");
        return apiKey;
    }

    public String getClientCode() {
        clientCode = configuration.getProperty("sandboxClientDetails.ClientCode");
        return clientCode;
    }

}
