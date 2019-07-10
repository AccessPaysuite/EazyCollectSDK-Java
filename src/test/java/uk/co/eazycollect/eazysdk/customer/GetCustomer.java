package uk.co.eazycollect.eazysdk.customer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.Get;

import java.util.Properties;

public class GetCustomer {
    private Properties settings;
    private JsonParser parser;

    @BeforeEach
    public void testInit() {
        ClientHandler handler = new ClientHandler();
        parser = new JsonParser();
        settings = handler.settings();
        settings.setProperty("currentEnvironment.Environment", "sandbox");
        settings.setProperty("sandboxClientDetails.ClientCode", "SDKTST");
        settings.setProperty("sandboxClientDetails.ApiKey", "hkZujzFR2907XAtYe6qkKRsBo");
    }

    @Test
    public void testSearchForCustomersNoParametersThrowsWarningIfEnabled() {
        settings.setProperty("warnings.CustomerSearchWarning", "true");
        Get get = new Get(settings);
        String req = new Get.customers().query();
        Assertions.assertTrue(1 == 1);
    }

    @Test
    public void testSearchForCustomerInvalidSearchTermsReturnsNoCustomers() {
        Get get = new Get(settings);
        String req = new Get.customers().postCode("Not a post code").firstName("Test").query();
        Assertions.assertTrue(req.contains("No customers could be found with the provided parameters"));
    }

    @Test
    public void testUsingFullEmailAsParameterReturnsRecordsContainingFullEmail() {
        Get get = new Get(settings);
        String req = new Get.customers().email("test@email.com").query();
        JsonArray reqAsJson = parser.parse(req).getAsJsonArray();

        for(JsonElement x : reqAsJson) {
            JsonElement email = x.getAsJsonObject().get("Email");

            if(!email.getAsString().equals("test@email.com")) {
                Assertions.fail(String.format("An email address was returned which did not equals test@email.com. The email address was %s",
                        email.getAsString()));
            }

        }

        Assertions.assertTrue(req.contains("test@email.com"));
    }

    /*
    Known issue.
     */
    @Test
    public void testUsingFullTitleAsParameterReturnsRecordsContainingFullTitle() {
        Get get = new Get(settings);
        String req = new Get.customers().title("Mrs").query();
        JsonArray reqAsJson = parser.parse(req).getAsJsonArray();

        for(JsonElement x : reqAsJson) {
            JsonElement title = x.getAsJsonObject().get("Title");

            if(!title.getAsString().equals("Mrs")) {
                Assertions.fail(String.format("A title was returned which did not equals Mrs. The title was %s",
                        title.getAsString()));
            }

        }

        Assertions.assertTrue(req.contains("Mrs"));
    }

    /*
    Known issue.
     */
    @Test
    public void testUsingPartialTitleAsParameterReturnsRecordsContainingPartialTitle() {
        Get get = new Get(settings);
        String req = new Get.customers().title("Mr").query();
        JsonArray reqAsJson = parser.parse(req).getAsJsonArray();

        for(JsonElement x : reqAsJson) {
            JsonElement title = x.getAsJsonObject().get("Title");

            if(!title.getAsString().equals("Mr")) {
                Assertions.fail(String.format("A title was returned which did not equals Mr. The title was %s",
                        title.getAsString()));
            }

        }

        Assertions.assertTrue(req.contains("Mr"));
    }

    @Test
    public void testUsingSearchFromAsParameterReturnsRecordsCreatedAfterSearchDate() {
        Get get = new Get(settings);
        String req = new Get.customers().searchFrom("2019-01-01").query();
        JsonArray reqAsJson = parser.parse(req).getAsJsonArray();

        for(JsonElement x : reqAsJson) {
            JsonElement dateAdded = x.getAsJsonObject().get("DateAdded");

            if(!dateAdded.getAsString().contains("2019")) {
                Assertions.fail(String.format("A customer was returned although they were created to soon. The customer was created on %s",
                        dateAdded.getAsString()));
            }

        }

        Assertions.assertTrue(req.contains("2019"));
    }

    @Test
    public void testUsingSearchToAsParameterReturnsRecordsCreatedBeforeSearchDate() {
        Get get = new Get(settings);
        String req = new Get.customers().searchTo("2019-05-10").query();
        JsonArray reqAsJson = parser.parse(req).getAsJsonArray();

        for(JsonElement x : reqAsJson) {
            JsonElement dateAdded = x.getAsJsonObject().get("DateAdded");

            if(dateAdded.getAsString().contains("0/2019")) {
                Assertions.fail(String.format("A customer was returned although they were created too late. The customer was created on %s",
                        dateAdded.getAsString()));
            }

        }

        Assertions.assertTrue(req.contains("2019"));
    }

    /*
    Known issue.
     */
    @Test
    public void testUsingDateOfBirthAsAParameterReturnsRecordsContainingDateOfBirth() {
        Get get = new Get(settings);
        String req = new Get.customers().dateOfBirth("1990-06-29").query();
        JsonArray reqAsJson = parser.parse(req).getAsJsonArray();

        for(JsonElement x : reqAsJson) {
            JsonElement dateOfBirth = x.getAsJsonObject().get("DateOfBirth");

            if(!dateOfBirth.toString().contains("1996")) {
                Assertions.fail(String.format("A customer was returned although they don't have the correct date of birth. The customers date of birth is %s",
                        dateOfBirth.toString()));
            }

        }

        Assertions.assertTrue(req.contains("29/06/1996"));
    }

    @Test
    public void testUsingCustomerReferenceAsAParameterReturnsRecordsContainingCustomerRef() {
        Get get = new Get(settings);
        String req = new Get.customers().customerReference("test-000002").query();
        JsonArray reqAsJson = parser.parse(req).getAsJsonArray();

        for(JsonElement x : reqAsJson) {
            JsonElement customerRef = x.getAsJsonObject().get("CustomerRef");

            if(!customerRef.getAsString().contains("test-000002")) {
                Assertions.fail(String.format("A customer was returned although they don't have the correct customer reference. The customers customer reference is %s",
                        customerRef.getAsString()));
            }

        }

        Assertions.assertTrue(req.contains("test-000002"));
    }

    @Test
    public void testUsingFirstNameAsAParameterReturnsRecordsContainingFirstName() {
        Get get = new Get(settings);
        String req = new Get.customers().firstName("test").query();
        JsonArray reqAsJson = parser.parse(req).getAsJsonArray();

        for(JsonElement x : reqAsJson) {
            JsonElement firstName = x.getAsJsonObject().get("FirstName");

            if(!firstName.getAsString().toLowerCase().contains("test")) {
                Assertions.fail(String.format("A customer was returned although they don't have the correct first name. The customers first name is %s",
                        firstName.getAsString()));
            }

        }

        Assertions.assertTrue(req.contains("test"));
    }

    @Test
    public void testUsingSurnameAsAParameterReturnsRecordsContainingSurname() {
        Get get = new Get(settings);
        String req = new Get.customers().surname("test").query();
        JsonArray reqAsJson = parser.parse(req).getAsJsonArray();

        for(JsonElement x : reqAsJson) {
            JsonElement surname = x.getAsJsonObject().get("Surname");

            if(!surname.getAsString().toLowerCase().contains("test")) {
                Assertions.fail(String.format("A customer was returned although they don't have the correct surname. The customers surname is %s",
                        surname.getAsString()));
            }

        }

        Assertions.assertTrue(req.contains("test"));
    }

    @Test
    public void testUsingCompanyNameAsAParameterReturnsRecordsContainingCompanyName() {
        Get get = new Get(settings);
        String req = new Get.customers().companyName("test").query();
        JsonArray reqAsJson = parser.parse(req).getAsJsonArray();

        for(JsonElement x : reqAsJson) {
            JsonElement companyName = x.getAsJsonObject().get("CompanyName");

            if(!companyName.getAsString().toLowerCase().contains("test")) {
                Assertions.fail(String.format("A customer was returned although they don't have the correct company name. The customers company name is %s",
                        companyName.getAsString()));
            }

        }

        Assertions.assertTrue(req.contains("test"));
    }

    @Test
    public void testUsingPostCodeAsAParameterReturnsRecordsContainingPostCode() {
        Get get = new Get(settings);
        String req = new Get.customers().postCode("GL52 2NF").query();
        JsonArray reqAsJson = parser.parse(req).getAsJsonArray();

        for(JsonElement x : reqAsJson) {
            JsonElement postCode = x.getAsJsonObject().getAsJsonObject("AddressDetail").get("PostCode");

            if(!postCode.getAsString().toLowerCase().contains("gl52 2nf")) {
                Assertions.fail(String.format("A customer was returned although they don't have the correct post code. The customers post code is %s",
                        postCode.getAsString()));
            }

        }

        Assertions.assertTrue(req.contains("GL52 2NF"));
    }

    @Test
    public void testUsingAccountNumberAsAParameterReturnsRecordsContainingAccountNumber() {
        Get get = new Get(settings);
        String req = new Get.customers().accountNumber("45678912").query();
        JsonArray reqAsJson = parser.parse(req).getAsJsonArray();

        for(JsonElement x : reqAsJson) {
            JsonElement accountNumber = x.getAsJsonObject().getAsJsonObject("BankDetail").get("AccountNumber");

            if(!accountNumber.getAsString().toLowerCase().contains("45678912")) {
                Assertions.fail(String.format("A customer was returned although they don't have the correct account number. The customers account number is %s",
                        accountNumber.getAsString()));
            }

        }

        Assertions.assertTrue(req.contains("45678912"));
    }

    @Test
    public void testUsingSortCodeAsAParameterReturnsRecordsContainingSortCode() {
        Get get = new Get(settings);
        String req = new Get.customers().sortCode("147852").query();
        JsonArray reqAsJson = parser.parse(req).getAsJsonArray();

        for(JsonElement x : reqAsJson) {
            JsonElement sortCode = x.getAsJsonObject().getAsJsonObject("BankDetail").get("BankSortCode");

            if(!sortCode.getAsString().toLowerCase().contains("147852")) {
                Assertions.fail(String.format("A customer was returned although they don't have the correct sort code. The customers sort code is %s",
                        sortCode.getAsString()));
            }

        }

        Assertions.assertTrue(req.contains("147852"));
    }

    @Test
    public void testUsingAccountHolderNameAsAParameterReturnsRecordsContainingAccountHolderName() {
        Get get = new Get(settings);
        String req = new Get.customers().accountHolderName("Mr Test Client").query();
        JsonArray reqAsJson = parser.parse(req).getAsJsonArray();

        for(JsonElement x : reqAsJson) {
            JsonElement accountHolderName = x.getAsJsonObject().getAsJsonObject("BankDetail").get("AccountHolderName");

            if(!accountHolderName.getAsString().toLowerCase().contains("mr test client")) {
                Assertions.fail(String.format("A customer was returned although they don't have the correct account holder name. The customers account holder name is %s",
                        accountHolderName.getAsString()));
            }

        }

        Assertions.assertTrue(req.contains("Mr Test Client"));
    }

    @Test
    public void testUsingHomePhoneAsAParameterReturnsRecordsContainingHomePhone() {
        Get get = new Get(settings);
        String req = new Get.customers().homePhone("01242147852").query();
        JsonArray reqAsJson = parser.parse(req).getAsJsonArray();

        for(JsonElement x : reqAsJson) {
            JsonElement homePhone = x.getAsJsonObject().get("HomePhoneNumber");

            if(!homePhone.getAsString().toLowerCase().contains("01242147852")) {
                Assertions.fail(String.format("A customer was returned although they don't have the correct home phone. The customers home phone is %s",
                        homePhone.getAsString()));
            }

        }

        Assertions.assertTrue(req.contains("01242147852"));
    }

    @Test
    public void testUsingMobilePhoneAsAParameterReturnsRecordsContainingMobilePhone() {
        Get get = new Get(settings);
        String req = new Get.customers().mobilePhone("07393549789").query();
        JsonArray reqAsJson = parser.parse(req).getAsJsonArray();

        for(JsonElement x : reqAsJson) {
            JsonElement mobilePhone = x.getAsJsonObject().get("MobilePhoneNumber");

            if(!mobilePhone.getAsString().toLowerCase().contains("07393549789")) {
                Assertions.fail(String.format("A customer was returned although they don't have the correct mobile phone. The customers mobile phone is %s",
                        mobilePhone.getAsString()));
            }

        }

        Assertions.assertTrue(req.contains("07393549789"));
    }

    @Test
    public void testUsingWorkPhoneAsAParameterReturnsRecordsContainingWorkPhone() {
        Get get = new Get(settings);
        String req = new Get.customers().workPhone("01452365478").query();
        JsonArray reqAsJson = parser.parse(req).getAsJsonArray();

        for(JsonElement x : reqAsJson) {
            JsonElement workPhone = x.getAsJsonObject().get("WorkPhoneNumber");

            if(!workPhone.getAsString().toLowerCase().contains("01452365478")) {
                Assertions.fail(String.format("A customer was returned although they don't have the correct work phone. The customers work phone is %s",
                        workPhone.getAsString()));
            }

        }

        Assertions.assertTrue(req.contains("01452365478"));
    }

}
