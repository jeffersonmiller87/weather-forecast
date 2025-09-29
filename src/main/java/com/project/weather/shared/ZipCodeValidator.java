package com.project.weather.shared;

import java.util.regex.Pattern;

public class ZipCodeValidator {

    private static final String ZIP_REGEX = "^(?=.*\\d)[\\d A-Za-z-]*$";
    private static final Pattern ZIP_PATTERN = Pattern.compile(ZIP_REGEX);

    private ZipCodeValidator() {}

    /**
     * Validates a string against the standard Zip code format.
     * @param zipCode The Zip Code string to validate.
     * @return true if the Zip Code is valid, false otherwise.
     */
    public static boolean isValidZipCode(String zipCode) {
        if (zipCode == null || zipCode.isBlank()) {
            return false;
        }

        return ZIP_PATTERN.matcher(zipCode.trim()).matches();
    }
}
