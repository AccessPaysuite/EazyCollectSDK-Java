package uk.co.eazycollect.eazysdk.directDebit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.Post;
import uk.co.eazycollect.eazysdk.exceptions.ResourceNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

public class Restart {
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
    public void testRestartContractValidContract() {
        Post post = new Post(settings);
        String req = new Post.restartContract().contract("a899ced6-a601-4146-92f7-5c8ee40bbf93")
                .terminationType("Until further notice").atTheEnd("Switch to further notice").paymentDayInMonth("15")
                .paymentMonthInYear("6").query();
        Assertions.assertTrue(req.contains("Contract restarted"));
    }

    @Test
    public void testRestartContractInvalidContractThrowException() {
        Post post = new Post(settings);
        String req = new Post.restartContract().contract("a899ced6-a601-4146-92f7-5c8")
                .terminationType("Until further notice").atTheEnd("Switch to further notice").paymentDayInMonth("15")
                .paymentMonthInYear("6").query();
        Assertions.assertTrue(errorOutput.toString().contains("ResourceNotFoundException"));
    }

}
