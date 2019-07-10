package uk.co.eazycollect.eazysdk.exceptions;

public class ParameterNotAllowedException extends Exception {

    public ParameterNotAllowedException() {
        super();
    }

    public ParameterNotAllowedException(String formattedString, Object... args) {
        super(String.format(formattedString, args));
    }

}
