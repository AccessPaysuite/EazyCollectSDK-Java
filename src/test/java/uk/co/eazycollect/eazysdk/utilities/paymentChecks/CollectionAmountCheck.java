package uk.co.eazycollect.eazysdk.utilities.paymentChecks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.utilities.PaymentPostChecks;

public class CollectionAmountCheck {
    private PaymentPostChecks paymentCheck;

    @BeforeEach
    public void testInit() {
        ClientHandler handler = new ClientHandler();
        paymentCheck = new PaymentPostChecks();
    }

    @Test
    public void testValidPaymentAmount() throws InvalidParameterException {
        boolean paymentAmount = paymentCheck.checkPaymentAmount("12.34");
        Assertions.assertTrue(paymentAmount);
    }

    @Test
    public void testValidPaymentAmountAsInt() throws InvalidParameterException {
        boolean paymentAmount = paymentCheck.checkPaymentAmount("12");
        Assertions.assertTrue(paymentAmount);
    }

    @Test
    public void testPassing0ThrowsError() {
        Assertions.assertThrows(InvalidParameterException.class, () -> paymentCheck.checkPaymentAmount("0.00"));
    }

    @Test
    public void testNegativeValueThrowsError() {
        Assertions.assertThrows(InvalidParameterException.class, () -> paymentCheck.checkPaymentAmount("-10.00"));
    }

    @Test
    public void testInvalidPaymentAmountThrowsError() {
        Assertions.assertThrows(InvalidParameterException.class, () -> paymentCheck.checkPaymentAmount("1.2.3"));
    }

    @Test
    public void testPassingNonNumericStringThrowsError() {
        Assertions.assertThrows(InvalidParameterException.class, () -> paymentCheck.checkPaymentAmount("one"));
    }

}
