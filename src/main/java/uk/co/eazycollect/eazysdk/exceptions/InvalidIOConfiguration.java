package uk.co.eazycollect.eazysdk.exceptions;

public class InvalidIOConfiguration extends Exception {

    public InvalidIOConfiguration() {
        super();
    }

    public InvalidIOConfiguration(String formattedString, Object... args) {
        super(String.format(formattedString, args));
    }

}
