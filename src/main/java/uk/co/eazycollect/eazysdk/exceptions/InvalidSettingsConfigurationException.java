package uk.co.eazycollect.eazysdk.exceptions;

public class InvalidSettingsConfigurationException extends Exception {

    public InvalidSettingsConfigurationException() {
        super();
    }

    public InvalidSettingsConfigurationException(String formattedString, Object... args) {
        super(String.format(formattedString, args));
    }

}
