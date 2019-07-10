package uk.co.eazycollect.eazysdk;

import uk.co.eazycollect.eazysdk.exceptions.ResourceNotFoundException;
import uk.co.eazycollect.eazysdk.exceptions.SDKNotEnabledException;
import uk.co.eazycollect.eazysdk.exceptions.UnsupportedHTTPMethodException;

import java.util.Properties;

public class ClientHandler {

    public Properties settings() {
        return SettingsManager.createSettings();
    }

    public Session session(Properties settings) throws Exception {
        return new Session(settings);
    }

    public String genericExceptionCheck(String result) throws UnsupportedHTTPMethodException, SDKNotEnabledException,
            ResourceNotFoundException {

        if(result.contains("not supported")) {
            throw new UnsupportedHTTPMethodException(
                    String.format("This is a generic error. This error can be caused by several events, including but not limited to:" +
                            "%n- The incorrect HTTP method is being used. For example, you cannot pass DELETE on a Customer object." +
                            "%n- The correct HTTP method is being used, but a mandatory parameter has been missed."));
        } else if(result.contains("API not enabled")) {
            throw new SDKNotEnabledException(
                    String.format("This is a generic error. This error can be caused by several events, including but not limited to:" +
                            "%n- The API key is either not valid or is not correct." +
                            "%n- The API key is correct and valid, but the incorrect client code is being used." +
                            "%n - The record you are attempting to access does not exist."));
        } else if(result.contains("IIS 8.5 Detailed Error - 404.0 - Not Found")) {
            throw new ResourceNotFoundException(
                    String.format("This is a generic error. This error can be caused by several events, including but not limited to:" +
                            "%n- You are searching against a record that does not exist." +
                            "%n- The record you are searching for does exist, but a mandatory parameter has been missed." +
                            "%n - The provided data is malformed."));
        } else {
            return result;
        }

    }

}
