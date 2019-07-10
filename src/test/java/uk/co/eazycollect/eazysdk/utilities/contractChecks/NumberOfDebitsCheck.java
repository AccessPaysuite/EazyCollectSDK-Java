package uk.co.eazycollect.eazysdk.utilities.contractChecks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.utilities.ContractPostChecks;

public class NumberOfDebitsCheck {
    private ContractPostChecks contractCheck;

    @BeforeEach
    public void testInit() {
        ClientHandler handler = new ClientHandler();
        contractCheck = new ContractPostChecks();
    }

    @Test
    public void testValidNumberOfDebits() throws InvalidParameterException {
        boolean numberOfDebits = contractCheck.checkNumberOfDebitsIsValid("49");
        Assertions.assertTrue(numberOfDebits);
    }

    @Test
    public void testNumberOfDebitsCannotBeBelow1() {
        Assertions.assertThrows(InvalidParameterException.class, () -> contractCheck.checkNumberOfDebitsIsValid("0"));
    }

    @Test
    public void testNumberOfDebitsCannotBeAbove99() {
        Assertions.assertThrows(InvalidParameterException.class, () -> contractCheck.checkNumberOfDebitsIsValid("100"));
    }

    @Test
    public void testNumberOfDebitsMustBeANumber() {
        Assertions.assertThrows(InvalidParameterException.class, () -> contractCheck.checkNumberOfDebitsIsValid("test"));
    }

}
