package uk.co.eazycollect.eazysdk.exceptions;

public class InvalidPaymentDateException extends Exception {

    public InvalidPaymentDateException() {
        super();
    }

    public InvalidPaymentDateException(String formattedString, Object... args) {
        super(String.format(formattedString, args));
    }

}
