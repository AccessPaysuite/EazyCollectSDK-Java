package uk.co.eazycollect.eazysdk.elements;

import java.util.Properties;

public class Payments {
    private final Properties configuration;
    private boolean autoFixPaymentDate;
    private boolean isCreditAllowed;

    public Payments(Properties config) {
        configuration = config;
    }

    public boolean getAutoFixPaymentDate() {
        autoFixPaymentDate = Boolean.parseBoolean(configuration.getProperty("payments.AutoFixPaymentDate"));
        return autoFixPaymentDate;
    }

    public boolean getCreditAllowed() {
        isCreditAllowed = Boolean.parseBoolean(configuration.getProperty("payments.IsCreditAllowed"));
        return isCreditAllowed;
    }

}
