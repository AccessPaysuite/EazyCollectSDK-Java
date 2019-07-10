package uk.co.eazycollect.eazysdk.utilities.contractChecks;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.utilities.ContractPostChecks;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

public class ScheduleNameCheck {
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
    public void testValidScheduleName() {
        boolean schedule = contractCheck.checkScheduleNameIsAvailable("adhoc_monthly_free", settings);
        Assertions.assertTrue(schedule);
    }

    @Test
    public void testValidScheduleNameFromDifferentService() {
        boolean schedule = contractCheck.checkScheduleAdHocStatus("Best test", settings);
        Assertions.assertTrue(schedule);
    }

    @Test
    public void testScheduleIsNotCaseSensitive() {
        boolean schedule = contractCheck.checkScheduleAdHocStatus("Adhoc_monthly_free", settings);
        Assertions.assertTrue(schedule);
    }

    @Test
    public void testInvalidScheduleNameThrowsError() {
        boolean schedule = contractCheck.checkScheduleAdHocStatus("not a schedule name", settings);
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

}
