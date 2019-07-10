package uk.co.eazycollect.eazysdk.exceptions;

public class InvalidStartDateException extends Exception {

    public InvalidStartDateException() {
        super();
    }

    public InvalidStartDateException(String formattedString, Object... args) {
        super(String.format(formattedString, args));
    }

}
