package uk.co.eazycollect.eazysdk.utilities.paymentChecks;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.exceptions.InvalidPaymentDateException;
import uk.co.eazycollect.eazysdk.exceptions.InvalidSettingsConfigurationException;
import uk.co.eazycollect.eazysdk.utilities.PaymentPostChecks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

public class CollectionDateCheck {
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
        settings.setProperty("currentEnvironment.Environment", "sandbox");
    }

    @AfterEach
    public void reset() {
        System.setErr(original);
    }

    @Test
    public void testValidPaymentDate() {
        settings.setProperty("directDebitProcessingDays.OngoingProcessingDays", "5");
        String paymentAmount = paymentCheck.checkPaymentDate("2019-08-01", settings);
        Assertions.assertEquals("2019-08-01", paymentAmount);
    }

    @Test
    public void testValidPaymentDateAfterBankHoliday() {
        settings.setProperty("directDebitProcessingDays.OngoingProcessingDays", "43");
        String paymentAmount = paymentCheck.checkPaymentDate("2019-12-27", settings);
        Assertions.assertEquals("2019-12-27", paymentAmount);
    }

    @Test
    public void testPaymentDateAcceptsNonISODates() {
        settings.setProperty("directDebitProcessingDays.OngoingProcessingDays", "5");
        String paymentAmount = paymentCheck.checkPaymentDate("01/08/2019", settings);
        Assertions.assertEquals("2019-08-01", paymentAmount);
    }

    @Test
    public void testInvalidOngoingProcessingDaysThrowsError() {
        settings.setProperty("directDebitProcessingDays.OngoingProcessingDays", "five");
        String paymentAmount = paymentCheck.checkPaymentDate("01/08/2019", settings);
        Assertions.assertTrue(errorOutput.toString().contains("InvalidSettingsConfigurationException"));
    }

    @Test
    public void testPaymentDateInvalidDateThrowsError() {
        String paymentAmount = paymentCheck.checkPaymentDate("2019-13-13", settings);
        Assertions.assertTrue(errorOutput.toString().contains("InvalidPaymentDateException"));
    }

}
