package uk.co.eazycollect.eazysdk.payment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.Delete;
import uk.co.eazycollect.eazysdk.exceptions.EmptyRequiredParameterException;
import uk.co.eazycollect.eazysdk.exceptions.ResourceNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

public class DeletePayment {
    private final ByteArrayOutputStream errorOutput = new ByteArrayOutputStream();
    private final PrintStream original = System.err;
    private Properties settings;

    @BeforeEach
    public void testInit() {
        System.setErr(new PrintStream(errorOutput));
        ClientHandler handler = new ClientHandler();
        settings = handler.settings();
        settings.setProperty("currentEnvironment.Environment", "sandbox");
        settings.setProperty("sandboxClientDetails.ClientCode", "SDKTST");
        settings.setProperty("sandboxClientDetails.ApiKey", "hkZujzFR2907XAtYe6qkKRsBo");
    }

    @AfterEach
    public void reset() {
        System.setErr(original);
    }

    @Test
    public void testDeletePaymentDeleteValidPayment() {
        Delete delete = new Delete(settings);
        String req = delete.payment("2b62a358-9a1a-4c71-9450-e419e393dcd1", "750f142f-1608-464a-8e34-4b322e703c2c", "A comment");
        Assertions.assertTrue(req.contains("Payment 750f142f-1608-464a-8e34-4b322e703c2c deleted"));
    }

    @Test
    public void testDeletePaymentCommentRequired() {
        Delete delete = new Delete(settings);
        String req = delete.payment("2b62a358-9a1a-4c71-9450-e419e393dcd1", "750f142f-1608-464a-8e34-4b322e703c2c", "");
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testDeletePaymentInvalidContractThrowsException() {
        Delete delete = new Delete(settings);
        String req = delete.payment("2b62a358-9a1a-4c71-9450-e4", "750f142f-1608-464a-8e34-4b322e703c2c", "A comment");
        Assertions.assertTrue(errorOutput.toString().contains("ResourceNotFoundException"));
    }

    @Test
    public void testDeletePaymentInvalidPaymentThrowsException() {
        Delete delete = new Delete(settings);
        String req = delete.payment("2b62a358-9a1a-4c71-9450-e419e393dcd1", "750f142f-1608-464a-8e34-4b32c", "A comment");
        Assertions.assertTrue(errorOutput.toString().contains("ResourceNotFoundException"));
    }

}
