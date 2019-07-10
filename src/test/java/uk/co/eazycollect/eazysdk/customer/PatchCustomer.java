package uk.co.eazycollect.eazysdk.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.Patch;
import uk.co.eazycollect.eazysdk.Post;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.exceptions.ResourceNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

public class PatchCustomer {
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
    public void testPatchCustomerPatchingAllDetailsReturnsCustomer() {
        Patch patch = new Patch(settings);
        String req = new Patch.customer("310a826b-d095-48e7-a55a-19dba82c566f").email("test@email.com").title("Mr")
                .dateOfBirth("1990-01-01").firstName("Test").surname("Test").companyName("TestCom").line1("1 Test Road")
                .line2("Line2").line3("line3").line4("line4").postCode("GL52 2NF").accountNumber("12345678")
                .sortCode("123456").accountHolderName("Mr Test Test").homePhone("01242694874")
                .mobilePhone("07398745641").workPhone("0145213458").initials("tt").query();
        Assertions.assertTrue(req.contains("The customer 310a826b-d095-48e7-a55a-19dba82c566f has been updated successfully"));
    }

    @Test
    public void testPatchCustomerPostCodeTooShortThrowsException(){
        Patch patch = new Patch(settings);
        String req = new Patch.customer("310a826b-d095-48e7-a55a-19dba82c566f").postCode("1").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPatchCustomerPostCodeTooLongThrowsException() {
        Patch patch = new Patch(settings);
        String req = new Patch.customer("310a826b-d095-48e7-a55a-19dba82c566f").postCode("GL51 ABCDEFG").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPatchCustomerPostCodeMustContainNumbers() {
        Patch patch = new Patch(settings);
        String req = new Patch.customer("310a826b-d095-48e7-a55a-19dba82c566f").postCode("GLFO NBA").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPatchCustomerPostCodeMustContainLetters() {
        Patch patch = new Patch(settings);
        String req = new Patch.customer("310a826b-d095-48e7-a55a-19dba82c566f").postCode("1234 123").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPatchCustomerEmailMustBeValidFormat() {
        Patch patch = new Patch(settings);
        String req = new Patch.customer("310a826b-d095-48e7-a55a-19dba82c566f").email("helpeazycollect.co.uk").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPatchCustomerAccountNumberMustNotContainLetters() {
        Patch patch = new Patch(settings);
        String req = new Patch.customer("310a826b-d095-48e7-a55a-19dba82c566f").accountNumber("1234567A").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPatchCustomerAccountNumberMustNotBeTooShort() {
        Patch patch = new Patch(settings);
        String req = new Patch.customer("310a826b-d095-48e7-a55a-19dba82c566f").accountNumber("1234567").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPatchCustomerAccountNumberMustNotBeTooLong() {
        Patch patch = new Patch(settings);
        String req = new Patch.customer("310a826b-d095-48e7-a55a-19dba82c566f").accountNumber("123456789").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPatchCustomerSortCodeMustNotBeTooShort() {
        Patch patch = new Patch(settings);
        String req = new Patch.customer("310a826b-d095-48e7-a55a-19dba82c566f").sortCode("12345").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPatchCustomerSortCodeMustNotBeTooLong() {
        Patch patch = new Patch(settings);
        String req = new Patch.customer("310a826b-d095-48e7-a55a-19dba82c566f").sortCode("1234567").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPatchCustomerSortCodeMustNotContainLetters() {
        Patch patch = new Patch(settings);
        String req = new Patch.customer("310a826b-d095-48e7-a55a-19dba82c566f").sortCode("12345A").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPatchCustomerAccountHolderNameMustNotSpecialCharacters() {
        Patch patch = new Patch(settings);
        String req = new Patch.customer("310a826b-d095-48e7-a55a-19dba82c566f").accountHolderName("TEST ! ~ # te").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPatchCustomerAccountHolderNameMustNotBeTooShort() {
        Patch patch = new Patch(settings);
        String req = new Patch.customer("310a826b-d095-48e7-a55a-19dba82c566f").accountHolderName("TE").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPatchCustomerAccountHolderNameMustNotBeTooLong() {
        Patch patch = new Patch(settings);
        String req = new Patch.customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .accountHolderName("TEST ACCOUNT HOLDER NAME TEST").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPatchCustomerInvalidCustomerThrowsException() {
        Patch patch = new Patch(settings);
        String req = new Patch.customer("310a826b-d095-48e7-a55a-19db").email("test@email.com").query();
        Assertions.assertTrue(errorOutput.toString().contains("ResourceNotFoundException"));
    }

}
