package uk.co.eazycollect.eazysdk.exceptions;

public class EmptyRequiredParameterException extends Exception {

    public EmptyRequiredParameterException() {
        super();
    }

    public EmptyRequiredParameterException(String formattedString, Object... args) {
        super(String.format(formattedString, args));
    }

}
