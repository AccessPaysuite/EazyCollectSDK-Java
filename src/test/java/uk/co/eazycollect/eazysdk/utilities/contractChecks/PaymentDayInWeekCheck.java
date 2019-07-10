package uk.co.eazycollect.eazysdk.utilities.contractChecks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.utilities.ContractPostChecks;

public class PaymentDayInWeekCheck {
    private ContractPostChecks contractCheck;

    @BeforeEach
    public void testInit() {
        ClientHandler handler = new ClientHandler();
        contractCheck = new ContractPostChecks();
    }

    @Test
    public void testValidPaymentDayInWeek() throws InvalidParameterException {
        boolean paymentDayInWeek = contractCheck.checkPaymentDayInWeekIsValid("1");
        Assertions.assertTrue(paymentDayInWeek);
    }

    @Test
    public void testInvalidPaymentDayInWeekThrowsError() {
        Assertions.assertThrows(InvalidParameterException.class, () -> contractCheck.checkPaymentDayInWeekIsValid("6"));
    }

    @Test
    public void testStringNotAcceptable() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> contractCheck.checkPaymentDayInWeekIsValid("last day of month"));
    }

    @Test
    public void testMustBeInt() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> contractCheck.checkPaymentDayInWeekIsValid("1.01"));
    }

}
