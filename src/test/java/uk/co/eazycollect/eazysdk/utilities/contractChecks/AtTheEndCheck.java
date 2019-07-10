package uk.co.eazycollect.eazysdk.utilities.contractChecks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.utilities.ContractPostChecks;

public class AtTheEndCheck {
    private ContractPostChecks contractCheck;

    @BeforeEach
    public void testInit() {
        ClientHandler handler = new ClientHandler();
        contractCheck = new ContractPostChecks();
    }

    @Test
    public void testAtTheEndExpireReturns0() throws InvalidParameterException {
        int atTheEnd = contractCheck.checkAtTheEndIsValid("Expire");
        Assertions.assertEquals(0, atTheEnd);
    }

    @Test
    public void testAtTheEndSwitchToFurtherNoticeReturns1() throws InvalidParameterException {
        int atTheEnd = contractCheck.checkAtTheEndIsValid("Switch to further notice");
        Assertions.assertEquals(1, atTheEnd);
    }

    @Test
    public void testInvalidAtTheEndThrowsError() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> contractCheck.checkAtTheEndIsValid("not an at the end"));
    }

}
