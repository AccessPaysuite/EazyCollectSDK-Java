package uk.co.eazycollect.eazysdk.utilities;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonToken;
import uk.co.eazycollect.eazysdk.Get;
import uk.co.eazycollect.eazysdk.models.Schedules;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class SchedulesWriter {
    private static String scheduleString;
    private static JsonObject schedulesJson;
    private JsonArray schedulesToken;
    private JsonToken servicesListToken;
    private JsonToken schedulesListToken;
    private Get get;
    private static JsonObject schedulesObject;
    private static JsonObject rootObject;
    private static JsonObject lastUpdatedObject;
    private static String environment;
    private JsonParser parser;

    /**
     * Get the available schedules from the Get().Schedules() command and return the schedules as a JSON object
     * @param settings A settings object passed on from the SchedulesReader
     * @return JSON schedules list
     */
    public JsonObject scheduleWriter(Properties settings) throws Exception {
        // We will use Get to call Get.Schedules()
        get = new Get(settings);
        scheduleString = get.schedules();
        // Parse SchedulesString into a Json object
        parser = new JsonParser();
        schedulesJson = parser.parse(scheduleString).getAsJsonObject();
        // Get a token of the Services property of the SchedulesJson
        schedulesToken = schedulesJson.get("Services").getAsJsonArray();
        // Create the objects to format the JSON to be written
        rootObject = new JsonObject();
        schedulesObject = new JsonObject();
        lastUpdatedObject = new JsonObject();

        // There can be multiple lists of services, though there is usually only one
        for(JsonElement servicesListToken : schedulesToken) {

            // Get the list of schedules within a given service
            for(JsonElement scheduleElement : servicesListToken.getAsJsonObject().getAsJsonArray("Schedules")) {
                // Build a schedule object
                JsonObject scheduleObject = scheduleElement.getAsJsonObject();
                Schedules.scheduleName = scheduleObject.get("Name").getAsString();
                Schedules.scheduleAdHoc = scheduleObject.toString().contains("AD-HOC Payments");
                Schedules.scheduleFrequency = scheduleObject.get("Frequency").getAsString();
                // We use a temp object to prepare the JObject. We could just add three entries to the Schedule object, but this ensures correct formatting
                JsonObject tempObject = new JsonObject();
                tempObject.addProperty("ScheduleAdHoc", Schedules.scheduleAdHoc);
                tempObject.addProperty("ScheduleFrequency", Schedules.scheduleFrequency);
                schedulesObject.add(Schedules.scheduleName, tempObject);
            }

        }

        rootObject.add("Schedules", schedulesObject);
        rootObject.addProperty("LastUpdated", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        environment = settings.getProperty("currentEnvironment.Environment").toLowerCase();

        if(!Files.exists(Paths.get(".\\includes"))) {
            Files.createDirectory(Paths.get(".\\includes"));
        }

        FileWriter writer = new FileWriter(Paths.get(".\\includes\\" + environment + "scheduleslist.json").toFile(), false);
        writer.write(rootObject.toString());
        writer.close();
        return rootObject;
    }

}
