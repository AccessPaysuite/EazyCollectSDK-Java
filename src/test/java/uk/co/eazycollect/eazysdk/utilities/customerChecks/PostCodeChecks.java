package uk.co.eazycollect.eazysdk.utilities.customerChecks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.utilities.CustomerPostChecks;

public class PostCodeChecks {
    private CustomerPostChecks check;

    @BeforeEach
    public void testInit() {
        check = new CustomerPostChecks();
    }

    @Test
    public void testAcceptsValidPostCode() throws InvalidParameterException {
        boolean result = check.checkPostCodeIsCorrectlyFormatted("GL52 2NF");
        Assertions.assertTrue(result);
    }

    @Test
    public void testAcceptsInvalidPostCodeUsingCorrectFormat() throws InvalidParameterException {
        boolean result = check.checkPostCodeIsCorrectlyFormatted("GL51 6NC");
        Assertions.assertTrue(result);
    }

    @Test
    public void testAcceptsBFPOPostCodes() throws InvalidParameterException {
        boolean result = check.checkPostCodeIsCorrectlyFormatted("BF1 3AA");
        Assertions.assertTrue(result);
    }

    @Test
    public void testDoesNotAcceptInvalidUKPostCode() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> check.checkPostCodeIsCorrectlyFormatted("ZZ11 3ZZ"));
    }

    @Test
    public void testDoesNotAcceptNonUKPostCode() {
        Assertions.assertThrows(InvalidParameterException.class, () -> check.checkPostCodeIsCorrectlyFormatted("20500"));
    }

}
