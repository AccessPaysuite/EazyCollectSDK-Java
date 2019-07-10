package uk.co.eazycollect.eazysdk.exceptions;

public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String formattedString, Object... args) {
        super(String.format(formattedString, args));
    }

}
