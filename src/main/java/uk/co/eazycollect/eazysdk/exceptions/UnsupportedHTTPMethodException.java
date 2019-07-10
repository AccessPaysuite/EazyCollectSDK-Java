package uk.co.eazycollect.eazysdk.exceptions;

public class UnsupportedHTTPMethodException extends Exception {

    public UnsupportedHTTPMethodException() {
        super();
    }

    public UnsupportedHTTPMethodException(String formattedString, Object... args) {
        super(String.format(formattedString, args));
    }

}
