package uk.co.eazycollect.eazysdk.elements;

import java.util.Properties;

public class Warnings {
    private final Properties configuration;
    private boolean customerSearchWarning;

    public Warnings(Properties config) {
        configuration = config;
    }

    public boolean getCustomerSearchWarning() {
        customerSearchWarning = Boolean.parseBoolean(configuration.getProperty("warnings.CustomerSearchWarning"));
        return customerSearchWarning;
    }

}
