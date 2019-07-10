package uk.co.eazycollect.eazysdk;

import org.apache.http.util.TextUtils;
import uk.co.eazycollect.eazysdk.exceptions.EmptyRequiredParameterException;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.exceptions.ParameterNotAllowedException;
import uk.co.eazycollect.eazysdk.exceptions.ResourceNotFoundException;
import uk.co.eazycollect.eazysdk.utilities.ContractPostChecks;
import uk.co.eazycollect.eazysdk.utilities.CustomerPostChecks;
import uk.co.eazycollect.eazysdk.utilities.PaymentPostChecks;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A collection of PATCH requests made to the EazyCustomerManager API
 */
public class Patch {
    // The client object which manages the Session and Settings
    private static ClientHandler handler;
    // The settings object, derives from ClientHandler
    private static Properties settings;
    private CustomerPostChecks customerChecks;
    private ContractPostChecks contractChecks;
    private PaymentPostChecks paymentChecks;
    // The parameters passed from a function, if there are any
    private Map<String, String> parameters;

    public Patch(Properties settings) {
        this.settings = settings;
        handler = new ClientHandler();
    }

    public static class customer {
        private String guid;
        private Map<String, String> parameters;
        private CustomerPostChecks customerChecks;
        private String email;
        private String postCode;
        private String accountNumber;
        private String sortCode;
        private String accountHolderName;
        private String title;
        private String firstName;
        private String surname;
        private String line1;
        private String line2;
        private String line3;
        private String line4;
        private String companyName;
        private String dateOfBirth;
        private String initials;
        private String homePhone;
        private String workPhone;
        private String mobilePhone;

        public customer(String customer) {
            guid = customer;
            parameters = new HashMap<>();
        }

        public customer email(String email) {
            this.email = email;
            return this;
        }

        public customer postCode(String postCode) {
            this.postCode = postCode;
            return this;
        }

        public customer accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public customer sortCode(String bankSortCode) {
            this.sortCode = bankSortCode;
            return this;
        }

        public customer accountHolderName(String accountHolderName) {
            this.accountHolderName = accountHolderName;
            return this;
        }

        public customer title(String title) {
            this.title = title;
            return this;
        }

        public customer firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public customer surname(String surname) {
            this.surname = surname;
            return this;
        }

        public customer line1(String line1) {
            this.line1 = line1;
            return this;
        }

        public customer line2(String line2) {
            this.line2 = line2;
            return this;
        }

        public customer line3(String line3) {
            this.line3 = line3;
            return this;
        }

        public customer line4(String line4) {
            this.line4 = line4;
            return this;
        }

        public customer companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public customer dateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public customer initials(String initials) {
            this.initials = initials;
            return this;
        }

        public customer homePhone(String homePhoneNumber) {
            this.homePhone = homePhoneNumber;
            return this;
        }

        public customer workPhone(String workPhoneNumber) {
            this.workPhone = workPhoneNumber;
            return this;
        }

        public customer mobilePhone(String mobilePhoneNumber) {
            this.mobilePhone = mobilePhoneNumber;
            return this;
        }

        public String query() {

            try {
                customerChecks = new CustomerPostChecks();

                if(!TextUtils.isEmpty(email)) {
                    customerChecks.checkEmailAddressIsCorrectlyFormatted(email);
                    parameters.put("email", email);
                }

                if(!TextUtils.isEmpty(postCode)) {
                    customerChecks.checkPostCodeIsCorrectlyFormatted(postCode);
                    parameters.put("postCode", postCode);
                }

                if(!TextUtils.isEmpty(accountNumber)) {
                    customerChecks.checkAccountNumberIsFormattedCorrectly(accountNumber);
                    parameters.put("accountNumber", accountNumber);
                }

                if(!TextUtils.isEmpty(sortCode)) {
                    customerChecks.checkSortCodeIsFormattedCorrectly(sortCode);
                    parameters.put("bankSortCode", sortCode);
                }

                if(!TextUtils.isEmpty(accountHolderName)) {
                    customerChecks.checkAccountHolderNameIsFormattedCorrectly(accountHolderName);
                    parameters.put("accountHolderName", accountHolderName);
                }

                if(!TextUtils.isEmpty(title)) parameters.put("title", title);
                if(!TextUtils.isEmpty(firstName)) parameters.put("firstName", firstName);
                if(!TextUtils.isEmpty(surname)) parameters.put("surname", surname);
                if(!TextUtils.isEmpty(line1)) parameters.put("line1", line1);
                if(!TextUtils.isEmpty(line2)) parameters.put("line2", line2);
                if(!TextUtils.isEmpty(line3)) parameters.put("line3", line3);
                if(!TextUtils.isEmpty(line4)) parameters.put("line4", line4);
                if(!TextUtils.isEmpty(companyName)) parameters.put("companyName", companyName);
                if(!TextUtils.isEmpty(dateOfBirth)) parameters.put("dateOfBirth", dateOfBirth);
                if(!TextUtils.isEmpty(initials)) parameters.put("initials", initials);
                if(!TextUtils.isEmpty(homePhone)) parameters.put("homePhone", homePhone);
                if(!TextUtils.isEmpty(workPhone)) parameters.put("workPhone", workPhone);
                if(!TextUtils.isEmpty(mobilePhone)) parameters.put("mobilePhone", mobilePhone);

                Session createRequest = handler.session(settings);
                String sendRequest = createRequest.patch(String.format("customer/%s", guid), parameters);
                // Pass the return string to the handler. This will throw an exception if it is not what we expect
                handler.genericExceptionCheck(sendRequest);

                if(sendRequest.contains("Customer updated")) {
                    return String.format("The customer %s has been updated successfully", guid);
                } else {
                    return String.format("An unknown error has occurred. The customer %s has not been updated.", guid);
                }

            } catch(Exception e) {
                e.printStackTrace();
                return "";
            }

        }

    }

    /**
     * Modify the regular collection amount of a contract in EazyCustomerManager
     * @param contract The GUID of an existing contract within EazyCustomerManager
     * @param paymentAmount The new regular collection amount the existing contract
     * @param comment A comment to describe the actions performed when modifying the regular collection amount of the existing contract
     */
    public String contractAmount(String contract, String paymentAmount, String comment) {

        try {

            if(paymentAmount.equals("") || comment.equals("")) {
                throw new EmptyRequiredParameterException("One or more required parameters are empty. Please double check all required parameters are filled and re-submit.");
            }

            paymentChecks = new PaymentPostChecks();
            // Perform several basic checks to ensure the information provided for the customer is fit to use
            paymentChecks.checkPaymentAmount(paymentAmount);
            // Create a new dictionary of parameters
            parameters = new HashMap<>();

            // Add method arguments to the parameters only if they are not empty
            try {
                parameters.put("amount", paymentAmount);
                parameters.put("comment", comment);
            } catch(IllegalArgumentException e) {
                throw new InvalidParameterException("There was an error adding one or more parameters to the call. Please try again, or contact help@eazycollect.co.uk");
            }

            Session createRequest = handler.session(settings);
            String sendRequest = createRequest.patch(String.format("contract/%s/amount", contract), parameters);
            handler.genericExceptionCheck(sendRequest);

            // If no customers were returned
            if(sendRequest.contains("Contract updated")) {
                return String.format("The contract %s has been updated with the new regular collection amount of %s",
                        contract, paymentAmount);
            } else if(sendRequest.contains("Contract not found")) {
                throw new ResourceNotFoundException(String.format("The contract %s could not be found within EazyCustomerManager",
                        contract));
            } else {
                return sendRequest;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public String contractDayWeekly(String contract, String newPaymentDay, String comment, boolean amendNextPayment) {
        return contractDayWeekly(contract, newPaymentDay, comment, amendNextPayment, "");
    }

    /**
     * Modify the regular collection day of a weekly contract in EazyCustomerManager
     * @param contract The GUID of an existing contract within EazyCustomerManager
     * @param newPaymentDay The new regular collection day of the existing contract
     * @param comment A comment to describe the amendment made to the contract
     * @param amendNextPayment Whether or not the next payment should be amended to account for the change in contract day
     * @param nextPaymentAmount If the next payment amount is to be amended, dictate what the next payment amount should be
     */
    public String contractDayWeekly(String contract, String newPaymentDay, String comment, boolean amendNextPayment,
                                   String nextPaymentAmount) {

        try {

            if(newPaymentDay.equals("") || comment.equals("")) {
                throw new EmptyRequiredParameterException("One or more required parameters are empty. Please double check all required parameters are filled and re-submit.");
            }

            contractChecks = new ContractPostChecks();
            // Perform several basic checks to ensure the information provided for the customer is fit for use
            contractChecks.checkPaymentDayInWeekIsValid(newPaymentDay);
            // Create a new dictionary of parameters
            parameters = new HashMap<>();

            // Add method arguments to the parameters only if they are not empty
            try {
                parameters.put("day", newPaymentDay);
                parameters.put("comment", comment);
                parameters.put("patchNextPayment", String.valueOf(amendNextPayment));
            } catch(IllegalArgumentException e) {
                throw new InvalidParameterException("There was an error adding one or more parameters to the call. Please try again, or contact help@eazycollect.co.uk");
            }

            if(amendNextPayment) {
                parameters.put("nextPaymentPatchAmount", nextPaymentAmount);
            }

            Session createRequest = handler.session(settings);
            String sendRequest = createRequest.patch(String.format("contract/%s/weekly", contract), parameters);
            // Pass the return string to the handler. This will throw an exception if it is not what we expect
            handler.genericExceptionCheck(sendRequest);

            if(sendRequest.contains("Contract updated")) {
                return String.format("The contract %s has been updated with the new regular collection day of %s", contract,
                        newPaymentDay);
            } else if(sendRequest.contains("Contract not found")) {
                throw new ResourceNotFoundException(String.format("The contract %s could not be found within EazyCustomerManager",
                        contract));
            } else {
                return sendRequest;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public String contractDayMonthly(String contract, String newPaymentDay, String comment, boolean amendNextPayment) {
        return contractDayMonthly(contract, newPaymentDay, comment, amendNextPayment, "");
    }

    /**
     * Modify the regular collection day of a monthly contract in EazyCustomerManager
     * @param contract The GUID of an existing contract within EazyCustomerManager
     * @param newPaymentDay The new regular collection day of the existing contract
     * @param comment A comment to describe the amendment made to the contract
     * @param amendNextPayment Whether or not the next payment should be amended to account for the change in contract day
     * @param nextPaymentAmount If the next payment amount is to be amended, dictate what the next payment amount should be
     */
    public String contractDayMonthly(String contract, String newPaymentDay, String comment, boolean amendNextPayment,
                                     String nextPaymentAmount) {

        try {

            if(newPaymentDay.equals("") || comment.equals("")) {
                throw new EmptyRequiredParameterException("One or more required parameters are empty. Please double check all required parameters are filled and re-submit.");
            }

            if(amendNextPayment) {

                if(nextPaymentAmount.equals("")) {
                    throw new EmptyRequiredParameterException("NextPaymentAmount must be called if AmendNextPayment is set to true.");
                }

            } else {

                if(!nextPaymentAmount.equals("")) {
                    throw new ParameterNotAllowedException("NextPaymentAmount must not be called if AmendNextPayment is set to false.");
                }

            }

            contractChecks = new ContractPostChecks();
            // Perform several basic checks to ensure the information provided for the customer is fit for use
            contractChecks.checkPaymentDayInMonthIsValid(newPaymentDay);
            parameters = new HashMap<>();

            // Add method arguments to the parameters only if they are not empty
            try {
                parameters.put("monthDay", newPaymentDay);
                parameters.put("comment", comment);
                parameters.put("patchNextPayment", String.valueOf(amendNextPayment));
            } catch(IllegalArgumentException e) {
                throw new InvalidParameterException("There was an error adding one or more parameters to the call. Please try again, or contact help@eazycollect.co.uk");
            }

            if(amendNextPayment) {
                parameters.put("nextPaymentPatchAmount", nextPaymentAmount);
            }

            Session createRequest = handler.session(settings);
            String sendRequest = createRequest.patch(String.format("contract/%s/monthly", contract), parameters);
            // Pass the return string to the handler. This will throw an exception if it is not what we expect
            handler.genericExceptionCheck(sendRequest);

            if(sendRequest.contains("Contract updated")) {
                return String.format("The contract %s has been updated with the new regular collection day of %s", contract,
                        newPaymentDay);
            } else if(sendRequest.contains("Contract not found")) {
                throw new ResourceNotFoundException(String.format("The contract %s could not be found within EazyCustomerManager",
                        contract));
            } else {
                return sendRequest;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public String contractDayAnnually(String contract, String newPaymentDay, String newPaymentMonth, String comment,
                                      boolean amendNextPayment) {
        return contractDayAnnually(contract, newPaymentDay, newPaymentMonth, comment, amendNextPayment, "");
    }

    /**
     * Modify the regular collection day and month of an annual contract in EazyCustomerManager
     * @param contract The GUID of an existing contract within EazyCustomerManager
     * @param newPaymentDay The new regular collection day of the existing contract
     * @param newPaymentMonth The new regular collection month of the existing contract
     * @param comment A comment to describe the amendment made to the contract
     * @param amendNextPayment Whether or not the next payment should be amended to account for the change in contract day
     * @param nextPaymentAmount If the next payment amount is to be amended, dictate what the next payment amount should be
     */
    public String contractDayAnnually(String contract, String newPaymentDay, String newPaymentMonth, String comment,
                                      boolean amendNextPayment, String nextPaymentAmount) {

        try {

            if(newPaymentDay.equals("") || newPaymentMonth.equals("") || comment.equals("")) {
                throw new EmptyRequiredParameterException("One or more required parameters are empty. Please double check all required parameters are filled and re-submit.");
            }

            if(amendNextPayment) {

                if(nextPaymentAmount.equals("")) {
                    throw new EmptyRequiredParameterException("NextPaymentAmount must be called if AmendNextPayment is set to true.");
                }

            } else {

                if(!nextPaymentAmount.equals("")) {
                    throw new ParameterNotAllowedException("NextPaymentAmount must not be called if AmendNextPayment is set to false.");
                }

            }

            contractChecks = new ContractPostChecks();
            // Perform several basic checks to ensure the information provided for the customer is fit for use
            contractChecks.checkPaymentDayInMonthIsValid(newPaymentDay);
            contractChecks.checkPaymentMonthInYearIsValid(newPaymentMonth);
            // Create a new dictionary of parameters
            parameters = new HashMap<>();

            // Add method arguments to the parameters only if they are not empty
            try {
                parameters.put("monthDay", newPaymentDay);
                parameters.put("month", newPaymentMonth);
                parameters.put("comment", comment);
                parameters.put("patchNextPayment", String.valueOf(amendNextPayment));
            } catch(IllegalArgumentException e) {
                throw new InvalidParameterException("There was an error adding one or more parameters to the call. Please try again, or contact help@eazycollect.co.uk");
            }

            if(amendNextPayment) {
                parameters.put("nextPaymentPatchAmount", nextPaymentAmount);
            }

            Session createRequest = handler.session(settings);
            String sendRequest = createRequest.patch(String.format("contract/%s/annual", contract), parameters);
            // Pass the return string to the handler. This will throw an exception if it is not what we expect
            handler.genericExceptionCheck(sendRequest);

            if(sendRequest.contains("Contract updated")) {
                return String.format("The contract %s has been updated with the new regular collection day of %s and regular collection month of %s",
                        contract, newPaymentDay, newPaymentMonth);
            } else if(sendRequest.contains("Contract not found")) {
                throw new ResourceNotFoundException(String.format("The contract %s could not be found within EazyCustomerManager", contract));
            } else {
                return sendRequest;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * Modify a payment within EazyCustomerManager
     * @param contract The GUID of an existing contract within EazyCustomerManager
     * @param payment The GUID of an existing payment within EazyCustomerManager
     * @param paymentAmount The new regular collection amount the existing contract
     * @param paymentDay The new collection day of the payment
     * @param comment A comment to describe the actions performed when amending the payment
     */
    public String payment(String contract, String payment, String paymentAmount, String paymentDay, String comment) {

        try {

            if(paymentAmount.equals("") || paymentDay.equals("") || comment.equals("")) {
                throw new EmptyRequiredParameterException("One or more required parameters are empty. Please double check all required parameters are filled and re-submit.");
            }

            // Create a new dictionary of parameters
            parameters = new HashMap<>();
            paymentChecks = new PaymentPostChecks();
            paymentChecks.checkPaymentAmount(paymentAmount);

            // Add method arguments to the parameters only if they are not empty
            try {
                parameters.put("amount", paymentAmount);
                parameters.put("date", paymentDay);
                parameters.put("comment", comment);
            } catch(IllegalArgumentException e) {
                throw new InvalidParameterException("There was an error adding one or more parameters to the call. Please try again, or contact help@eazycollect.co.uk");
            }

            Session createRequest = handler.session(settings);
            String sendRequest = createRequest.patch(String.format("contract/%s/payment/%s", contract, payment), parameters);

            if(sendRequest.contains("Contract not found")) {
                throw new InvalidParameterException("The specified contract could not be found in EazyCustomerManager");
            }

            // Pass the return string to the handler. This will throw an exception if it is not what we expect
            handler.genericExceptionCheck(sendRequest);
            return sendRequest;
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }

    }

}
