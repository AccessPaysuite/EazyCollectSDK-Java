package uk.co.eazycollect.eazysdk.exceptions;

public class RecordAlreadyExistsException extends Exception {

    public RecordAlreadyExistsException() {
        super();
    }

    public RecordAlreadyExistsException(String formattedString, Object... args) {
        super(String.format(formattedString, args));
    }

}
