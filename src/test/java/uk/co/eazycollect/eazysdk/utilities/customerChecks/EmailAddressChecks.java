package uk.co.eazycollect.eazysdk.utilities.customerChecks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.utilities.CustomerPostChecks;

public class EmailAddressChecks {
    private CustomerPostChecks check;

    @BeforeEach
    public void testInit() {
        check = new CustomerPostChecks();
    }

    @Test
    public void testAcceptsValidEmail() throws InvalidParameterException {
        boolean result = check.checkEmailAddressIsCorrectlyFormatted("test@email.com");
        Assertions.assertTrue(result);
    }

    @Test
    public void testAcceptsValidEmailWithSpecialCharacters() throws InvalidParameterException {
        boolean result = check.checkEmailAddressIsCorrectlyFormatted("te_s-t+123@email.com");
        Assertions.assertTrue(result);
    }

    @Test
    public void testDoesNotAcceptEmailWithoutAtSymbol() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> check.checkEmailAddressIsCorrectlyFormatted("testemail.com"));
    }

    @Test
    public void testDoesNotAcceptEmailWithoutTLDSeparator() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> check.checkEmailAddressIsCorrectlyFormatted("tes@temailcom"));
    }

}
