package uk.co.eazycollect.eazysdk.payment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.Post;
import uk.co.eazycollect.eazysdk.exceptions.EmptyRequiredParameterException;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.exceptions.InvalidPaymentDateException;
import uk.co.eazycollect.eazysdk.exceptions.ResourceNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

public class PostPayment {
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
    public void testPostPaymentUsingAllRequiredFields() {
        Post post = new Post(settings);
        String req = post.payment("1802e1dd-a657-428c-b8d0-ba162fc76203", "10.00", "2019-08-01", "A comment");
        Assertions.assertTrue(req.contains("\"Error\":null"));
    }

    @Test
    public void testPostPaymentIsCreditAllowedIfSettingTrue() {
        settings.setProperty("payments.IsCreditAllowed", "true");
        Post post = new Post(settings);
        String req = post.payment("1802e1dd-a657-428c-b8d0-ba162fc76203", "10.00", "2019-08-01", "A comment", true);
        Assertions.assertTrue(req.contains("Client is not allowed to credit customer"));
    }

    @Test
    public void testPostPaymentAutoFixPaymentDate() {
        settings.setProperty("payments.AutoFixPaymentDate", "true");
        Post post = new Post(settings);
        String req = post.payment("1802e1dd-a657-428c-b8d0-ba162fc76203", "10.00", "2018-07-15", "A comment");
        Assertions.assertTrue(req.contains("\"Error\":null"));
    }

    @Test
    public void testPostPaymentACommentIsRequired() {
        Post post = new Post(settings);
        String req = post.payment("1802e1dd-a657-428c-b8d0-ba162fc76203", "10.00", "2019-08-01", "");
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testPostPaymentPaymentAmountCannotBeNegative() {
        Post post = new Post(settings);
        String req = post.payment("1802e1dd-a657-428c-b8d0-ba162fc76203", "-10.00", "2019-08-01", "A comment");
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPostPaymentInvalidContractThrowsException() {
        Post post = new Post(settings);
        String req = post.payment("1802e1dd-a657-428c-b8d0-b203", "10.00", "2019-08-01", "A comment");
        Assertions.assertTrue(errorOutput.toString().contains("ResourceNotFoundException"));
    }

    @Test
    public void testPostPaymentIsCreditNotAllowedIfSettingIsFalse() {
        settings.setProperty("payments.IsCreditAllowed", "false");
        Post post = new Post(settings);
        String req = post.payment("1802e1dd-a657-428c-b8d0-ba162fc76203", "10.00", "2019-08-01", "A comment", true);
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPostPaymentInvalidPaymentDateThrowsException() {
        settings.setProperty("payments.AutoFixPaymentDate", "false");
        Post post = new Post(settings);
        String req = post.payment("1802e1dd-a657-428c-b8d0-ba162fc76203", "10.00", "2018-07-15", "A comment");
        Assertions.assertTrue(errorOutput.toString().contains("InvalidPaymentDateException"));
    }

}
