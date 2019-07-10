package uk.co.eazycollect.eazysdk.exceptions;

public class InvalidEnvironmentException extends Exception {

    public InvalidEnvironmentException() {
        super();
    }

    public InvalidEnvironmentException(String formattedString, Object... args) {
        super(String.format(formattedString, args));
    }

}
