package uk.co.eazycollect.eazysdk.utilities.contractChecks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.utilities.ContractPostChecks;

public class PaymentMonthInYearCheck {
    private ContractPostChecks contractCheck;

    @BeforeEach
    public void testInit() {
        ClientHandler handler = new ClientHandler();
        contractCheck = new ContractPostChecks();
    }

    @Test
    public void testValidPaymentMonthInYear() throws InvalidParameterException {
        boolean paymentMonthInYear = contractCheck.checkPaymentMonthInYearIsValid("11");
        Assertions.assertTrue(paymentMonthInYear);
    }

    @Test
    public void testInvalidPaymentMonthInYearThrowsError() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> contractCheck.checkPaymentMonthInYearIsValid("13"));
    }

    @Test
    public void testStringNotAcceptable() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> contractCheck.checkPaymentMonthInYearIsValid("last month of year"));
    }

    @Test
    public void testMustBeInt() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> contractCheck.checkPaymentMonthInYearIsValid("1.01"));
    }

}
