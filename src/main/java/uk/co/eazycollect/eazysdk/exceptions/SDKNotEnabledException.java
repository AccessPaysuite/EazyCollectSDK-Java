package uk.co.eazycollect.eazysdk.exceptions;

public class SDKNotEnabledException extends Exception {

    public SDKNotEnabledException() {
        super();
    }

    public SDKNotEnabledException(String formattedString, Object... args) {
        super(String.format(formattedString, args));
    }

}
