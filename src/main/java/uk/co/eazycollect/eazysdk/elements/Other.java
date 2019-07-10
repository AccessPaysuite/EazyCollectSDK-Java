package uk.co.eazycollect.eazysdk.elements;

import java.util.Properties;

public class Other {
    private final Properties configuration;
    private int bankHolidayUpdateDays;
    private boolean forceUpdateSchedulesOnRun;

    public Other(Properties config) {
        configuration = config;
    }

    public int getBankHolidayUpdateDays() {
        bankHolidayUpdateDays = Integer.parseInt(configuration.getProperty("other.BankHolidayUpdateDays"));
        return bankHolidayUpdateDays;
    }

    public boolean getForceUpdateSchedulesOnRun() {
        forceUpdateSchedulesOnRun = Boolean.parseBoolean(configuration.getProperty("other.ForceUpdateSchedulesOnRun"));
        return forceUpdateSchedulesOnRun;
    }

}
