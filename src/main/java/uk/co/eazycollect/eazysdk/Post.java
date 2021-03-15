package uk.co.eazycollect.eazysdk;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.util.TextUtils;
import uk.co.eazycollect.eazysdk.exceptions.EmptyRequiredParameterException;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.exceptions.ParameterNotAllowedException;
import uk.co.eazycollect.eazysdk.exceptions.RecordAlreadyExistsException;
import uk.co.eazycollect.eazysdk.exceptions.ResourceNotFoundException;
import uk.co.eazycollect.eazysdk.utilities.ContractPostChecks;
import uk.co.eazycollect.eazysdk.utilities.CustomerPostChecks;
import uk.co.eazycollect.eazysdk.utilities.PaymentPostChecks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * A collection of POST requests made to the EazyCustomerManager API
 */
public class Post {
    // The client object which manages the Session and Settings
    private static ClientHandler handler;
    // The settings object, derives from ClientHandler
    private static Properties settings;
    private CustomerPostChecks customerChecks;
    private ContractPostChecks contractChecks;
    private static PaymentPostChecks paymentChecks;
    // The parameters passed from a function, if there are any
    private Map<String, String> parameters;
    private static JsonParser parser;

    public Post(Properties settings) {
        // Get the Settings passed from the Client Handler
        this.settings = settings;
        // Reference the Client Handler for sending a request
        handler = new ClientHandler();
    }

    /**
     * Create a new call back URL for EazyCustomerManager
     * NOTE: We strongly recommend using a HTTPS secured URL as the return endpoint.
     *
     * @param entity The entity for which to receive BACS messages. Valid choices: "contract", "customer", "payment"
     * @param callbackUrl The new URL to set
     * @return "The new callback URL is https://my.website.com/webhook"
     */
    public String callbackUrl(String entity, String callbackUrl) {

        try {

            if (!Arrays.asList("contract", "customer", "payment").contains(entity.toLowerCase())) {
                throw new InvalidParameterException(String.format("%s is not a valid entity; must be one of either 'contract', 'customer' or 'payment'.", entity));
            }

            // Create a new dictionary of parameters
            parameters = new HashMap<>();
            parameters.put("url", callbackUrl);
            Session createRequest = handler.session(settings);
            String sendRequest = createRequest.post(String.format("BACS/%s/callback", entity), parameters);
            // Pass the return string to the handler. This will throw an exception if it is not what we expect
            handler.genericExceptionCheck(sendRequest);
            return String.format("The new callback URL is %s", callbackUrl);
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }


    }

    public static class customer {
        private Map<String, String> parameters;
        private CustomerPostChecks customerChecks;
        private String email;
        private String title;
        private String customerReference;
        private String firstName;
        private String surname;
        private String line1;
        private String postCode;
        private String accountNumber;
        private String sortCode;
        private String accountHolderName;
        private String line2;
        private String line3;
        private String line4;
        private String companyName;
        private String dateOfBirth;
        private String initials;
        private String homePhone;
        private String mobilePhone;
        private String workPhone;

        public customer() {
            parameters = new HashMap<>();
        }

        public customer email(String email) {
            this.email = email;
            return this;
        }

        public customer title(String title) {
            this.title = title;
            return this;
        }

        public customer customerReference(String customerReference) {
            this.customerReference = customerReference;
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

        public customer mobilePhone(String mobilePhoneNumber) {
            this.mobilePhone = mobilePhoneNumber;
            return this;
        }

        public customer workPhone(String workPhoneNumber) {
            this.workPhone = workPhoneNumber;
            return this;
        }

        public String query() {

            try {

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(title) || TextUtils.isEmpty(customerReference)
                        || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(surname) || TextUtils.isEmpty(line1)
                        || TextUtils.isEmpty(postCode) || TextUtils.isEmpty(accountNumber) || TextUtils.isEmpty(sortCode)
                        || TextUtils.isEmpty(accountHolderName)) {
                    throw new EmptyRequiredParameterException("One or more required parameters are empty. Please double check all required parameters are filled and re-submit.");
                }

                customerChecks = new CustomerPostChecks();
                // Perform several basic checks to ensure the information provided for the customer is fit for use
                customerChecks.checkEmailAddressIsCorrectlyFormatted(email);
                customerChecks.checkPostCodeIsCorrectlyFormatted(postCode);
                customerChecks.checkAccountNumberIsFormattedCorrectly(accountNumber);
                customerChecks.checkSortCodeIsFormattedCorrectly(sortCode);
                customerChecks.checkAccountHolderNameIsFormattedCorrectly(accountHolderName);
                parameters.put("email", email);
                parameters.put("title", title);
                parameters.put("customerRef", customerReference);
                parameters.put("firstName", firstName);
                parameters.put("surname", surname);
                parameters.put("line1", line1);
                parameters.put("postCode", postCode);
                parameters.put("accountNumber", accountNumber);
                parameters.put("bankSortCode", sortCode);
                parameters.put("accountHolderName", accountHolderName);

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
                String sendRequest = createRequest.post("customer", parameters);

                // If no customers were returned
                if(sendRequest.contains("There is an existing Customer with the same Client")) {
                    throw new RecordAlreadyExistsException(String.format("A customer with the customer reference of %s already exists within the client. Please change the customer reference and re-submit",
                            parameters.get("customerRef")));
                } else {
                    // Pass the return string to the handler. This will throw an exception if it is not what we expect
                    handler.genericExceptionCheck(sendRequest);
                    // Get the JSON returned from EazyCustomerManager
                    JsonObject sendRequestAsJson = parser.parse(sendRequest).getAsJsonObject();
                    // Get the list of Customers JSON objects
                    return String.format("%s", sendRequestAsJson);
                }

            } catch(Exception e) {
                e.printStackTrace();
                return "";
            }

        }

    }

    public static class contract {
        private Map<String, String> parameters;
        private ContractPostChecks contractChecks;
        private String customer;
        private String scheduleName;
        private String startDate;
        private boolean giftAid;
        private String terminationType;
        private String atTheEnd;
        private String numberOfDebits;
        private String frequency;
        private String initialAmount;
        private String extraInitialAmount;
        private String paymentAmount;
        private String finalAmount;
        private String paymentMonthInYear;
        private String paymentDayInMonth;
        private String paymentDayInWeek;
        private String terminationDate;
        private String additionalReference;
        private String customDDReference;

        public contract() {
            parameters = new HashMap<>();
            contractChecks = new ContractPostChecks();
            parser = new JsonParser();
        }

        public contract customer(String customer) {
            this.customer = customer;
            return this;
        }

        public contract scheduleName(String scheduleName) {
            this.scheduleName = scheduleName;
            return this;
        }

        public contract startDate(String startDate) {
            this.startDate = startDate;
            return this;
        }

        public contract giftAid(boolean giftAid) {
            this.giftAid = giftAid;
            return this;
        }

        public contract terminationType(String terminationType) {
            this.terminationType = terminationType;
            return this;
        }

        public contract atTheEnd(String atTheEnd) {
            this.atTheEnd = atTheEnd;
            return this;
        }

        public contract numberOfDebits(String numberOfDebits) {
            this.numberOfDebits = numberOfDebits;
            return this;
        }

        public contract frequency(String frequency) {
            this.frequency = frequency;
            return this;
        }

        public contract initialAmount(String initialAmount) {
            this.initialAmount = initialAmount;
            return this;
        }

        public contract extraInitialAmount(String extraInitialAmount) {
            this.extraInitialAmount = extraInitialAmount;
            return this;
        }

        public contract paymentAmount(String paymentAmount) {
            this.paymentAmount = paymentAmount;
            return this;
        }

        public contract finalAmount(String finalAmount) {
            this.finalAmount = finalAmount;
            return this;
        }

        public contract paymentMonthInYear(String paymentMonthInYear) {
            this.paymentMonthInYear = paymentMonthInYear;
            return this;
        }

        public contract paymentDayInMonth(String paymentDayInMonth) {
            this.paymentDayInMonth = paymentDayInMonth;
            return this;
        }

        public contract paymentDayInWeek(String paymentDayInWeek) {
            this.paymentDayInWeek = paymentDayInWeek;
            return this;
        }

        public contract terminationDate(String terminationDate) {
            this.terminationDate = terminationDate;
            return this;
        }

        public contract additionalReference(String additionalReference) {
            this.additionalReference = additionalReference;
            return this;
        }

        public contract customDDReference(String customDDReference) {
            this.customDDReference = customDDReference;
            return this;
        }

        public String query() {

            try {

                if(customer.equals("") || scheduleName.equals("") || startDate.equals("") || terminationType.equals("") || atTheEnd.equals("")) {
                    throw new EmptyRequiredParameterException("One or more required parameters are empty. Please double check all required parameters are filled and re-submit.");
                }

                contractChecks = new ContractPostChecks();
                // Perform several basic checks to ensure the information provided for the customer is fit for use.
                contractChecks.checkScheduleNameIsAvailable(scheduleName, settings);
                int terminationTypeInt = contractChecks.checkTerminationTypeIsValid(terminationType);
                int atTheEndInt = contractChecks.checkAtTheEndIsValid(atTheEnd);
                String startDateDateString = contractChecks.checkStartDateIsValid(startDate, settings);
                boolean adHocBool = contractChecks.checkScheduleAdHocStatus(scheduleName, settings);

                if (adHocBool) {

                    if (Boolean.parseBoolean(settings.getProperty("contracts.AutoFixTerminationTypeAdHoc"))) {

                        if (terminationTypeInt != 1) {
                            terminationType = "Until further notice";
                        }

                    } else {

                        if (terminationTypeInt != 1) {
                            throw new InvalidParameterException("Termination type must be set to 'Until further notice' on ad-hoc contracts");
                        }

                    }

                    if (Boolean.parseBoolean(settings.getProperty("contracts.AutoFixAtTheEndAdHoc"))) {

                        if (atTheEndInt != 1) {
                            atTheEnd = "Switch to further notice";
                        }

                    } else {

                        if (atTheEndInt != 1) {
                            throw new InvalidParameterException("AtTheEnd must be set to 'Switch to further notice' on ad-hoc contracts");
                        }

                    }

                    if (!TextUtils.isEmpty(initialAmount)) {
                        throw new ParameterNotAllowedException("InitialAmount is not allowed on ad-hoc contracts");
                    }

                    if (!TextUtils.isEmpty(extraInitialAmount)) {
                        throw new ParameterNotAllowedException("ExtraInitialAmount is not allowed on ad-hoc contracts");
                    }

                    if (!TextUtils.isEmpty(finalAmount)) {
                        throw new ParameterNotAllowedException("FinalAmount is not allowed on ad-hoc contracts");
                    }

                } else {

                    if (TextUtils.isEmpty(frequency)) {
                        throw new EmptyRequiredParameterException("Frequency is mandatory on non-ad-hoc contracts");
                    }

                    if (TextUtils.isEmpty(paymentAmount)) {
                        throw new EmptyRequiredParameterException("Payment amount is mandatory on non-ad-hoc contracts");
                    }

                    int frequencyType = contractChecks.checkFrequency(scheduleName, settings);

                    if (frequencyType == 0) {

                        if (TextUtils.isEmpty(paymentDayInWeek)) {
                            throw new EmptyRequiredParameterException("Payment day in week is mandatory on weekly contracts");
                        } else {
                            contractChecks.checkPaymentDayInWeekIsValid(paymentDayInWeek);
                        }

                    } else if (frequencyType == 1) {

                        if (TextUtils.isEmpty(paymentDayInMonth)) {
                            throw new EmptyRequiredParameterException("Payment day in month is mandatory on monthly contracts");
                        } else {

                            if (!startDateDateString.substring(8, 10).equals(String.format("%02d",
                                    Integer.parseInt(paymentDayInMonth)))) {

                                if (Boolean.parseBoolean(settings.getProperty("contracts.AutoFixPaymentDayInMonth"))) {
                                    paymentDayInMonth = startDateDateString.substring(8, 10);
                                } else {
                                    paymentDayInMonth = startDateDateString.substring(8, 10);
                                    throw new InvalidParameterException(String.format("PaymentDayInMonth must be set to %s if the start date is %s.", paymentDayInMonth, startDateDateString));
                                }

                            }

                            contractChecks.checkPaymentDayInMonthIsValid(paymentDayInMonth);
                        }

                    } else if (frequencyType == 2) {

                        if (paymentDayInMonth.equals("")) {
                            throw new EmptyRequiredParameterException("Payment day in month is mandatory on annual contracts");
                        } else if (paymentMonthInYear.equals("")) {
                            throw new EmptyRequiredParameterException("Payment month in year is mandatory on annual contracts");
                        } else {

                            if (!startDateDateString.substring(5, 7).equals(String.format("%02d",
                                    Integer.parseInt(paymentMonthInYear)))) {

                                if (Boolean.parseBoolean(settings.getProperty("contracts.AutoFixPaymentMonthInYear"))) {
                                    paymentMonthInYear = startDateDateString.substring(5, 7);
                                } else {
                                    throw new InvalidParameterException(String.format("PaymentMonthInYear must be set to %s if the start date is %s.",
                                            startDateDateString.substring(5, 7), startDateDateString));
                                }

                            }

                            if (!startDateDateString.substring(8, 10).equals(String.format("%02d",
                                    Integer.parseInt(paymentDayInMonth)))) {

                                if (Boolean.parseBoolean(settings.getProperty("contracts.AutoFixPaymentDayInMonth"))) {
                                    paymentDayInMonth = startDateDateString.substring(8, 10);
                                } else {
                                    throw new InvalidParameterException(String.format("PaymentDayInMonth must be set to %s if the start date is %s.",
                                            startDateDateString.substring(8, 10), startDateDateString));
                                }

                            }

                            contractChecks.checkPaymentDayInMonthIsValid(paymentDayInMonth);
                            contractChecks.checkPaymentMonthInYearIsValid(paymentMonthInYear);
                        }

                    }

                    if (terminationTypeInt == 0) {

                        if (TextUtils.isEmpty(numberOfDebits)) {
                            throw new EmptyRequiredParameterException("NumberOfDebits is mandatory if TerminationType is set to 'Take certain number of debits'.");
                        } else {
                            contractChecks.checkNumberOfDebitsIsValid(numberOfDebits);
                        }

                    } else if (terminationTypeInt == 1) {

                        if (atTheEndInt != 1) {
                            throw new InvalidParameterException("AtTheEnd must be set to 'Switch to further notice' if TerminationType is set to 'Until further notice'.");
                        }

                    } else if (terminationTypeInt == 2) {

                        if (TextUtils.isEmpty(terminationDate)) {
                            throw new EmptyRequiredParameterException("TerminationDate is mandatory if TerminationType set to 'End on exact date'.");
                        } else {
                            contractChecks.checkTerminationDateIsAfterStartDate(terminationDate, startDate);
                        }

                    }

                }

                // Add method arguments to the parameters only if they are not empty
                parameters.put("scheduleName", scheduleName);
                parameters.put("start", startDateDateString);
                parameters.put("isGiftAid", String.valueOf(giftAid));
                parameters.put("terminationType", terminationType);
                parameters.put("atTheEnd", atTheEnd);

                if(!TextUtils.isEmpty(numberOfDebits)) parameters.put("numberOfDebits", numberOfDebits);
                if(!TextUtils.isEmpty(frequency)) parameters.put("every", frequency);
                if(!TextUtils.isEmpty(initialAmount)) parameters.put("initialAmount", initialAmount);
                if(!TextUtils.isEmpty(extraInitialAmount)) parameters.put("extraInitialAmount", extraInitialAmount);
                if(!TextUtils.isEmpty(paymentAmount)) parameters.put("amount", paymentAmount);
                if(!TextUtils.isEmpty(finalAmount)) parameters.put("finalAmount", finalAmount);
                if(!TextUtils.isEmpty(paymentMonthInYear)) parameters.put("paymentMonthInYear", paymentMonthInYear);
                if(!TextUtils.isEmpty(paymentDayInMonth)) parameters.put("paymentDayInMonth", paymentDayInMonth);
                if(!TextUtils.isEmpty(paymentDayInWeek)) parameters.put("paymentDayInWeek", paymentDayInWeek);
                if(!TextUtils.isEmpty(terminationDate)) parameters.put("terminationDate", terminationDate);
                if(!TextUtils.isEmpty(additionalReference)) parameters.put("additionalReference", additionalReference);
                if(!TextUtils.isEmpty(customDDReference)) parameters.put("customDirectDebitReference", customDDReference);

                Session createRequest = handler.session(settings);
                String sendRequest = createRequest.post(String.format("customer/%s/contract", customer), parameters);
                // Pass the return string to the handler. This will throw an exception if it is not what we expect
                handler.genericExceptionCheck(sendRequest);
                // Get the JSON returned from EazyCustomerManager
                JsonObject sendRequestAsJson = parser.parse(sendRequest).getAsJsonObject();
                // Get the list of Contracts JSON objects
                return sendRequestAsJson.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }

        }

    }

    /**
     * Cancel a Direct Debit within EazyCustomerManager
     * NOTE: Canceling a Direct Debit will not cancel the payment creation process.The reason being; There are two parts to a contract, the schedule and the Direct Debit.Cancelling the Direct Debit will
     * cease future payments to the bank, but it will generate payments on the system. These payments will return unpaid, though any ad-hoc payments must be manually deleted.
     *
     * @param contract The GUID of the contract ot be archived
     * @return A confirmation string
     */
    public String cancelDirectDebit(String contract) {

        try {
            Session createRequest = handler.session(settings);
            String sendRequest = createRequest.post(String.format("contract/%s/cancel", contract));
            // Pass the return string to the handler. This will throw an exception if it is not what we expect
            handler.genericExceptionCheck(sendRequest);

            if (sendRequest.contains("Contract not found")) {
                throw new ResourceNotFoundException(String.format("The contract %s could not be found.", contract));
            } else {
                return sendRequest;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * Archive a contract within ECM3
     * NOTE: Archiving a contract achieves different results to canceling a Direct Debit.First and most importantly, the process is irreversible.Once a contract is archived, it can not
     * be unarchived.The process flow works like so; The Direct Debit is canceled, any arrears that are outstanding are written off, any future scheduled payments are canceled and finally,
     * the contract status is set to archived.Like canceling a Direct Debit, any ad_hoc payments must be manually canceled.
     *
     * @param contract The GUID of the contract owning the Direct Debit to be cancelled
     * @return A confirmation string
     */
    public String archiveContract(String contract) {

        try {
            Session createRequest = handler.session(settings);
            String sendRequest = createRequest.post(String.format("contract/%s/archive", contract));
            // Pass the return string to the handler. This will throw an exception if it is not what we expect
            handler.genericExceptionCheck(sendRequest);

            if(sendRequest.contains("Contract is already archived")) {
                throw new RecordAlreadyExistsException(String.format("The contract %s is already archived.", contract));
            } else {
                return sendRequest;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }



    }

    /**
     * Reactivate a Direct Debit within EazyCustomerManager
     * NOTE: Reactivating a contract changes the status of a contract from ‘Canceled’ to ‘Pending to activate’. This will sent a new instruction to the bank, generating an 0N charge.
     *
     * @param contract GUID of the contract to be re-activated
     * @return Confirmation string
     */
    public String reactivateDirectDebit(String contract) {

        try {
            Session createRequest = handler.session(settings);
            String sendRequest = createRequest.post(String.format("contract/%s/reactivate", contract));
            // Pass the return string to the handler. This will throw an exception if it is not what we expect
            handler.genericExceptionCheck(sendRequest);
            return sendRequest;
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static class restartContract {
        private Map<String, String> parameters;
        private String contract;
        private String terminationType;
        private String atTheEnd;
        private String paymentAmount;
        private String initialAmount;
        private String finalAmount;
        private String paymentDayInMonth;
        private String paymentMonthInYear;
        private String additionalReference;

        public restartContract() {
            parameters = new HashMap<>();
        }

        public restartContract contract(String contract) {
            this.contract = contract;
            return this;
        }

        public restartContract terminationType(String terminationType) {
            this.terminationType = terminationType;
            return this;
        }

        public restartContract atTheEnd(String atTheEnd) {
            this.atTheEnd = atTheEnd;
            return this;
        }

        public restartContract paymentAmount(String paymentAmount) {
            this.paymentAmount = paymentAmount;
            return this;
        }

        public restartContract initialAmount(String initialAmount) {
            this.initialAmount = initialAmount;
            return this;
        }

        public restartContract finalAmount(String finalAmount) {
            this.finalAmount = finalAmount;
            return this;
        }

        public restartContract paymentDayInMonth(String paymentDayInMonth) {
            this.paymentDayInMonth = paymentDayInMonth;
            return this;
        }

        public restartContract paymentMonthInYear(String paymentMonthInYear) {
            this.paymentMonthInYear = paymentMonthInYear;
            return this;
        }

        public restartContract additionalReference(String additionalReference) {
            this.additionalReference = additionalReference;
            return this;
        }

        public String query() {

            try {

                if(TextUtils.isEmpty(contract) || TextUtils.isEmpty(terminationType) || TextUtils.isEmpty(atTheEnd)) {
                    throw new EmptyRequiredParameterException("One or more required parameters are empty. Please double check all required parameters are filled and re-submit.");
                }

                parameters.put("terminationType", terminationType);
                parameters.put("atTheEnd", atTheEnd);

                if(!TextUtils.isEmpty(paymentAmount)) parameters.put("amount", paymentAmount);
                if(!TextUtils.isEmpty(initialAmount)) parameters.put("initialAmount", initialAmount);
                if(!TextUtils.isEmpty(finalAmount)) parameters.put("finalAmount", finalAmount);
                if(!TextUtils.isEmpty(paymentDayInMonth)) parameters.put("paymentDayInMonth", paymentDayInMonth);
                if(!TextUtils.isEmpty(paymentMonthInYear)) parameters.put("paymentMonthInYear", paymentMonthInYear);
                if(!TextUtils.isEmpty(additionalReference)) parameters.put("additionalReference", additionalReference);

                Session createRequest = handler.session(settings);
                String sendRequest = createRequest.post(String.format("contract/%s/restart", contract), parameters);
                // Pass the return string to the handler. This will throw an exception if it is not what we expect
                handler.genericExceptionCheck(sendRequest);
                return sendRequest;
            } catch(Exception e) {
                e.printStackTrace();
                return "";
            }

        }

    }

    public String payment(String contract, String paymentAmount, String collectionDate, String comment) {
        return payment(contract, paymentAmount, collectionDate, comment, false);
    }

    /**
     * Create a new payment against an existing contract in EazyCustomerManager
     *
     * @param contract       The GUID of the contract a payment will be made against
     * @param paymentAmount  The total amount to be collected from the new payment
     * @param collectionDate The desired start date of the new contract. This must be x working days in the future, where x is the agreed amount of working days with Eazy Collect.
     * @param comment        A comment related to the new payment
     * @param isCredit       If you have your own SUN and you have made prior arrangements with Eazy Collect, this may be passed to issue a credit to a customer.By default, it is set to false.
     * @return Confirmation string
     */
    public String payment(String contract, String paymentAmount, String collectionDate, String comment, boolean isCredit) {

        try {

            if(contract.equals("") || paymentAmount.equals("") || collectionDate.equals("") || comment.equals("")) {
                throw new EmptyRequiredParameterException("One or more required parameters are empty. Please double check all required parameters are filled and re-submit.");
            }

            parameters = new HashMap<>();
            paymentChecks = new PaymentPostChecks();
            boolean paymentAmountNotNegative = paymentChecks.checkPaymentAmount(paymentAmount);
            String trueCollectionDate = paymentChecks.checkPaymentDate(collectionDate, settings);
            boolean isCreditAllowed = paymentChecks.checkIfCreditIsAllowed(settings);

            // Add method arguments to the parameters only if they are not empty
            try {
                parameters.put("amount", paymentAmount);
                parameters.put("date", trueCollectionDate);
                parameters.put("comment", comment);
            } catch(IllegalArgumentException e) {
                throw new InvalidParameterException("There was an error adding one or more parameters to the call. Please try again, or contact help@eazycollect.co.uk");
            }

            if(!isCreditAllowed) {

                if(isCredit) {
                    throw new InvalidParameterException("Is Credit is not allowed.");
                }

            } else {

                if(isCredit) {
                    parameters.put("isCredit", String.valueOf(true));
                }

            }

            Session createRequest = handler.session(settings);
            String sendRequest = createRequest.post(String.format("contract/%s/payment", contract), parameters);

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
