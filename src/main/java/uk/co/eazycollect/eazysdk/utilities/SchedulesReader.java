package uk.co.eazycollect.eazysdk.utilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonToken;
import org.apache.commons.io.FileUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Properties;

public class SchedulesReader {
    private static String schedules;
    private static String environment;
    private static LocalDateTime date;
    private static JsonObject schedulesJson;
    private static JsonToken schedulesList;
    private static SchedulesWriter writer;
    private JsonParser parser;

    /**
     * Read the given schedules file and return a JSON object with the schedules data. If the file needs updating, send the JObject to the schedule writer.
     * @return schedules JObject
     */
    public JsonObject readSchedulesFile(Properties settings) throws Exception {
        environment = settings.getProperty("currentEnvironment.Environment").toLowerCase();

        if(!Files.exists(Paths.get(".\\includes")) || !Files.exists(Paths.get(".\\includes\\" + environment + "scheduleslist.json"))) {
            writer = new SchedulesWriter();
            schedulesJson = writer.scheduleWriter(settings);
            return schedulesJson;
        } else {

            try {
                parser = new JsonParser();
                schedulesJson = parser.parse(FileUtils.readFileToString(Paths.get(".\\includes\\" + environment + "scheduleslist.json").toFile())).getAsJsonObject();
                return schedulesJson;
            } catch(Exception e) {
                writer = new SchedulesWriter();
                schedulesJson = writer.scheduleWriter(settings);
                return schedulesJson;
            }

        }

    }

}
