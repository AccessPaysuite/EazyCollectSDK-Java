package uk.co.eazycollect.eazysdk.contract;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.Post;
import uk.co.eazycollect.eazysdk.exceptions.EmptyRequiredParameterException;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.exceptions.InvalidStartDateException;
import uk.co.eazycollect.eazysdk.exceptions.ParameterNotAllowedException;
import uk.co.eazycollect.eazysdk.exceptions.ResourceNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

public class PostContract {
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
    public void testPostAdHocContract() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("adhoc_monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostAdHocContractAutoFixStartDate() {
        settings.setProperty("contracts.AutoFixStartDate", "true");
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("adhoc_monthly_free").startDate("1990-07-15").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostAdHocContractAutoFixTerminationType() {
        settings.setProperty("contracts.AutoFixTerminationTypeAdHoc", "true");
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("adhoc_monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("End on exact date").atTheEnd("Switch to further notice").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostAdHocContractAutoFixAtTheEnd() {
        settings.setProperty("contracts.AutoFixAtTheEndAdHoc", "true");
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("adhoc_monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Expire").query();
    }

    @Test
    public void testPostAdHocContractGiftAidTrue() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("adhoc_monthly_free").startDate("2019-08-01").giftAid(true)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostAdHocContractAllowAdditionalRef() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("adhoc_monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice")
                .additionalReference("Test").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostAdHocContractInvalidCustomerException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba8")
                .scheduleName("adhoc_monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").query();
        Assertions.assertTrue(errorOutput.toString().contains("ResourceNotFoundException"));
    }

    @Test
    public void testPostAdHocContractInvalidStartDateException() {
        settings.setProperty("contracts.AutoFixStartDate", "false");
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("adhoc_monthly_free").startDate("1990-07-15").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidStartDateException"));
    }

    @Test
    public void testPostAdHobContractInvalidTerminationTypeException() {
        settings.setProperty("contracts.AutoFixTerminationTypeAdHoc", "false");
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("adhoc_monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Take certain number of debits").atTheEnd("Switch to further notice").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPostAdHocContractInvalidAtTheEndException() {
        settings.setProperty("contracts.AutoFixAtTheEndAdHoc", "false");
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("adhoc_monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Expire").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPostAdHocContractInitialAmountException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("adhoc_monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").initialAmount("10.00").query();
        Assertions.assertTrue(errorOutput.toString().contains("ParameterNotAllowedException"));
    }

    @Test
    public void testPostAdHobContractExtraInitialAmountException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("adhoc_monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice")
                .extraInitialAmount("10.00").query();
        Assertions.assertTrue(errorOutput.toString().contains("ParameterNotAllowedException"));
    }

    @Test
    public void testPostAdHocContractFinalAmountException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("adhoc_monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").finalAmount("10.00").query();
        Assertions.assertTrue(errorOutput.toString().contains("ParameterNotAllowedException"));
    }

    @Test
    public void testPostContractInvalidScheduleNameException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("not a schedule").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPostMonthlyContractUntilFurtherNotice() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("1").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostMonthlyContractTakeCertainNumberOfDebits() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Take certain number of debits").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("1").numberOfDebits("10").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostMonthlyContractEndOnExactDate() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("End On Exact Date").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("1").terminationDate("2019-10-10").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostMonthlyContractAutoFixStartDate() {
        settings.setProperty("contracts.AutoFixStartDate", "true");
        settings.setProperty("contracts.AutoFixPaymentDayInMonth", "true");
        settings.setProperty("contracts.AutoFixPaymentMonthInYear", "true");
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("monthly_free").startDate("1990-07-15").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("1").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostMonthlyContractAutoFixPaymentDayInMonth() {
        settings.setProperty("contracts.AutoFixPaymentDayInMonth", "true");
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("1").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostMonthlyContractInitialAmount() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("1").initialAmount("10.00").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostMonthlyContractExtraInitialAmount() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("1").extraInitialAmount("10.00").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostMonthlyContractFinalAmount() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("1").finalAmount("10.00").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostMonthlyContractAdditionalReference() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("1").additionalReference("A Reference").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostMonthlyContractInvalidCustomerException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19db")
                .scheduleName("monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("1").query();
        Assertions.assertTrue(errorOutput.toString().contains("ResourceNotFoundException"));
    }

    @Test
    public void testPostMonthlyContractInvalidScheduleException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("not a schedule").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("15").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPostMonthlyContractInvalidStartDateException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("monthly_free").startDate("1990-07-15").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("15").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidStartDateException"));
    }

    @Test
    public void testPostMonthlyContractInvalidPaymentDayInMonthException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("15").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPostMonthlyContractFrequencyMustBePassedException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").paymentAmount("10.00")
                .paymentDayInMonth("1").query();
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testPostMonthlyContractPaymentAmountMustBePassedException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").frequency("1")
                .paymentDayInMonth("1").query();
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testPostMonthlyContractIfCertainNumberOfDebitsNumberOfDebitsMustBePassedException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Take certain number of debits").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("1").query();
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testPostMonthlyContractIfEndOnExactDateTerminationDateMustBePassedException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("End On Exact Date").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("1").query();
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testPostMonthlyContractIfEndOnExactDateTerminationDateMustNotBeSoonerThanStartDateException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("monthly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("End On Exact Date").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("1").terminationDate("1990-01-01").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPostAnnualContractUntilFurtherNotice() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f").scheduleName("annual_free")
                .startDate("2019-08-01").giftAid(false).terminationType("Until further notice")
                .atTheEnd("Switch to further notice").frequency("1").paymentAmount("10.00").paymentDayInMonth("1")
                .paymentMonthInYear("8").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostAnnualContractTakeCertainNumberOfDebits() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f").scheduleName("annual_free")
                .startDate("2019-08-01").giftAid(false).terminationType("Take certain number of debits")
                .atTheEnd("Switch to further notice").frequency("1").paymentAmount("10.00").paymentDayInMonth("1")
                .paymentMonthInYear("8").numberOfDebits("10").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostAnnualContractEndOnExactDate() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f").scheduleName("annual_free")
                .startDate("2019-08-01").giftAid(false).terminationType("End on exact date")
                .atTheEnd("Switch to further notice").frequency("1").paymentAmount("10.00").paymentDayInMonth("1")
                .paymentMonthInYear("8").terminationDate("2020-01-01").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostAnnualContractAutoFixStartDate() {
        settings.setProperty("contracts.AutoFixStartDate", "true");
        settings.setProperty("contracts.AutoFixPaymentDayInMonth", "true");
        settings.setProperty("contracts.AutoFixPaymentMonthInYear", "true");
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f").scheduleName("annual_free")
                .startDate("1990-07-15").giftAid(false).terminationType("Until further notice")
                .atTheEnd("Switch to further notice").frequency("1").paymentAmount("10.00").paymentDayInMonth("1")
                .paymentMonthInYear("8").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostAnnualContractAutoFixPaymentDayInMonth() {
        settings.setProperty("contracts.AutoFixPaymentDayInMonth", "true");
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f").scheduleName("annual_free")
                .startDate("2019-08-01").giftAid(false).terminationType("Until further notice")
                .atTheEnd("Switch to further notice").frequency("1").paymentAmount("10.00").paymentDayInMonth("1")
                .paymentMonthInYear("8").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostAnnualContractAutoFixPaymentMonthInYear() {
        settings.setProperty("contracts.AutoFixPaymentMonthInYear", "true");
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f").scheduleName("annual_free")
                .startDate("2019-08-01").giftAid(false).terminationType("Until further notice")
                .atTheEnd("Switch to further notice").frequency("1").paymentAmount("10.00").paymentDayInMonth("1")
                .paymentMonthInYear("1").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostAnnualContractInitialAmount() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f").scheduleName("annual_free")
                .startDate("2019-08-01").giftAid(false).terminationType("Until further notice")
                .atTheEnd("Switch to further notice").frequency("1").paymentAmount("10.00").paymentDayInMonth("1")
                .paymentMonthInYear("8").initialAmount("10.00").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostAnnualContractExtraInitialAmount() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f").scheduleName("annual_free")
                .startDate("2019-08-01").giftAid(false).terminationType("Until further notice")
                .atTheEnd("Switch to further notice").frequency("1").paymentAmount("10.00").paymentDayInMonth("1")
                .paymentMonthInYear("8").extraInitialAmount("10.00").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostAnnualContractFinalAmount() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f").scheduleName("annual_free")
                .startDate("2019-08-01").giftAid(false).terminationType("Until further notice")
                .atTheEnd("Switch to further notice").frequency("1").paymentAmount("10.00").paymentDayInMonth("1")
                .paymentMonthInYear("8").finalAmount("10.00").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostAnnualContractAdditionalReference() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f").scheduleName("annual_free")
                .startDate("2019-08-01").giftAid(false).terminationType("Until further notice")
                .atTheEnd("Switch to further notice").frequency("1").paymentAmount("10.00").paymentDayInMonth("1")
                .paymentMonthInYear("8").additionalReference("A Reference").query();
        Assertions.assertTrue(req.contains("DirectDebitRef"));
    }

    @Test
    public void testPostAnnualContractInvalidCustomerException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dbaf").scheduleName("annual_free")
                .startDate("2019-08-01").giftAid(false).terminationType("Until further notice")
                .atTheEnd("Switch to further notice").frequency("1").paymentAmount("10.00").paymentDayInMonth("1")
                .paymentMonthInYear("8").query();
        Assertions.assertTrue(errorOutput.toString().contains("ResourceNotFoundException"));
    }

    @Test
    public void testPostAnnualContractInvalidStartDateException() {
        settings.setProperty("contracts.AutoFixStartDate", "false");
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("annual_free").startDate("1990-07-15").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("1").paymentMonthInYear("8").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidStartDateException"));
    }

    @Test
    public void testPostAnnualContractInvalidPaymentMonthInYearException() {
        settings.setProperty("contracts.AutoFixPaymentMonthInYear", "false");
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("annual_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("1").paymentMonthInYear("1").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPostAnnualContractPaymentAmountMustBePassedException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("annual_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").frequency("1")
                .paymentDayInMonth("1").paymentMonthInYear("8").query();
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testPostAnnualContractFrequencyMustBePassedException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("annual_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").paymentAmount("10.00")
                .paymentDayInMonth("1").paymentMonthInYear("8").query();
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testPostAnnualContractIfEndOnExactDateTerminationDateMustBePassed() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("annual_free").startDate("2019-08-01").giftAid(false)
                .terminationType("End on exact date").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("1").paymentMonthInYear("8").query();
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testPostAnnualContractIfEndOnExactDateTerminationDateMustNotBeSoonerThanStartDate() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("annual_free").startDate("2019-08-01").giftAid(false)
                .terminationType("End on exact date").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInMonth("1").paymentMonthInYear("8").terminationDate("1990-01-01").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPostWeeklyContractInvalidCustomerException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba8")
                .scheduleName("weekly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInWeek("Monday").additionalReference("A Reference").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPostWeeklyContractInvalidStartDateException() {
        settings.setProperty("contracts.AutoFixStartDate", "false");
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("weekly_free").startDate("1990-07-15").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInWeek("Monday").additionalReference("A Reference").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidStartDateException"));
    }

    @Test
    public void testPostWeeklyContractInvalidPaymentDayInWeekException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("weekly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInWeek("Sunday").additionalReference("A Reference").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPostWeeklyContractFrequencyMustBePassedException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("weekly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").paymentAmount("10.00")
                .paymentDayInWeek("Monday").additionalReference("A Reference").query();
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testPostWeeklyContractPaymentAmountMustBePassedException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("weekly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Until further notice").atTheEnd("Switch to further notice").frequency("1")
                .paymentDayInWeek("Monday").additionalReference("A Reference").query();
        Assertions.assertTrue(errorOutput.toString().contains("EmptyRequiredParameterException"));
    }

    @Test
    public void testPostWeeklyContractIfCertainNumberOfDebitsNumberOfDebitsMustBePassedException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("weekly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("Take certain number of debits").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInWeek("Monday").additionalReference("A Reference").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPostWeeklyContractIfEndOnExactDateTerminationDateMustBePassedException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("weekly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("End on exact date").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInWeek("Monday").additionalReference("A Reference").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testPostWeeklyContractIfEndOnExactDateTerminationDateMustBeLaterThanStartDateException() {
        Post post = new Post(settings);
        String req = new Post.contract().customer("310a826b-d095-48e7-a55a-19dba82c566f")
                .scheduleName("weekly_free").startDate("2019-08-01").giftAid(false)
                .terminationType("End of exact date").atTheEnd("Switch to further notice").frequency("1")
                .paymentAmount("10.00").paymentDayInWeek("Monday").additionalReference("A Reference")
                .terminationDate("1990-01-01").query();
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    /*
     * It is a known issue that non-adhoc weekly contracts are not functioning correctly.
     */

}
