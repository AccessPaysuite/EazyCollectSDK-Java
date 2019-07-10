package uk.co.eazycollect.eazysdk.payment;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.Get;
import uk.co.eazycollect.eazysdk.exceptions.ResourceNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

public class GetPayment {
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
    public void testGetAllPaymentsFromAContract() {
        Get get = new Get(settings);
        String req = get.payments("2b62a358-9a1a-4c71-9450-e419e393dcd1");
        Assertions.assertTrue(req.contains("be047ad1-e314-4980-9cf6-2bb1d324a41d"));
    }

    @Test
    public void testGetAllPaymentsFromAContractNoPaymentsReturnsMessage() {
        Get get = new Get(settings);
        String req = get.payments("a4d77bc5-8480-4148-8838-1afd7f5bab6b");
        Assertions.assertTrue(req.contains("No payments could be associated with the contract"));
    }

    @Test
    public void testGetXNumberOfPaymentsFromAContract() {
        Get get = new Get(settings);
        String req = get.payments("2b62a358-9a1a-4c71-9450-e419e393dcd1", 2);
        JsonArray reqAsJson = new JsonParser().parse(req).getAsJsonArray();
        Assertions.assertEquals(2, reqAsJson.size());
    }

    @Test
    public void testGetASinglePayment() {
        Get get = new Get(settings);
        String req = get.paymentsSingle("2b62a358-9a1a-4c71-9450-e419e393dcd1", "6917d51a-be83-424f-b0a6-31fbf9574a79");
        Assertions.assertTrue(req.contains("\"Amount\":10.0"));
    }

    @Test
    public void testGetAllPaymentsInvalidContractThrowsError() {
        Get get = new Get(settings);
        String req = get.payments("2b62a358-9a1a-4c71-9450-e419e393dc");
        Assertions.assertTrue(errorOutput.toString().contains("ResourceNotFoundException"));
    }

    @Test
    public void testGetASinglePaymentInvalidContractThrowsError() {
        Get get = new Get(settings);
        String req = get.paymentsSingle("2b62a358-9a1a-4c71-9450-e41", "6917d51a-be83-424f-b0a6-31fbf9574a79");
        Assertions.assertTrue(errorOutput.toString().contains("ResourceNotFoundException"));
    }

    @Test
    public void testGetASinglePaymentInvalidPaymentThrowsError() {
        Get get = new Get(settings);
        String req = get.paymentsSingle("2b62a358-9a1a-4c71-9450-e419e393dcd1", "6917d51a-be83-424f-b0a6");
        Assertions.assertTrue(errorOutput.toString().contains("ResourceNotFoundException"));
    }

}
