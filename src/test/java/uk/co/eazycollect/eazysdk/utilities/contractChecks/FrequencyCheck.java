package uk.co.eazycollect.eazysdk.utilities.contractChecks;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.exceptions.InvalidIOConfiguration;
import uk.co.eazycollect.eazysdk.utilities.ContractPostChecks;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

public class FrequencyCheck {
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
    public void testValidWeeklySchedule() throws Exception {
        int frequency = contractCheck.checkFrequency("weekly_free", settings);
        Assertions.assertEquals(0, frequency);
    }

    @Test
    public void testValidMonthlySchedule() throws Exception {
        int frequency = contractCheck.checkFrequency("monthly_free", settings);
        Assertions.assertEquals(1, frequency);
    }

    @Test
    public void testValidAnnualFreeSchedule() throws Exception {
        int frequency = contractCheck.checkFrequency("annual_free", settings);
        Assertions.assertEquals(2, frequency);
    }

    @Test
    public void testValidNoFrequency() throws Exception {
        int frequency = contractCheck.checkFrequency("adhoc_monthly_free", settings);
        Assertions.assertEquals(-1, frequency);
    }

    @Test
    public void testInvalidScheduleName() {
        int frequency = contractCheck.checkFrequency("not a schedule name", settings);
        Assertions.assertTrue(errorOutput.toString().contains("InvalidIOConfiguration"));
    }

}
