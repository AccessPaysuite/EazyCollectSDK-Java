package uk.co.eazycollect.eazysdk.contract;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.eazycollect.eazysdk.ClientHandler;
import uk.co.eazycollect.eazysdk.Get;
import uk.co.eazycollect.eazysdk.exceptions.ResourceNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

public class GetContracts {
    private final ByteArrayOutputStream errorOutput = new ByteArrayOutputStream();
    private final PrintStream original = System.err;
    private Properties settings;
    private JsonParser parser;

    @BeforeEach
    public void testInit() {
        System.setErr(new PrintStream(errorOutput));
        ClientHandler handler = new ClientHandler();
        parser = new JsonParser();
        settings = handler.settings();
        settings.setProperty("currentEnvironment.Environment", "sandbox");
        settings.setProperty("sandboxClientDetails.ClientCode", "SDKTST");
        settings.setProperty("sandboxClientDetails.ApiKey", "hkZujzFR2907XAtYe6qkKRsBo");
    }

    @AfterEach
    public void reset() {
        System.setErr(original);
    }

    @Test
    public void testSearchingForACustomerWithContractsReturnsContracts() {
        Get get = new Get(settings);
        String req = get.contracts("310a826b-d095-48e7-a55a-19dba82c566f");
        JsonArray reqAsJson = parser.parse(req).getAsJsonArray();

        for(JsonElement x : reqAsJson) {
            JsonElement contractId = x.getAsJsonObject().get("Id");

            if(contractId.getAsString().equals("null")) {
                Assertions.fail("The customer does not own any contracts");
            }

        }

        Assertions.assertTrue(req.contains("Id"));
    }

    @Test
    public void testSearchingForACustomerWithNoContractsReturnsNoContracts() {
        Get get = new Get(settings);
        String req = get.contracts("7c1a60c5-af12-4477-a10b-a61770e312a5");
        Assertions.assertTrue(req.contains("No contracts could be associated with the customer"));
    }

    @Test
    public void testSearchingForAnInvalidCustomerReturnsAnError() {
        Get get = new Get(settings);
        String req = get.contracts("7c1a60c5-af12-4477-a10b-a617");
        Assertions.assertTrue(errorOutput.toString().contains("ResourceNotFoundException"));
    }

    @Test
    public void testSearchingForABlankCustomerReturnsAnError() {
        Get get = new Get(settings);
        String req = get.contracts("");
        Assertions.assertTrue(errorOutput.toString().contains("ResourceNotFoundException"));
    }

}
