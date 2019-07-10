package uk.co.eazycollect.eazysdk.utilities;

import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.exceptions.InvalidPaymentDateException;
import uk.co.eazycollect.eazysdk.exceptions.InvalidSettingsConfigurationException;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Properties;

public class PaymentPostChecks {

    /**
     * Check that the collection_date argument is a valid ISO date and is at least x working days in the future, where x is the predetermined OngoingProcessingDays setting.
     * Throw an error if this is not the case.
     * @param collectionDate A collection date provided by an external function
     * @param settings Settings configuration used for making a call to EazyCustomerManager
     * @return date formatted as string
     */
    public String checkPaymentDate(String collectionDate, Properties settings) {

        try {
            LocalDate initialDate;
            int ongoingProcessingDays;
            boolean autoFixStartDate;

            try {
                initialDate = LocalDate.parse(collectionDate, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch(DateTimeParseException e) {

                try {
                    initialDate = LocalDate.parse(collectionDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch(DateTimeParseException ex) {
                    throw new InvalidPaymentDateException(String.format("The payment date %s is not valid. Please re-submit in ISO format (yyyy-mm-dd)", collectionDate));
                }

            }

            try {
                ongoingProcessingDays = Integer.parseInt(settings.getProperty("directDebitProcessingDays.OngoingProcessingDays"));
            } catch(NumberFormatException e) {
                String invalidOPD = settings.getProperty("directDebitProcessingDays.OngoingProcessingDays");
                throw new InvalidSettingsConfigurationException();
            }

            try {
                autoFixStartDate = Boolean.parseBoolean(settings.getProperty("payments.AutoFixPaymentDate"));
            } catch(ClassCastException e) {
                String invalidAFSD = settings.getProperty("directDebitProcessingDays.AutoFixStartDate");
                throw new InvalidSettingsConfigurationException(String.format("The AutoFixStartDate requires a boolean. '%s' is not a valid value for this setting.", invalidAFSD));
            }

            CheckWorkingDays workingDays = new CheckWorkingDays();
            LocalDate firstAvailableDate = workingDays.checkWorkingDaysInFuture(ongoingProcessingDays);

            if(initialDate.isBefore(firstAvailableDate)) {

                if(autoFixStartDate) {
                    return firstAvailableDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
                } else {
                    throw new InvalidPaymentDateException(String.format("The payment date of %s is too soon. The earliest possible payment date is %s",
                            collectionDate, firstAvailableDate.format(DateTimeFormatter.ISO_LOCAL_DATE)));
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
     * Check whether or not is_credit is enabled for a client. This function does not check whether is_credit is enabled through EazyCustomerManager,
     * instead it checks the setting in AppSettings.json. Regardless of the setting, IsCredit will fail if it is  disallowed on EazyCustomerManager.
     * @param settings Settings configuration used for making a call to EazyCustomerManager
     * @return bool
     */
    public boolean checkIfCreditIsAllowed(Properties settings) {
        try {
            boolean isCreditAllowed;
            String setting = settings.getProperty("payments.IsCreditAllowed").toLowerCase();

            if(!setting.equals("true") && !setting.equals("false")) {
                String invalidCredit = settings.getProperty("payments.IsCreditAllowed");
                throw new InvalidSettingsConfigurationException(String.format("The isCreditAllowed setting is misconfigured. Its value is '%s', though it should be either TRUE or FALSE.",
                        invalidCredit));
            }

            isCreditAllowed = Boolean.parseBoolean(setting);
            return isCreditAllowed;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Check that the PaymentAmount is above 0.00. If this is not the case, throw an error.
     * @param paymentAmount A PaymentAmount provided by an external function
     * @return bool
     */
    public boolean checkPaymentAmount(String paymentAmount) throws InvalidParameterException {
        float fPaymentAmount;

        try {
            fPaymentAmount = Float.parseFloat(paymentAmount);
        } catch(NumberFormatException e) {
            throw new InvalidParameterException("The payment amount must be formatted as GBP currency, using x.xx formatting. " + paymentAmount + " does not conform to this formatting.");
        }

        if(fPaymentAmount >= 0.01) {
            return true;
        } else {
            throw new InvalidParameterException("The payment amount must be at least £0.01. £" + paymentAmount + " is too low.");
        }

    }

}
