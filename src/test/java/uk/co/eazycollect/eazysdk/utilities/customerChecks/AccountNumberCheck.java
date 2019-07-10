package uk.co.eazycollect.eazysdk.utilities.customerChecks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.utilities.CustomerPostChecks;

public class AccountNumberCheck {
    private CustomerPostChecks check;

    @BeforeEach
    public void testInit() {
        check = new CustomerPostChecks();
    }

    @Test
    public void testAcceptAccountNumberCorrectLength() throws InvalidParameterException {
        boolean result = check.checkAccountNumberIsFormattedCorrectly("12345678");
        Assertions.assertTrue(result);
    }

    @Test
    public void testAcceptsInvalidAccountNumberCorrectLength() throws InvalidParameterException {
        boolean result = check.checkAccountNumberIsFormattedCorrectly("00000000");
        Assertions.assertTrue(result);
    }

    @Test
    public void testDoesNotAcceptAlphabeticCharactersInAccountNumber() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> check.checkAccountNumberIsFormattedCorrectly("1234567A"));
    }

    @Test
    public void testDoesNotAcceptSpecialCharactersInAccountNumber() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> check.checkAccountNumberIsFormattedCorrectly("12345-78"));
    }

    @Test
    public void testDoesNotAcceptTooSmallAccountNumber() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> check.checkAccountNumberIsFormattedCorrectly("123456"));
    }

    @Test
    public void testDoesNotAcceptTooLongAccountNumber() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> check.checkAccountNumberIsFormattedCorrectly("1234567890"));
    }

}
