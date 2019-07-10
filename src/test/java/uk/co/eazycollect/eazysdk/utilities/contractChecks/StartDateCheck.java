package uk.co.eazycollect.eazysdk.utilities.contractChecks;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.exceptions.InvalidSettingsConfigurationException;
import uk.co.eazycollect.eazysdk.exceptions.InvalidStartDateException;
import uk.co.eazycollect.eazysdk.utilities.ContractPostChecks;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

public class StartDateCheck {
    private final ByteArrayOutputStream errorOutput = new ByteArrayOutputStream();
    private final PrintStream original = System.err;
    private Properties settings;
    private ContractPostChecks contractCheck;

    @BeforeEach
    public void testInit() {
        System.setErr(new PrintStream(errorOutput));
        ClientHandler handler = new ClientHandler();
        settings = handler.settings();
        contractCheck = new ContractPostChecks();
        settings.setProperty("currentEnvironment.Environment", "sandbox");
    }

    @AfterEach
    public void reset() {
        System.setErr(original);
    }

    @Test
    public void testValidStartDate() {
        settings.setProperty("directDebitProcessingDays.InitialProcessingDays", "10");
        String schedule = contractCheck.checkStartDateIsValid("2019-08-01", settings);
        Assertions.assertEquals("2019-08-01", schedule);
    }

    @Test
    public void testValidStartDateAfterBankHoliday() {
        settings.setProperty("directDebitProcessingDays.InitialProcessingDays", "43");
        String schedule = contractCheck.checkStartDateIsValid("2019-12-27", settings);
        Assertions.assertEquals("2019-12-27", schedule);
    }

    @Test
    public void testStartDateAcceptsNonISODates() {
        settings.setProperty("directDebitProcessingDays.InitialProcessingDays", "10");
        String schedule = contractCheck.checkStartDateIsValid("01/08/2019", settings);
        Assertions.assertEquals("2019-08-01", schedule);
    }

    @Test
    public void testStartDateInvalidDateThrowsError() {
        String schedule = contractCheck.checkStartDateIsValid("2019-13-13", settings);
        Assertions.assertTrue(errorOutput.toString().contains("InvalidStartDateException"));
    }

    @Test
    public void testStartDateInvalidInitialProcessingDaysThrowsError() {
        settings.setProperty("directDebitProcessingDays.InitialProcessingDays", "ten");
        String schedule = contractCheck.checkStartDateIsValid("2019-08-01", settings);
        Assertions.assertTrue(errorOutput.toString().contains("InvalidSettingsConfigurationException"));
    }

}
