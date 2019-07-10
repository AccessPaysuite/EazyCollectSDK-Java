package uk.co.eazycollect.eazysdk.utilities.schedulesCheck;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.utilities.SchedulesReader;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class SchedulesReaderCheck {
    private ClientHandler handler;
    private Properties settings;
    private SchedulesReader reader;

    @BeforeEach
    public void testInit() {
        handler = new ClientHandler();
        settings = handler.settings();
        reader = new SchedulesReader();
        settings.setProperty("currentEnvironment.Environment", "sandbox");
        settings.setProperty("sandboxClientDetails.ApiKey", "hkZujzFR2907XAtYe6qkKRsBo");
        settings.setProperty("sandboxClientDetails.ClientCode", "SDKTST");
    }

    @Test
    public void testReaderWillCreateFileIfNeeded() throws Exception {
        Files.deleteIfExists(Paths.get(".\\includes\\sandboxscheduleslist.json"));
        reader.readSchedulesFile(settings);
        Assertions.assertTrue(Files.exists(Paths.get(".\\includes\\sandboxscheduleslist.json")));
    }

    @Test
    public void testReaderWillRebuildFileIfFormattedIncorrectly() throws Exception {
        JsonObject x = reader.readSchedulesFile(settings);
        Assertions.assertTrue(x.has("LastUpdated"));
    }

}
