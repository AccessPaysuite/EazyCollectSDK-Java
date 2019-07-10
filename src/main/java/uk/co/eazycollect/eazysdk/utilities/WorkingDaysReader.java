package uk.co.eazycollect.eazysdk.utilities;

import org.apache.commons.io.FileUtils;
import uk.co.eazycollect.eazysdk.ClientHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class WorkingDaysReader {
    private ClientHandler handler;
    private Properties settings;
    private int updateDays;
    private static LocalDate date;
    private static List<String> workingDaysList;
    private static Iterable<String> fileLines;
    private static LocalDate lastUpdateDate;

    /**
     * Read the working days file and return a List object with the working days data. If the file needs updating, send the updating or it doesn't exist, write the file..
     * @return bank holidays list
     */
    public List<String> readWorkingDaysFile() throws IOException {
        handler = new ClientHandler();
        settings = handler.settings();
        updateDays = Integer.parseInt(settings.getProperty("other.BankHolidayUpdateDays"));

        if(!Files.exists(Paths.get(".\\includes")) || !Files.exists(Paths.get(".\\includes\\bankholidays.json"))) {
            WorkingDaysWriter writer = new WorkingDaysWriter();
            workingDaysList = writer.workingDayWriter();
            return workingDaysList;
        } else {
            workingDaysList = new ArrayList<>();
            fileLines = FileUtils.readLines(Paths.get(".\\includes\\bankholidays.json").toFile());
            lastUpdateDate = LocalDate.parse(fileLines.iterator().next());

            if(ChronoUnit.DAYS.between(lastUpdateDate, LocalDateTime.now()) >= updateDays) {
                WorkingDaysWriter writer = new WorkingDaysWriter();
                workingDaysList = writer.workingDayWriter();
                return workingDaysList;
            } else {

                for(String line : fileLines) {
                    workingDaysList.add(line);
                }

                workingDaysList.remove(0);
                return workingDaysList;
            }

        }

    }

}
