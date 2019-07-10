package uk.co.eazycollect.eazysdk.utilities.contractChecks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.utilities.ContractPostChecks;

public class TerminationTypeCheck {
    private ContractPostChecks contractCheck;

    @BeforeEach
    public void testInit() {
        ClientHandler handler = new ClientHandler();
        contractCheck = new ContractPostChecks();
    }

    @Test
    public void testTerminationTypeTakeCertainNumberOfDebitsReturns0() throws InvalidParameterException {
        int atTheEnd = contractCheck.checkTerminationTypeIsValid("take certain number of debits");
        Assertions.assertEquals(0, atTheEnd);
    }

    @Test
    public void testTerminationTypeUntilFurtherNoticeReturns1() throws InvalidParameterException {
        int atTheEnd = contractCheck.checkTerminationTypeIsValid("until further notice");
        Assertions.assertEquals(1, atTheEnd);
    }

    @Test
    public void testTerminationTypeEndOnExactDateReturns2() throws InvalidParameterException {
        int atTheEnd = contractCheck.checkTerminationTypeIsValid("end on exact date");
        Assertions.assertEquals(2, atTheEnd);
    }

    @Test
    public void testTerminationTypeIsNotCaseSensitive() throws InvalidParameterException {
        int atTheEnd = contractCheck.checkTerminationTypeIsValid("Until further notice");
        Assertions.assertEquals(1, atTheEnd);
    }

    @Test
    public void testTerminationTypeInvalidTypeThrowsError() {
        Assertions.assertThrows(InvalidParameterException.class,
                () -> contractCheck.checkTerminationTypeIsValid("not a termination type"));
    }

}
