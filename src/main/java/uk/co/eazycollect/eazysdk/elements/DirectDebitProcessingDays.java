package uk.co.eazycollect.eazysdk.elements;

import java.util.Properties;

public class DirectDebitProcessingDays {
    private final Properties configuration;
    private int initialProcessingDays;
    private int ongoingProcessingDays;

    public DirectDebitProcessingDays(Properties config) {
        configuration = config;
    }

    public int getInitialProcessingDays() {
        initialProcessingDays = Integer.parseInt(configuration.getProperty("directDebitProcessingDays.InitialProcessingDays"));
        return initialProcessingDays;
    }

    public int getOngoingProcessingDays() {
        ongoingProcessingDays = Integer.parseInt(configuration.getProperty("directDebitProcessingDays.OngoingProcessingDays"));
        return ongoingProcessingDays;
    }

}
