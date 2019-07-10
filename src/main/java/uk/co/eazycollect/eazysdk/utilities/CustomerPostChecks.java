package uk.co.eazycollect.eazysdk.utilities;

import uk.co.eazycollect.eazysdk.exceptions.InvalidParameterException;

import java.util.regex.Pattern;

public class CustomerPostChecks {

    /**
     * Check that a post code is formatted correctly. This does not verify the integrity of a post code, just that it could possibly exist. The responsibility of verifying a post code lies with the client.
     * @param postCode A post code provided by an external function
     * @return bool
     */
    public boolean checkPostCodeIsCorrectlyFormatted(String postCode) throws InvalidParameterException {
        Pattern search = Pattern.compile("^([A-Za-z][A-Ha-hJ-Yj-y]?[0-9][A-Za-z0-9]? ?[0-9][A-Za-z]{2}|[Gg][Ii][Rr] ?0[Aa]{2})$");
        boolean result = search.matcher(postCode).matches();

        if(!result) {
            throw new InvalidParameterException(String.format("%s is not a valid UK post code. Please double check the post code and re-submit.",
                    postCode));
        } else {
            return result;
        }

    }

    /**
     * Check an email address is formatted correctly. This will not verify the integrity of an email address, instead it verifies that an email address could exist.
     * The responsibility of verifying an email address lies with the client.
     * @param emailAddress An email address provided by an external function
     * @return bool
     */
    public boolean checkEmailAddressIsCorrectlyFormatted(String emailAddress) throws InvalidParameterException {
        Pattern search = Pattern.compile("(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$)");
        boolean result = search.matcher(emailAddress).matches();

        if(!result) {
            throw new InvalidParameterException(String.format("%s is not a valid email address. Please double check the email address and re-submit.",
                    emailAddress));
        } else {
            return result;
        }

    }

    /**
     * Check a bank account number is formatted correctly. This will not verify the integrity of a bank account number, and does not perform any mathematical checks on the account number.
     * The responsibility of verifying a bank account number lies with the client. We can offer a bank checking API to check the validity of bank details. For more information, contact our sales team on 01242 650052.
     * @param accountNumber A bank account number provided by an external function
     * @return bool
     */
    public boolean checkAccountNumberIsFormattedCorrectly(String accountNumber) throws InvalidParameterException {
        Pattern search = Pattern.compile("^[0-9]{8}$");
        boolean result = search.matcher(accountNumber).matches();

        if(!result) {
            throw new InvalidParameterException(String.format("%s is not a valid UK bank account number. Please double check that the account number is 8 characters long and only contains digits.",
                    accountNumber));
        } else {
            return result;
        }

    }

    /**
     * Check a bank sort code is formatted correctly. This will not verify the integrity of a bank sort code, and does not perform any mathematical checks on the sort code.
     * The responsibility of verifying a bank sort code lies with the client. We can offer a bank checking API to check the validity of bank details. For more information, contact our sales team on 01242 650052.
     * @param sortCode A bank sort code provided by an external function
     * @return bool
     */
    public boolean checkSortCodeIsFormattedCorrectly(String sortCode) throws InvalidParameterException {
        Pattern search = Pattern.compile("^[0-9]{6}$");
        boolean result = search.matcher(sortCode).matches();

        if(!result) {
            throw new InvalidParameterException(String.format("%s is not a valid UK bank sort code. Please double check that the sort code is 6 characters long and only contains digits.",
                    sortCode));
        } else {
            return result;
        }

    }

    /**
     * Check an account holder name is formatted correctly. This will not verify the account holder name of a UK bank account, instead it performs several checks to ensure the account holder name could be correct.
     * The responsibility of verifying a bank account holder name lies with the client.
     * @param accountHolderName An account holder name provided by an external function
     * @return bool
     */
    public boolean checkAccountHolderNameIsFormattedCorrectly(String accountHolderName) throws InvalidParameterException {
        Pattern search = Pattern.compile("^[A-Z0-9\\-\\/& ]{3,18}$");
        boolean result = search.matcher(accountHolderName.toUpperCase()).matches();

        if(!result) {
            throw new InvalidParameterException(String.format("%s is not formatted as a UK bank account holder name. A UK bank account holder name must be between 3 and 18 characters, contain only alphabetic characters(a-z), ampersands (&)," +
                    "hyphens (-), forward slashes (/) and spaces ( ). Please double check that the bank account holder name meets these criteria and re-submit.",
                    accountHolderName));
        } else {
            return result;
        }

    }

}
