package uk.co.eazycollect.eazysdk;

import uk.co.eazycollect.eazysdk.exceptions.EmptyRequiredParameterException;
import uk.co.eazycollect.eazysdk.exceptions.InvalidEnvironmentException;
import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;
import uk.co.eazycollect.eazysdk.exceptions.ResourceNotFoundException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A collection of DELETE requests made to the EazyCustomerManager API
 */
public class Delete {
    // The client object which manages the Session and Settings
    private ClientHandler handler;
    // The settings object, derives from ClientHandler
    private Properties settings;
    // The parameters passed from a function, if there are any
    private Map<String, String> parameters;

    public Delete(Properties settings) {
        this.settings = settings;
        handler = new ClientHandler();
    }

    /**
     * Delete the currently selected callback URL for EazyCustomerManager
     * @param entity The entity for which to receive BACS messages. Valid choices: "contract", "customer", "payment"
     * @return Confirmation string
     */
    public String callbackUrl(String entity) {

        try {

            if (!Arrays.asList("contract", "customer", "payment").contains(entity.toLowerCase())) {
                throw new InvalidParameterException(String.format("%s is not a valid entity; must be one of either 'contract', 'customer' or 'payment'.", entity));
            }

            Session createRequest = handler.session(settings);
            String sendRequest = createRequest.delete(String.format("BACS/%s/callback", entity));
            // Pass the return string to the handler. This will throw an exception if it is not what we expect.
            handler.genericExceptionCheck(sendRequest);

            if(sendRequest.contains("null")) {
                throw new ResourceNotFoundException("A callback URL has not been set.");
            } else {
                return "Callback URL deleted";
            }

        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public String payment(String contract, String payment, String comment) {

        try {

            if(comment.equals("")) {
                throw new EmptyRequiredParameterException("One or more required parameters are empty. Please double check all required parameters are filled and re-submit.");
            }

            // Create a new dictionary of parameters
            parameters = new HashMap<>();

            // Add method arguments to the parameters only if they are not empty
            try {
                parameters.put("comment", comment);
            } catch(IllegalArgumentException e) {
                throw new InvalidParameterException("There was an error adding one or more parameters to the call. Please try again, or contact help@eazycollect.co.uk");
            }

            Session createRequest = handler.session(settings);
            String sendRequest = createRequest.delete(String.format("contract/%s/payment/%s", contract, payment), parameters);
            handler.genericExceptionCheck(sendRequest);

            // If no customer were returned
            if(sendRequest.contains("Payment not found")) {
                return String.format("The payment %s either does not exist or has already been deleted", payment);
            } else {
                return String.format("Payment %s deleted", payment);
            }

        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }

    }

}
