package uk.co.eazycollect.eazysdk.contract;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.Patch;
import uk.co.eazycollect.eazysdk.exceptions.EmptyRequiredParameterException;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.exceptions.ParameterNotAllowedException;
import uk.co.eazycollect.eazysdk.exceptions.ResourceNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;
import java.util.Random;

public class PatchContract {
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
    public void testPatchContractPatchContractAmount() {
        Random rand = new Random();
        String randInt = String.valueOf(rand.nextInt(100) + 1);
        Patch patch = new Patch(settings);
        String req = patch.contractAmount("1802e1dd-a657-428c-b8d0-ba162fc76203", randInt, "Change contract amount");
        Assertions.assertTrue(req.contains(randInt));
    }

    @Test
    public void testPatchContractAmountPaymentAmountMustContainOnlyNumbers() {
        Patch patch = new Patch(settings);
        String req = patch.contractAmount("1802e1dd-a657-428c-b8d0-ba162fc76203", "ten", "Change contract amount");
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPatchContractAmountPaymentCannotBeNegative() {
        Patch patch = new Patch(settings);
        String req = patch.contractAmount("1802e1dd-a657-428c-b8d0-ba162fc76203", "-10", "Change contract amount");
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPatchContractAmountCommentCannotBeEmpty() {
        Patch patch = new Patch(settings);
        String req = patch.contractAmount("1802e1dd-a657-428c-b8d0-ba162fc76203", "10", "");
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testPatchContractAmountInvalidContractThrowsException() {
        Patch patch = new Patch(settings);
        String req = patch.contractAmount("1802e1dd-a657-428c-b8d0", "10", "Change contract amount");
        Assertions.assertTrue(errorOutput.toString().contains("ResourceNotFoundException"));
    }

    @Test
    public void testPatchCollectionDateMonthlyContractsValidCollectionDate() {
        Random rand = new Random();
        String randInt = String.valueOf(rand.nextInt(28) + 1);
        Patch patch = new Patch(settings);
        String req = patch.contractDayMonthly("6dfb8179-2f7f-46cb-bc05-fe7f2d36bf36", randInt, "Change contract amount", false);
        Assertions.assertTrue(req.contains(randInt));
    }

    @Test
    public void testPatchCollectionDateMonthlyContractsPatchNextPaymentAmount() {
        Random rand = new Random();
        String randInt = String.valueOf(rand.nextInt(28) + 1);
        Patch patch = new Patch(settings);
        String req = patch.contractDayMonthly("6dfb8179-2f7f-46cb-bc05-fe7f2d36bf36", randInt, "Change contract amount", true, "10.50");
        Assertions.assertTrue(req.contains(randInt));
    }

    @Test
    public void testPatchCollectionDateMonthlyContractsInvalidCollectionDateThrowsException() {
        Patch patch = new Patch(settings);
        String req = patch.contractDayMonthly("6dfb8179-2f7f-46cb-bc05-fe7f2d36bf36", "30", "Change contract amount", false);
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPatchCollectionDateMonthlyContractsCommentCannotBeEmpty() {
        Patch patch = new Patch(settings);
        String req = patch.contractDayMonthly("6dfb8179-2f7f-46cb-bc05-fe7f2d36bf36", "28", "", false);
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testPatchCollectionDateMonthlyIfNotAmendNextPaymentNextPaymentAmountMustNotBeCalled() {
        Patch patch = new Patch(settings);
        String req = patch.contractDayMonthly("6dfb8179-2f7f-46cb-bc05-fe7f2d36bf36", "28", "A comment", false, "10.50");
        Assertions.assertTrue(errorOutput.toString().contains("ParameterNotAllowedException"));
    }

    @Test
    public void testPatchCollectionDateMonthlyContractsIfAmendNextPaymentPaymentAmountMustBeCalled() {
        Patch patch = new Patch(settings);
        String req = patch.contractDayMonthly("6dfb8179-2f7f-46cb-bc05-fe7f2d36bf36", "30", "", true);
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testPatchCollectionDateMonthlyInvalidContractThrowsException() {
        Patch patch = new Patch(settings);
        String req = patch.contractDayMonthly("6dfb8179-2f7f-46cb-bc05", "28", "Change contract amount", false);
        Assertions.assertTrue(errorOutput.toString().contains("ResourceNotFoundException"));
    }

    @Test
    public void testPatchCollectionDateAnnualContractsValidCollectionDate() {
        Random rand = new Random();
        String m = String.valueOf(rand.nextInt(12) + 1);
        String d = String.valueOf(rand.nextInt(28) + 1);
        Patch patch = new Patch(settings);
        String req = patch.contractDayAnnually("8998eab6-f4fe-43b8-b718-78b4520e5529", d, m, "Change contract amount", true, "10.50");
        Assertions.assertTrue(req.contains(m));
    }

    @Test
    public void testPatchCollectionDateAnnualContractsInvalidCollectionDayThrowsException() {
        Patch patch = new Patch(settings);
        String req = patch.contractDayAnnually("8998eab6-f4fe-43b8-b718-78b4520e5529", "15", "15", "Change contract amount", false);
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPatchCollectionDateAnnualContractsCommentCannotBeEmpty() {
        Patch patch = new Patch(settings);
        String req = patch.contractDayAnnually("8998eab6-f4fe-43b8-b718-78b4520e5529", "15", "7", "", false);
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testPatchCollectionDateAnnualIfNotAmendNextPaymentNextPaymentAmountMustNotBeCalled() {
        Patch patch = new Patch(settings);
        String req = patch.contractDayAnnually("8998eab6-f4fe-43b8-b718-78b4520e5529", "15", "7", "A comment", false, "10.50");
        Assertions.assertTrue(errorOutput.toString().contains("ParameterNotAllowedException"));
    }

    @Test
    public void testPatchCollectionDateAnnualContractsIfAmendNextPaymentPaymentAmountMustBeCalled() {
        Patch patch = new Patch(settings);
        String req = patch.contractDayAnnually("8998eab6-f4fe-43b8-b718-78b4520e5529", "15", "7", "A comment", true);
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testPatchCollectionDateAnnualInvalidContractThrowsException() {
        Patch patch = new Patch(settings);
        String req = patch.contractDayAnnually("8998eab6-f4fe-43b8-b718", "15", "7", "A comment", false);
        Assertions.assertTrue(errorOutput.toString().contains("ResourceNotFoundException"));
    }

}
