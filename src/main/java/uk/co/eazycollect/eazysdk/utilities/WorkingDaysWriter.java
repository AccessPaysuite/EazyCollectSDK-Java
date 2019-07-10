package uk.co.eazycollect.eazysdk.utilities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonToken;
import com.sun.corba.se.impl.ior.iiop.RequestPartitioningComponentImpl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class WorkingDaysWriter {
    private static String requestMessage;
    private static String responseMessage;
    private static InputStream responseStream;
    private static HttpClient httpClient;
    private static String requestUri;
    private static String responseAsString;
    private static InputStreamReader reader;
    private static JsonObject responseAsJson;
    private static List<String> datesList;
    private JsonParser parser;

    /**
     * Get the UK bank holidays from the Gov.UK API, write the result to a file and return a list of bank holidays
     * @return List of JSON dates
     */
    public List<String> workingDayWriter() throws IOException {
        requestUri = "https://www.gov.uk/bank-holidays.json";
        httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(new HttpGet(requestUri));
        responseStream = response.getEntity().getContent();
        reader = new InputStreamReader(responseStream);
        responseAsString = IOUtils.toString(responseStream);
        parser = new JsonParser();
        responseAsJson = parser.parse(responseAsString).getAsJsonObject();
        datesList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        for(JsonElement i : responseAsJson.get("england-and-wales").getAsJsonObject().get("events").getAsJsonArray()) {

            if(LocalDate.from(formatter.parse(i.getAsJsonObject().get("date").getAsString())).getYear() >= LocalDate.now().getYear()) {
                datesList.add(i.getAsJsonObject().get("date").getAsString());
            }

        }

        // We will use the first line as a time check when attempting to read the file
        datesList.add(0, formatter.format(LocalDate.now()));
        FileUtils.writeStringToFile(Paths.get(".\\includes\\bankholidays.json").toFile(),
                String.join(String.format("%n"), datesList));
        return datesList;
    }

}
