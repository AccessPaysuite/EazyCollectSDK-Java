package uk.co.eazycollect.eazysdk.utilities.customerChecks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.utilities.CustomerPostChecks;

public class AccountHolderNameCheck {
    private CustomerPostChecks check;

    @BeforeEach
    public void testInit() {
        check = new CustomerPostChecks();
    }

    @Test
    public void testAcceptValidAccountHolderName() throws InvalidParameterException {
        boolean result = check.checkAccountHolderNameIsFormattedCorrectly("Mr John Doe");
        Assertions.assertTrue(result);
    }

    @Test
    public void testAcceptsAccountHolderName18Digits() throws InvalidParameterException {
        boolean result = check.checkAccountHolderNameIsFormattedCorrectly("John Doe Test Test");
        Assertions.assertTrue(result);
    }

    @Test
    public void testAcceptsAccountHolderName3Digits() throws InvalidParameterException {
        boolean result = check.checkAccountHolderNameIsFormattedCorrectly("Joh");
        Assertions.assertTrue(result);
    }

    @Test
    public void testAcceptsSpecialCharactersAccountHolderName() throws InvalidParameterException {
        boolean result = check.checkAccountHolderNameIsFormattedCorrectly("John & Doe - A / B");
        Assertions.assertTrue(result);
    }

    @Test
    public void testDoesNotAcceptTooSmallAccountHolderName() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> check.checkAccountHolderNameIsFormattedCorrectly("Jo"));
    }

    @Test
    public void testDoesNotAcceptTooLongAccountHolderName() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> check.checkAccountHolderNameIsFormattedCorrectly("Too long account holder name"));
    }

    @Test
    public void testDoesNotAcceptSpecialCharactersAccountHolderName() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> check.checkAccountHolderNameIsFormattedCorrectly("Account!;@"));
    }

}
