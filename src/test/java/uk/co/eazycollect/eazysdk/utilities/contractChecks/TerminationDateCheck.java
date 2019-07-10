package uk.co.eazycollect.eazysdk.utilities.contractChecks;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.utilities.ContractPostChecks;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TerminationDateCheck {
    private final ByteArrayOutputStream errorOutput = new ByteArrayOutputStream();
    private final PrintStream original = System.err;
    private ContractPostChecks contractCheck;

    @BeforeEach
    public void testInit() {
        System.setErr(new PrintStream(errorOutput));
        ClientHandler handler = new ClientHandler();
        contractCheck = new ContractPostChecks();
    }

    @AfterEach
    public void reset() {
        System.setErr(original);
    }

    @Test
    public void testTerminationDateAfterStartDate() {
        boolean terminationDate = contractCheck.checkTerminationDateIsAfterStartDate("2019-08-15", "2019-08-01");
        Assertions.assertTrue(terminationDate);
    }

    @Test
    public void testAcceptsNonISODates() {
        boolean terminationDate = contractCheck.checkTerminationDateIsAfterStartDate("15/08/2019", "2019-08-01");
        Assertions.assertTrue(terminationDate);
    }

    @Test
    public void testTerminationDateOnSameDayThrowsError() {
        boolean terminationDate = contractCheck.checkTerminationDateIsAfterStartDate("2019-08-01", "2019-08-01");
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testTerminationDateTooSoonThrowsError() {
        boolean terminationDate = contractCheck.checkTerminationDateIsAfterStartDate("2019-06-15", "2019-08-01");
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

    @Test
    public void testInvalidTerminationDateThrowsError() {
        boolean terminationDate = contractCheck.checkTerminationDateIsAfterStartDate("2019-15-15", "2019-08-01");
        Assertions.assertTrue(errorOutput.toString().contains("InvalidParameterException"));
    }

}
