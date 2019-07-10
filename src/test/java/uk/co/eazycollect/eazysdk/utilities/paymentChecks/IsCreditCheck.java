package uk.co.eazycollect.eazysdk.utilities.paymentChecks;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.utilities.PaymentPostChecks;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

public class IsCreditCheck {
    private final ByteArrayOutputStream errorOutput = new ByteArrayOutputStream();
    private final PrintStream original = System.err;
    private PaymentPostChecks paymentCheck;
    private Properties settings;

    @BeforeEach
    public void testInit() {
        System.setErr(new PrintStream(errorOutput));
        ClientHandler handler = new ClientHandler();
        paymentCheck = new PaymentPostChecks();
        settings = handler.settings();
    }

    @AfterEach
    public void reset() {
        System.setErr(original);
    }

    @Test
    public void testValidIsCreditReturnsTrue() {
        settings.setProperty("payments.IsCreditAllowed", "true");
        boolean isCredit = paymentCheck.checkIfCreditIsAllowed(settings);
        Assertions.assertTrue(isCredit);
    }

    @Test
    public void testValidNotCreditReturnsFalse() {
        settings.setProperty("payments.IsCreditAllowed", "false");
        boolean isCredit = paymentCheck.checkIfCreditIsAllowed(settings);
        Assertions.assertFalse(isCredit);
    }

    @Test
    public void testInvalidIsCreditThrowsError() {
        settings.setProperty("payments.IsCreditAllowed", "is not credit");
        boolean isCredit = paymentCheck.checkIfCreditIsAllowed(settings);
        Assertions.assertTrue(errorOutput.toString().contains("InvalidSettingsConfigurationException"));
    }

}
