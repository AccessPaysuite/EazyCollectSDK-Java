package uk.co.eazycollect.eazysdk.utilities.customerChecks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.utilities.CustomerPostChecks;

public class SortCodeCheck {
    private CustomerPostChecks check;

    @BeforeEach
    public void testInit() {
        check = new CustomerPostChecks();
    }

    @Test
    public void testAcceptSortCodeCorrectLength() throws InvalidParameterException {
        boolean result = check.checkSortCodeIsFormattedCorrectly("123456");
        Assertions.assertTrue(result);
    }

    @Test
    public void testAcceptsInvalidSortCodeCorrectLength() throws InvalidParameterException {
        boolean result = check.checkSortCodeIsFormattedCorrectly("000000");
        Assertions.assertTrue(result);
    }

    @Test
    public void testDoesNotAcceptAlphabeticCharactersInSortCode() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> check.checkSortCodeIsFormattedCorrectly("12345A"));
    }

    @Test
    public void testDoesNotAcceptHyphensInSortCode() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> check.checkSortCodeIsFormattedCorrectly("12-34-56"));
    }

    @Test
    public void testDoesNotAcceptTooSmallSortCode() {
        Assertions.assertThrows(InvalidParameterException.class, () -> check.checkSortCodeIsFormattedCorrectly("1234"));
    }

    @Test
    public void testDoesNotAcceptTooLongSortCode() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> check.checkSortCodeIsFormattedCorrectly("12345678"));
    }

}
