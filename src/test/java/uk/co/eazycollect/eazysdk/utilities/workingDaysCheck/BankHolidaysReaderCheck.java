package uk.co.eazycollect.eazysdk.utilities.workingDaysCheck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.utilities.WorkingDaysReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class BankHolidaysReaderCheck {
    private ClientHandler handler;
    private Properties settings;
    private WorkingDaysReader reader;

    @BeforeEach
    public void testInit() {
        handler = new ClientHandler();
        settings = handler.settings();
        reader = new WorkingDaysReader();
        settings.setProperty("currentEnvironment.Environment", "sandbox");
        settings.setProperty("sandboxClientDetails.ApiKey", "hkZujzFR2907XAtYe6qkKRsBo");
        settings.setProperty("sandboxClientDetails.ClientCode", "SDKTST");
    }

    @Test
    public void testReaderWillCreateFileIfNeeded() throws IOException {
        Files.deleteIfExists(Paths.get(".\\includes\\bankholidays.json"));
        reader.readWorkingDaysFile();
        Assertions.assertTrue(Files.exists(Paths.get(".\\includes\\bankholidays.json")));
    }

}
