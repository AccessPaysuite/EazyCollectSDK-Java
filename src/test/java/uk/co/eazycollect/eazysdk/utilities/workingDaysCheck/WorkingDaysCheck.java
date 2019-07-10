package uk.co.eazycollect.eazysdk.utilities.workingDaysCheck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.exceptions.InvalidSettingsConfigurationException;
import uk.co.eazycollect.eazysdk.utilities.CheckWorkingDays;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Properties;

public class WorkingDaysCheck {
    private ClientHandler handler;
    private Properties settings;
    private CheckWorkingDays workingDays;

    @BeforeEach
    public void testInit() {
        workingDays = new CheckWorkingDays();
    }

    @Test
    public void testOneWorkingDayInTheFuture() throws InvalidSettingsConfigurationException, IOException {
        LocalDate x = workingDays.checkWorkingDaysInFuture(1);
        LocalDate y = LocalDate.of(2019, 7, 8);
        Assertions.assertEquals(y, x);
    }

    @Test
    public void testNextMondayInTheFuture() throws InvalidSettingsConfigurationException, IOException {
        LocalDate x = workingDays.checkWorkingDaysInFuture(2);
        LocalDate y = LocalDate.of(2019, 7, 9);
        Assertions.assertEquals(y, x);
    }

    @Test
    public void testDayAfterNextBankHolidayInTheFuture() throws InvalidSettingsConfigurationException, IOException {
        LocalDate x = workingDays.checkWorkingDaysInFuture(36);
        LocalDate y = LocalDate.of(2019, 8, 27);
        Assertions.assertEquals(y, x);
    }

    @Test
    public void testDayInThePastThrowsException() {
        Assertions.assertThrows(InvalidSettingsConfigurationException.class,
                () -> workingDays.checkWorkingDaysInFuture(-1));
    }

}
