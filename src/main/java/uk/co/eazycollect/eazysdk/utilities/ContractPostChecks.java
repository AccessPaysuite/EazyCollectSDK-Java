package uk.co.eazycollect.eazysdk.utilities;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import uk.co.eazycollect.eazysdk.exceptions.InvalidIOConfiguration;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.exceptions.InvalidSettingsConfigurationException;
import uk.co.eazycollect.eazysdk.exceptions.InvalidStartDateException;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ContractPostChecks {

    /**
     * Read a file containing available schedule names and return an error if the given schedule could not be found
     * @param scheduleName A schedule name provided by an external function
     * @param settings Settings configuration used for making a call to EazyCustomerManager
     * @return bool
     */
    public boolean checkScheduleNameIsAvailable(String scheduleName, Properties settings) {

        try {
            SchedulesReader reader = new SchedulesReader();
            JsonObject rootJson = reader.readSchedulesFile(settings);
            JsonObject schedulesJson = rootJson.getAsJsonObject("Schedules");
            List<String> scheduleNamesList = new ArrayList<>();

            for(String property : schedulesJson.keySet()) {
                scheduleNamesList.add(property.toLowerCase());
            }

            if(scheduleNamesList.contains(scheduleName)) {
                return true;
            } else {
                throw new InvalidParameterException(String.format("%s is not a valid schedule name. The available schedule names are: %s",
                        scheduleName, String.join(", ", scheduleNamesList)));
            }

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Check that the TerminationType provided is valid, and return an integer to be used by other functions
     * @param terminationType A TerminationType provided by an external function
     * @return int(0-2)
     */
    public int checkTerminationTypeIsValid(String terminationType) throws InvalidParameterException {
        Map<String, Integer> validTerminationTypes = new HashMap<>();
        validTerminationTypes.put("take certain number of debits", 0);
        validTerminationTypes.put("until further notice", 1);
        validTerminationTypes.put("end on exact date", 2);

        if(!validTerminationTypes.containsKey(terminationType.toLowerCase())) {
            throw new InvalidParameterException(String.format("%s is not a valid TerminationType. The available TerminationTypes are: %s",
                    terminationType, String.join(", ", validTerminationTypes.keySet())));
        } else {
            return validTerminationTypes.get(terminationType.toLowerCase());
        }

    }

    /**
     * Check that the AtTheEnd provided is valid, and return an integer to be used by other functions
     * @param atTheEnd A TerminationType provided by an external function
     * @return int(0-1)
     */
    public int checkAtTheEndIsValid(String atTheEnd) throws InvalidParameterException {
        Map<String, Integer> validAtTheEnds = new HashMap<>();
        validAtTheEnds.put("expire", 0);
        validAtTheEnds.put("switch to further notice", 1);

        if(!validAtTheEnds.containsKey(atTheEnd.toLowerCase())) {
            throw new InvalidParameterException(String.format("%s is not a valid AtTheEnd. The available AtTheEnds are: %s",
                    atTheEnd, String.join(", ", validAtTheEnds.keySet())));
        } else {
            return validAtTheEnds.get(atTheEnd.toLowerCase());
        }

    }

    /**
     * Check that the PaymentDayInWeek provided is valid, and return a bool if it is.
     * @param paymentDayInWeek A PaymentDayInWeek provided by an external function
     * @return bool
     */
    public boolean checkPaymentDayInWeekIsValid(String paymentDayInWeek) throws InvalidParameterException {
        List<String> validPaymentDayInWeeks = Arrays.asList("1", "2", "3", "4", "5");

        if(!validPaymentDayInWeeks.contains(paymentDayInWeek.toLowerCase())) {
            throw new InvalidParameterException(String.format("%s is not a valid PaymentDayInWeek. The available PaymentDayInWeeks are: Monday, Tuesday, Wednesday, Thursday and Friday.",
                    paymentDayInWeek));
        } else {
            return true;
        }

    }

    /**
     * Check that the PaymentDayInMonth provided is valid, and return a bool if it is.
     * @param paymentDayInMonth A PaymentDayInMonth provided by an external function
     * @return bool
     */
    public boolean checkPaymentDayInMonthIsValid(String paymentDayInMonth) throws InvalidParameterException {

        try {

            if(Integer.parseInt(paymentDayInMonth) >= 1 && Integer.parseInt(paymentDayInMonth) <= 28) {
                return true;
            } else {
                throw new InvalidParameterException(String.format("%s is not a valid PaymentDayInMonth. The PaymentDayInMonth must be between 1 and 28 or be set to 'last day of month'",
                        paymentDayInMonth));
            }

        } catch(NumberFormatException e) {

            if(paymentDayInMonth.toLowerCase().equals("last day of month")) {
                return true;
            } else {
                throw new InvalidParameterException(String.format("%s is not a valid PaymentDayInMonth. The PaymentDayInMonth must be between 1 and 28 or be set to 'last day of month'",
                        paymentDayInMonth));
            }

        }

    }

    /**
     * Check that the PaymentMonthInYear provided is valid, and return a bool if it is.
     * @param paymentMonthInYear A PaymentMonthInYear provided by an external function
     * @return bool
     */
    public boolean checkPaymentMonthInYearIsValid(String paymentMonthInYear) throws InvalidParameterException {

        try {

            if(Integer.parseInt(paymentMonthInYear) >= 1 && Integer.parseInt(paymentMonthInYear) <= 12) {
                return true;
            } else {
                throw new InvalidParameterException(String.format("%s is not a valid PaymentMonthInYear. The PaymentMonthInYear must be between 1 and 12.",
                        paymentMonthInYear));
            }

        } catch(NumberFormatException e) {
            throw new InvalidParameterException(String.format("%s is not a valid PaymentMonthInYear. The PaymentMonthInYear must be between 1 and 12.",
                    paymentMonthInYear));
        }

    }

    /**
     * Check that the NumberOfDebits provided is valid, and return a bool if it is.
     * @param numberOfDebits A NumberOfDebits provided by an external function
     * @return bool
     */
    public boolean checkNumberOfDebitsIsValid(String numberOfDebits) throws InvalidParameterException {

        try {

            if(Integer.parseInt(numberOfDebits) >= 1 && Integer.parseInt(numberOfDebits) <= 99) {
                return true;
            } else {
                throw new InvalidParameterException(String.format("%s is not a valid NumberOfDebits. The NumberOfDebits must be between 1 and 99.",
                        numberOfDebits));
            }

        } catch(Exception e) {
            throw new InvalidParameterException(String.format("%s is not a valid NumberOfDebits. The NumberOfDebits must be between 1 and 99.",
                    numberOfDebits));
        }

    }

    /**
     * Check that the TerminationDate provided is valid, and is later than the StartDate.
     * @param terminationDate A TerminationDate provided by an external function
     * @param startDate A StartDate provided by an external function
     * @return bool
     */
    public boolean checkTerminationDateIsAfterStartDate(String terminationDate, String startDate) {

        try {
            LocalDate termDate;
            LocalDate sDate;

            try {
                termDate = LocalDate.parse(terminationDate, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch(DateTimeParseException e) {

                try {
                    termDate = LocalDate.parse(terminationDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch(DateTimeParseException ex) {
                    throw new InvalidParameterException("The termination date is not valid a ISO date.", ex);
                }

            }

            try {
                sDate = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch(DateTimeParseException e) {

                try {
                    sDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch(DateTimeParseException ex) {
                    throw new InvalidParameterException("The start date is not a valid ISO date.", ex);
                }

            }

            if(termDate.isBefore(sDate) || termDate.isEqual(sDate)) {
                throw new InvalidParameterException(String.format("The Termination date of %s is too early. It must be after the date %s",
                        terminationDate, startDate));
            } else {
                return true;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Check whether or not the schedule is Ad-Hoc, and return true if it is.
     * @param scheduleName A ScheduleName provided by an external function
     * @param settings Properties provided by an external function
     * @return bool
     */
    public boolean checkScheduleAdHocStatus(String scheduleName, Properties settings) {

        try {
            SchedulesReader reader = new SchedulesReader();
            JsonObject rootJson = reader.readSchedulesFile(settings);
            JsonObject schedulesJson = rootJson.getAsJsonObject("Schedules");
            JsonObject schedule = schedulesJson.getAsJsonObject(scheduleName.toLowerCase());
            boolean adHoc;

            try {
                adHoc = Boolean.parseBoolean(schedule.get("ScheduleAdHoc").getAsString());
            } catch(NullPointerException e) {
                throw new InvalidParameterException("The schedule %s does not exist for the given client", scheduleName);
            }

            return adHoc;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Check that the StartDate provided is valid, and return a string
     * @param startDate A start date provided by an external function
     * @return string
     */
    public String checkStartDateIsValid(String startDate, Properties settings) {

        try {
            LocalDate initialDate;
            int initialProcessingDays;
            boolean autoFixStartDate;

            try {
                initialDate = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch(DateTimeParseException e) {

                try {
                    initialDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch(DateTimeParseException ex) {
                    throw new InvalidStartDateException(String.format("The start date %s is not valid. Please re-submit in ISO format (yyyy-mm-dd)", startDate));
                }

            }

            try {
                initialProcessingDays = Integer.parseInt(settings.getProperty("directDebitProcessingDays.InitialProcessingDays"));
            } catch(NumberFormatException e) {
                String invalidIPD = settings.getProperty("directDebitProcessingDays.InitialProcessingDays");
                throw new InvalidSettingsConfigurationException("The InitialProcessingDays setting is misconfigured. It was expecting an integer, and received '%s' instead", invalidIPD);
            }

            try {
                autoFixStartDate = Boolean.parseBoolean(settings.getProperty("contracts.AutoFixStartDate"));
            } catch(Exception e) {
                String invalidAFSD = settings.getProperty("contracts.AutoFixStartDate");
                throw new InvalidSettingsConfigurationException("The AutoFixStartDate setting is misconfigured. It was expecting a boolean, and received '%s' instead",
                        invalidAFSD);
            }

            CheckWorkingDays workingDays = new CheckWorkingDays();
            LocalDate firstAvailableDate = workingDays.checkWorkingDaysInFuture(initialProcessingDays);

            if(initialDate.isBefore(firstAvailableDate)) {

                if(autoFixStartDate) {
                    return firstAvailableDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
                } else {
                    throw new InvalidStartDateException(String.format("The start date of %s is too soon. The earliest possible start date for this contract is %s",
                            startDate, firstAvailableDate.format(DateTimeFormatter.ISO_LOCAL_DATE)));
                }

            } else {
                return initialDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            }

        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * Check that the Frequency provided is valid, and return a string.
     * @param scheduleName A schedule name provided by an external function
     * @return int (-1,2)
     */
    public int checkFrequency(String scheduleName, Properties settings) {

        try {
            SchedulesReader reader = new SchedulesReader();
            JsonObject schedulesList = reader.readSchedulesFile(settings);
            String frequency;

            try {
                JsonObject scheduleToken = schedulesList.getAsJsonObject("Schedules").getAsJsonObject(scheduleName);
                frequency = scheduleToken.get("ScheduleFrequency").getAsString();
            } catch(NullPointerException e) {
                throw new InvalidIOConfiguration("The schedule %s could not be found.", scheduleName);
            }

            switch(frequency) {
                case "Weekly":
                    return 0;
                case "Monthly":
                    return 1;
                case "Annually":
                    return 2;
                default:
                    return -1;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

}
