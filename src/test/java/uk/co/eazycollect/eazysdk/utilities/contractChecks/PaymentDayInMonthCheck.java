package uk.co.eazycollect.eazysdk.utilities.contractChecks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.utilities.ContractPostChecks;

public class PaymentDayInMonthCheck {
    private ContractPostChecks contractCheck;

    @BeforeEach
    public void testInit() {
        ClientHandler handler = new ClientHandler();
        contractCheck = new ContractPostChecks();
    }

    @Test
    public void testValidPaymentDayInMonth() throws InvalidParameterException {
        boolean paymentDayInMonth = contractCheck.checkPaymentDayInMonthIsValid("1");
        Assertions.assertTrue(paymentDayInMonth);
    }

    @Test
    public void testLastDayOfMonthAcceptable() throws InvalidParameterException {
        boolean paymentDayInMonth = contractCheck.checkPaymentDayInMonthIsValid("last day of month");
        Assertions.assertTrue(paymentDayInMonth);
    }

    @Test
    public void testBelow1Invalid() {
        Assertions.assertThrows(InvalidParameterException.class, () -> contractCheck.checkPaymentDayInMonthIsValid("0"));
    }

    @Test
    public void testAbove28Invalid() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> contractCheck.checkPaymentDayInMonthIsValid("29"));
    }

    @Test
    public void testMustBeInt() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> contractCheck.checkPaymentDayInMonthIsValid("1.01"));
    }

    @Test
    public void testMustBeIntOrLastDayOfMonth() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> contractCheck.checkPaymentDayInMonthIsValid("first day of month"));
    }

}
