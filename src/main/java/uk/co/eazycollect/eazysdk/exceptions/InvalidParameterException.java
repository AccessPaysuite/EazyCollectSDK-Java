package uk.co.eazycollect.eazysdk.exceptions;

public class InvalidParameterException extends Exception {

    public InvalidParameterException() {
        super();
    }

    public InvalidParameterException(String formattedString, Object... args) {
        super(String.format(formattedString, args));
    }

}
