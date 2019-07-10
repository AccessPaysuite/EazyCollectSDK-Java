package uk.co.eazycollect.eazysdk.utilities;

import uk.co.eazycollect.eazysdk.exceptions.InvalidSettingsConfigurationException;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CheckWorkingDays {
    private WorkingDaysReader reader;
    private List<String> bankHolidaysList;
    private int workingDays;
    private int calendarDays;
    private LocalDate dateToday;
    private LocalDate dateStart;
    private LocalDate workingDate;
    private LocalDate finalDate;
    private boolean startToday;

    public LocalDate checkWorkingDaysInFuture(int numberOfDays) throws InvalidSettingsConfigurationException, IOException {

        if(numberOfDays <= 0) {
            throw new InvalidSettingsConfigurationException("The number of days in the future must be above 0");
        }

        reader = new WorkingDaysReader();
        bankHolidaysList = reader.readWorkingDaysFile();
        workingDays = 0;
        calendarDays = 0;
        dateToday = LocalDate.now();
        dateStart = dateToday;

        // Check if today is a bank holiday or falls on a weekend
        if(dateToday.getDayOfWeek() == DayOfWeek.SATURDAY || dateToday.getDayOfWeek() == DayOfWeek.SUNDAY || bankHolidaysList.contains(dateStart.toString())) {
            int i = 0;

            while(dateToday.getDayOfWeek() != DayOfWeek.SATURDAY && dateToday.getDayOfWeek() != DayOfWeek.SUNDAY || bankHolidaysList.contains(dateStart.toString())) {
                i++;
                dateStart = dateToday.plusDays(i);
            }

        }

        while(workingDays <= (numberOfDays - 1)) {
            workingDate = dateStart.plusDays(calendarDays);

            if(workingDate.getDayOfWeek() != DayOfWeek.SATURDAY && workingDate.getDayOfWeek() != DayOfWeek.SUNDAY && !bankHolidaysList.contains(workingDate.toString())) {
                workingDays++;
            }

            calendarDays++;
        }

        finalDate = dateStart.plusDays(calendarDays);

        while(finalDate.getDayOfWeek() == DayOfWeek.SATURDAY || finalDate.getDayOfWeek() == DayOfWeek.SUNDAY || bankHolidaysList.contains(finalDate.toString())) {
            finalDate = finalDate.plusDays(1);
            calendarDays++;
        }

        return finalDate;
    }

}
