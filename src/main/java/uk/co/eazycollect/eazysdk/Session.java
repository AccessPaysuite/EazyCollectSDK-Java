package uk.co.eazycollect.eazysdk;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import uk.co.eazycollect.eazysdk.exceptions.InvalidEnvironmentException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Session {
    //region Client objects
    // The settings object, derives from ClientHandler
    private static Properties settings;
    //endregion

    //region HTTP objects
    // HttpController returns a HTTP client
    private static HttpController httpController;
    // This HttpClient will inherit from HttpController
    private static HttpClient httpClient;
    //endregion

    //region String objects
    // We use the StreamReader to read the results of the HttpRequestMessage
    private static InputStreamReader reader;
    // The environment used when communicating with EazyCustomerManager
    private static String environment;
    // The ApiKey passed into the headers of requests sent to EazyCustomerManager
    private static String apiKey;
    // The client code of the client sending requests to EazyCustomerManager;
    private static String clientCode;
    // The base Uri of requests sent to EazyCustomerManager. This relies on the Environment and the Client code
    private static String baseUri;
    // The target Uri endpoint of requests sent to ECM3
    private static String uriEndpoint;
    // The HttpMethod of requests sent to ECM3. This will be passed into HttpEncodedMethod.
    private static String httpMethod;
    // The full request Uri to be sent to EazyCustomerManager
    private static String requestUri;
    // The response string to be returns from the request sent to EazyCustomerManager
    private static String responseAsText;
    //endregion

    //region Dictionary objects
    // The parameters to be sent to EazyCustomerManager along with the request
    private static Map<String, String> parameters;
    //endregion

    /**
     * Instantiate the Session object
     * @param settings settings is called from ClientHandler as a static entity
     */
    public Session(Properties settings) throws Exception {
        httpController = new HttpController();
        httpClient = HttpClients.createDefault();

        try {
            environment = settings.getProperty("currentEnvironment.Environment");
        } catch(ClassCastException e) {
            throw new InvalidEnvironmentException("The environment setting could not be read. Please ensure this is formatted as a string.");
        }

        // Ensure the Environment is valid
        Set<String> acceptableEnvironments = new HashSet<>(Arrays.asList("sandbox", "ecm3"));

        if(!acceptableEnvironments.contains(environment.toLowerCase())) {
            throw new Exception(String.format("%s is not an acceptable environment.", environment));
        } else if(environment.toLowerCase().equals("ecm3")) {
            apiKey = settings.getProperty("ecm3ClientDetails.ApiKey");
            clientCode = settings.getProperty("ecm3ClientDetails.ClientCode");
        } else {
            apiKey = settings.getProperty("sandboxClientDetails.ApiKey");
            clientCode = settings.getProperty("sandboxClientDetails.ClientCode");
        }

    }

    public static String request(String method, String endpoint) throws Exception {
        return request(method, endpoint, null);
    }

    /**
     * Send a Request to ECM3. This function is never directly called, and is instead handled by the Get(), Post(), Patch() or Delete() functions
     * @param method The HTTP method passed from a HTTP function
     * @param endpoint The URI endpoint passed from a HTTP function
     * @param parameters The parameters, if there are any, passed from a HTTP function
     */
    public static String request(String method, String endpoint, Map<String, String> parameters) throws Exception {
        // The base Uri for all requests sent to EazyCustomerManager
        baseUri = String.format("https://%s.eazycollect.co.uk/api/v3/client/%s/", environment, clientCode);
        // The endpoint for the current request to be sent to EazyCustomerManager
        uriEndpoint = endpoint;
        // The base Uri for the current request
        requestUri = baseUri + uriEndpoint;
        // The HTTP method for the current request
        httpMethod = method;
        // The parameters for the current request
        Session.parameters = parameters;
        // Ensure the HTTP method is allowed by EazyCustomerManager
        Set<String> acceptableHTTPMethods = new HashSet<>(Arrays.asList("POST", "PATCH", "DELETE", "GET"));

        if(!acceptableHTTPMethods.contains(httpMethod)) {
            // Throw an exception if HTTP method is disallowed
            throw new Exception(String.format("%s isn't a HTTP method supported by EazySDK. The avaliable methods are GET, POST, PATCH and DELETE.",
                    httpMethod.toUpperCase()));
        }

        // Attempt to create the full request URL
        try {
            // Get a Uri object from the request Uri
            URIBuilder uriBuilder = new URIBuilder(requestUri);

            // Iterate over the Parameters and add them into the query object.
            for(Map.Entry<String, String> param : parameters.entrySet()) {
                uriBuilder.addParameter(param.getKey(), param.getValue());
            }

            // Update the Request Uri with the new query
            requestUri = uriBuilder.toString();
        } catch(NullPointerException ignored) {
            // If no parameters exist, continue
        }

        HttpRequestBase request;

        switch(httpMethod) {
            case "POST":
                request = new HttpPost(requestUri);
                break;
            case "PATCH":
                request = new HttpPatch(requestUri);
                break;
            case "DELETE":
                request = new HttpDelete(requestUri);
                break;
            default:
                request = new HttpGet(requestUri);
                break;
        }

        request.addHeader("ApiKey", apiKey);
        HttpResponse response = httpClient.execute(request);
        InputStream responseStream = response.getEntity().getContent();
        return IOUtils.toString(responseStream);
    }

    /**
     * Send a GET request to EazyCustomerManager
     */
    public String get(String endpoint) throws Exception {
        return request("GET", endpoint);
    }

    /**
     * Send a GET request with parameters to EazyCustomerManager
     */
    public String get(String endpoint, Map<String, String> parameters) throws Exception {
        return request("GET", endpoint, parameters);
    }

    /**
     * Send a POST request to EazyCustomerManager
     */
    public String post(String endpoint) throws Exception {
        return request("POST", endpoint);
    }

    /**
     * Send a POST request with parameters to EazyCustomerManager
     */
    public String post(String endpoint, Map<String, String> parameters) throws Exception {
        return request("POST", endpoint, parameters);
    }

    /**
     * Send a PATCH request to EazyCustomerManager
     */
    public String patch(String endpoint) throws Exception {
        return request("PATCH", endpoint);
    }

    /**
     * Send a PATCH request with parameters to EazyCustomerManager
     */
    public String patch(String endpoint, Map<String, String> parameters) throws Exception {
        return request("PATCH", endpoint, parameters);
    }

    /**
     * Send a DELETE request to EazyCustomerManager
     */
    public String delete(String endpoint) throws Exception {
        return request("DELETE", endpoint);
    }

    /**
     * Send a DELETE request with parameters to EazyCustomerManager
     */
    public String delete(String endpoint, Map<String, String> parameters) throws Exception {
        return request("DELETE", endpoint, parameters);
    }

}
