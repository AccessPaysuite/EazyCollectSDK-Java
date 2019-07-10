package uk.co.eazycollect.eazysdk.elements;

import java.util.Properties;

public class Contracts {
    private final Properties configuration;
    private boolean autoFixStartDate;
    private boolean autoFixTerminationTypeAdHoc;
    private boolean autoFixAtTheEndAdHoc;
    private boolean autoFixPaymentDayInMonth;
    private boolean autoFixPaymentMonthInYear;

    public Contracts(Properties config) {
        configuration = config;
    }

    public boolean getAutoFixStartDate() {
        autoFixStartDate = Boolean.parseBoolean(configuration.getProperty("contracts.AutoFixStartDate"));
        return autoFixStartDate;
    }

    public boolean getAutoFixTerminationTypeAdHoc() {
        autoFixTerminationTypeAdHoc = Boolean.parseBoolean(configuration.getProperty("contracts.AutoFixTerminationTypeAdHoc"));
        return autoFixTerminationTypeAdHoc;
    }

    public boolean getAutoFixAtTheEndAdHoc() {
        autoFixAtTheEndAdHoc = Boolean.parseBoolean(configuration.getProperty("contracts.AutoFixAtTheEndAdHoc"));
        return autoFixAtTheEndAdHoc;
    }

    public boolean getAutoFixPaymentDayInMonth() {
        autoFixPaymentDayInMonth = Boolean.parseBoolean(configuration.getProperty("contracts.AutoFixPaymentDayInMonth"));
        return autoFixPaymentDayInMonth;
    }

    public boolean getAutoFixPaymentMonthInYear() {
        autoFixPaymentMonthInYear = Boolean.parseBoolean(configuration.getProperty("contracts.AutoFixPaymentMonthInYear"));
        return autoFixPaymentMonthInYear;
    }

}
