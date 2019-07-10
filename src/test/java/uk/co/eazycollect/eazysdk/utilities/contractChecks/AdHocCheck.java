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

public class AdHocCheck {
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
    public void testValidAdHocContract() {
        boolean adHoc = contractCheck.checkScheduleAdHocStatus("adhoc_monthly_free", settings);
        Assertions.assertTrue(adHoc);
    }

    @Test
    public void testValidNonAdHocContract() {
        boolean adHoc = contractCheck.checkScheduleAdHocStatus("test", settings);
        Assertions.assertFalse(adHoc);
    }

    @Test
    public void testInvalidContractThrowsError() {
        boolean adHoc = contractCheck.checkScheduleAdHocStatus("not a contract", settings);
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

}
