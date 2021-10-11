package uk.co.eazycollect.eazysdk;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A collection of GET requests made to the EazyCustomerAPI
 */
public class Get {
    //region Client objects
    // The client object which manages the Session and Settings
    private static ClientHandler handler;
    // The settings object, derives from ClientHandler
    private static Properties settings;
    //endregion

    //region Dictionary objects
    // The parameters passed from a function, if there are any
    private Map<String, String> parameters;
    //endregion

    //private static Gson gson;
    private static JsonParser parser;

    public Get(Properties settings) {
        // Get the Settings passed from the Client Handler
        Get.settings = settings;
        // Reference the Client Handler for sending a request
        handler = new ClientHandler();
        parser = new JsonParser();
    }

    /**
     * Get the current callback URL for given entity from EazyCustomerManager
     * @param entity The entity for which to receive BACS messages. Valid choices: "contract", "customer", "payment", "schedule"
     * @return "The callback URL is https://my.website.com/webhook"
     */
    public String callbackUrl(String entity) {

        try {

            if (!Arrays.asList("contract", "customer", "payment", "schedule").contains(entity.toLowerCase())) {
                throw new InvalidParameterException(String.format("%s is not a valid entity; must be one of either 'contract', 'customer', 'payment' or 'schedule'.", entity));
            }

            Session createRequest = handler.session(settings);
            String sendRequest = createRequest.get(String.format("BACS/%s/callback", entity));

            // Null will be returned if a callback URL currently does not exist
            if(sendRequest.equals("{\"Message\":null)")) {
                return "A callback URI has not yet been set";
            } else {
                // Pass the return string to the handler. This will throw an exception if it is not what we expect
                handler.genericExceptionCheck(sendRequest);
                // Get the JSON returned from EazyCustomerManager
                JsonObject sendRequestAsJson = parser.parse(sendRequest).getAsJsonObject();
                String callbackUri = sendRequestAsJson.get("Message").getAsString();
                return String.format("The callback URL is %s", callbackUri);
            }

        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static class customers {
        private Map<String, String> parameters;

        public customers() {
            parameters = new HashMap<>();
        }

        public customers email(String email) {
            parameters.put("email", email);
            return this;
        }

        public customers title(String title) {
            parameters.put("title", title);
            return this;
        }

        public customers searchFrom(String from) {
            parameters.put("from", from);
            return this;
        }

        public customers searchTo(String to) {
            parameters.put("to", to);
            return this;
        }

        public customers dateOfBirth(String dateOfBirth) {
            parameters.put("dateOfBirth", dateOfBirth);
            return this;
        }

        public customers customerReference(String customerRef) {
            parameters.put("customerRef", customerRef);
            return this;
        }

        public customers firstName(String firstName) {
            parameters.put("firstName", firstName);
            return this;
        }

        public customers surname(String surname) {
            parameters.put("surname", surname);
            return this;
        }

        public customers companyName(String companyName) {
            parameters.put("companyName", companyName);
            return this;
        }

        public customers postCode(String postCode) {
            parameters.put("postCode", postCode);
            return this;
        }

        public customers accountNumber(String accountNumber) {
            parameters.put("accountNumber", accountNumber);
            return this;
        }

        public customers sortCode(String bankSortCode) {
            parameters.put("bankSortCode", bankSortCode);
            return this;
        }

        public customers accountHolderName(String accountHolderName) {
            parameters.put("accountHolderName", accountHolderName);
            return this;
        }

        public customers homePhone(String homePhoneNumber) {
            parameters.put("homePhoneNumber", homePhoneNumber);
            return this;
        }

        public customers mobilePhone(String mobilePhoneNumber) {
            parameters.put("mobilePhoneNumber", mobilePhoneNumber);
            return this;
        }

        public customers workPhone(String workPhoneNumber) {
            parameters.put("workPhoneNumber", workPhoneNumber);
            return this;
        }

        public String query() {

            try {

                // If no parameters have been passed and the customerSearch warning is enabled, warn the user
                if(Boolean.parseBoolean(settings.getProperty("warnings.customerSearchWarning")) && parameters.size() == 0) {
                    System.out.println("Searching for customer without passing any parameters may take some time.");
                }

                Session createRequest = handler.session(settings);
                String sendRequest = createRequest.get("customer", parameters);

                // If no customers were returned
                if(sendRequest.equals("{\"Customers\":[]}")) {
                    return "No customers could be found with the provided parameters";
                } else {
                    // Pass the return string to the handler. This will throw an exception if it is not what we expect
                    handler.genericExceptionCheck(sendRequest);
                    // Get the JSON returned from EazyCustomerManager
                    JsonObject sendRequestAsJson = parser.parse(sendRequest).getAsJsonObject();
                    // Get the list of Customers JSON objects
                    return sendRequestAsJson.get("Customers").toString();
                }

            } catch(Exception e) {
                e.printStackTrace();
                return "";
            }

        }

    }

    /**
     * Return all contracts belonging to a provided customer
     * @param customer The GUID of the customer to be searched against
     * @return A string of contract JSON objects
     */
    public String contracts(String customer) {

        try {
            Session createRequest = handler.session(settings);
            String sendRequest = createRequest.get(String.format("customer/%s/contract", customer));

            // If no contracts were returned
            if(sendRequest.contains("\"Contracts\":[]")) {
                return String.format("No contracts could be associated with the customer %s", customer);
            } else {
                // Pass the return string to the handler. This will throw an exception if it is not what we expect
                handler.genericExceptionCheck(sendRequest);
                // Get the JSON returned from EazyCustomerManager
                JsonObject sendRequestAsJson = parser.parse(sendRequest).getAsJsonObject();
                // Get the list of Contracts JSON objects
                return sendRequestAsJson.get("Contracts").toString();
            }

        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public String payments(String contract) {
        return payments(contract, 100);
    }

    /**
     * Return all payments belonging to a specific contract
     * @param contract The GUID of the contract to be searched against
     * @param numberOfRows The number of payments to be returned.
     * @return A string of payment JSON objects
     */
    public String payments(String contract, int numberOfRows) {

        try {
            // Create a new dictionary of parameters
            Map<String, String> parameters = new HashMap<>();
            parameters.put("rows", String.valueOf(numberOfRows));
            Session createRequest = handler.session(settings);
            String sendRequest = createRequest.get(String.format("contract/%s/payment", contract), parameters);

            // If no payments were returned
            if(sendRequest.equals("{\"Payments\":[]}")) {
                return String.format("No payments could be associated with the contract %s", contract);
            } else {
                // Pass the return string to the handler. This will throw an exception if it is not what we expect.
                handler.genericExceptionCheck(sendRequest);
                // Get the JSON returned from EazyCustomerManager
                JsonObject sendRequestAsJson = parser.parse(sendRequest).getAsJsonObject();
                String payments = sendRequestAsJson.get("Payments").toString();
                return payments;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * Return a single payment from a given contract
     * @param contract The GUID of the contract to be searched against
     * @param payment The GUID of the payment to be searched against
     * @return payment json object
     */
    public String paymentsSingle(String contract, String payment) {

        try {
            Session createRequest = handler.session(settings);
            String sendRequest = createRequest.get(String.format("contract/%s/payment/%s", contract, payment));
            // Pass the return string to the handler. This will throw an exception if it is not what we expect
            handler.genericExceptionCheck(sendRequest);
            return sendRequest;
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * Return all available schedules from EazyCustomerManager
     * NOTE: You should not need to run this command manually. The SDK will automatically get a list of available schedules when first ran,
     * and place them in the includes folder, named sandbox.csv and ecm3.csv respectively.
     * @return schedules json objects
     */
    public String schedules() {

        try {
            Session createRequest = handler.session(settings);
            String sendRequest = createRequest.get("schedules");
            // Pass the return string to the handler. This will throw an exception if it is not what we expect
            handler.genericExceptionCheck(sendRequest);
            return sendRequest;
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }


    }

}
