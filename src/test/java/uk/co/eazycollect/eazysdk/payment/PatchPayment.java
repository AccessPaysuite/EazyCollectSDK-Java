package uk.co.eazycollect.eazysdk.payment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.Patch;

import java.util.Properties;

public class PatchPayment {
    private Properties settings;

    @BeforeEach
    public void testInit() {
        ClientHandler handler = new ClientHandler();
        settings = handler.settings();
        settings.setProperty("currentEnvironment.Environment", "sandbox");
        settings.setProperty("sandboxClientDetails.ClientCode", "SDKTST");
        settings.setProperty("sandboxClientDetails.ApiKey", "hkZujzFR2907XAtYe6qkKRsBo");
    }

    @Test
    public void testPatchPaymentValidPatch() throws Exception {
        Patch patch = new Patch(settings);
        String req = patch.payment("2b62a358-9a1a-4c71-9450-e419e393dcd1", "a75f9829-2753-4f67-aafb-bb24aba27dd1", "10.00", "2019-08-01", "test comment");
        Assertions.assertTrue(req.contains("Payment updated"));
    }

}
