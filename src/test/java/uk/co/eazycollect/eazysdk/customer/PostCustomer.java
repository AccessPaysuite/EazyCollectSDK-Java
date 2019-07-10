package uk.co.eazycollect.eazysdk.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.Post;
import uk.co.eazycollect.eazysdk.exceptions.EmptyRequiredParameterException;
import uk.co.eazycollect.eazysdk.exceptions.RecordAlreadyExistsException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class PostCustomer {
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
    public void testPostCustomerUsingOnlyRequiredFields() {
        String randInt = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        Post post = new Post(settings);
        String req = new Post.customer().email(randInt + "@test.com").title("Mr").customerReference(randInt)
                .firstName("John").surname("Doe").line1("1 Tebbit Mews").postCode("GL52 2NF").accountNumber("12345678")
                .sortCode("123456").accountHolderName("Mr John Doe").query();
        Assertions.assertTrue(req.contains(randInt));
    }

    @Test
    public void testPostCustomerUsingAllFields() {
        String randInt = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        Post post = new Post(settings);
        String req = new Post.customer().email(randInt + "@test.com").title("Mr").customerReference(randInt)
                .firstName("John").surname("Doe").line1("1 Tebbit Mews").postCode("GL52 2NF").accountNumber("12345678")
                .sortCode("123456").accountHolderName("Mr John Doe").line2("Eazy Collect").line3("Cheltenham")
                .line4("Gloucestershire").companyName("Acme Ltd").dateOfBirth("1990-01-01").initials("JD")
                .homePhone("01242000000").mobilePhone("07300000000").workPhone("01242000000").query();
        Assertions.assertTrue(req.contains(randInt));
    }

    @Test
    public void testCustomerReferenceMustBeUnique() {
        String randInt = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        Post post = new Post(settings);
        String req = new Post.customer().email(randInt + "@test.com").title("Mr").customerReference("test-000001")
                .firstName("John").surname("Doe").line1("1 Tebbit Mews").postCode("GL52 2NF").accountNumber("12345678")
                .sortCode("123456").accountHolderName("Mr John Doe").line2("Eazy Collect").line3("Cheltenham")
                .line4("Gloucestershire").companyName("Acme Ltd").dateOfBirth("1990-01-01").initials("JD")
                .homePhone("01242000000").mobilePhone("07300000000").workPhone("01242000000").query();
        Assertions.assertTrue(errorOutput.toString().contains("RecordAlreadyExistsException"));
    }

    @Test
    public void testEmailMustBePassed() {
        String randInt = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        Post post = new Post(settings);
        String req = new Post.customer().email("").title("Mr").customerReference(randInt)
                .firstName("John").surname("Doe").line1("1 Tebbit Mews").postCode("GL52 2NF").accountNumber("12345678")
                .sortCode("123456").accountHolderName("Mr John Doe").query();
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testTitleMustBePassed() {
        String randInt = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        Post post = new Post(settings);
        String req = new Post.customer().email(randInt + "@test.com").title("").customerReference(randInt)
                .firstName("John").surname("Doe").line1("1 Tebbit Mews").postCode("GL52 2NF").accountNumber("12345678")
                .sortCode("123456").accountHolderName("Mr John Doe").query();
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testCustomerReferenceMustBePassed() {
        String randInt = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        Post post = new Post(settings);
        String req = new Post.customer().email(randInt + "@test.com").title("Mr").customerReference("")
                .firstName("John").surname("Doe").line1("1 Tebbit Mews").postCode("GL52 2NF").accountNumber("12345678")
                .sortCode("123456").accountHolderName("Mr John Doe").query();
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testFirstNameMustBePassed() {
        String randInt = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        Post post = new Post(settings);
        String req = new Post.customer().email(randInt + "@test.com").title("Mr").customerReference(randInt)
                .firstName("").surname("Doe").line1("1 Tebbit Mews").postCode("GL52 2NF").accountNumber("12345678")
                .sortCode("123456").accountHolderName("Mr John Doe").query();
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testSurnameMustBePassed() {
        String randInt = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        Post post = new Post(settings);
        String req = new Post.customer().email(randInt + "@test.com").title("Mr").customerReference(randInt)
                .firstName("John").surname("").line1("1 Tebbit Mews").postCode("GL52 2NF").accountNumber("12345678")
                .sortCode("123456").accountHolderName("Mr John Doe").query();
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testFirstLineMustBePassed() {
        String randInt = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        Post post = new Post(settings);
        String req = new Post.customer().email(randInt + "@test.com").title("Mr").customerReference(randInt)
                .firstName("John").surname("Doe").line1("").postCode("GL52 2NF").accountNumber("12345678")
                .sortCode("123456").accountHolderName("Mr John Doe").query();
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testPostCodeMustBePassed() {
        String randInt = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        Post post = new Post(settings);
        String req = new Post.customer().email(randInt + "@test.com").title("Mr").customerReference(randInt)
                .firstName("John").surname("Doe").line1("1 Tebbit Mews").postCode("").accountNumber("12345678")
                .sortCode("123456").accountHolderName("Mr John Doe").query();
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testAccountNumberMustBePassed() {
        String randInt = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        Post post = new Post(settings);
        String req = new Post.customer().email(randInt + "@test.com").title("Mr").customerReference(randInt)
                .firstName("John").surname("Doe").line1("1 Tebbit Mews").postCode("GL52 2NF").accountNumber("")
                .sortCode("123456").accountHolderName("Mr John Doe").query();
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testSortCodeMustBePassed() {
        String randInt = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        Post post = new Post(settings);
        String req = new Post.customer().email(randInt + "@test.com").title("Mr").customerReference(randInt)
                .firstName("John").surname("Doe").line1("1 Tebbit Mews").postCode("GL52 2NF").accountNumber("12345678")
                .sortCode("").accountHolderName("Mr John Doe").query();
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testAccountHolderNameMustBePassed() {
        String randInt = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        Post post = new Post(settings);
        String req = new Post.customer().email(randInt + "@test.com").title("Mr").customerReference(randInt)
                .firstName("John").surname("Doe").line1("1 Tebbit Mews").postCode("GL52 2NF").accountNumber("12345678")
                .sortCode("123456").accountHolderName("").query();
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

}
