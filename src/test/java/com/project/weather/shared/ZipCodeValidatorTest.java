package com.project.weather.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ZipCodeValidatorTest {

    @Test
    void isValidZipCode_shouldBeTrue_whenValidCodes() {
        assertTrue(ZipCodeValidator.isValidZipCode("88220000"));
        assertTrue(ZipCodeValidator.isValidZipCode("88220-000"));
        assertTrue(ZipCodeValidator.isValidZipCode("SW1A 0AA"));
        assertTrue(ZipCodeValidator.isValidZipCode("SW1A0AA"));
        assertTrue(ZipCodeValidator.isValidZipCode("90210"));
        assertTrue(ZipCodeValidator.isValidZipCode("90210-1234"));
        assertTrue(ZipCodeValidator.isValidZipCode("90210 1234"));
        assertTrue(ZipCodeValidator.isValidZipCode("123456"));
    }

    @Test
    void isValidZipCode_shouldBeFalse_whenNotValidCodes() {
        assertFalse(ZipCodeValidator.isValidZipCode("812.23"));
        assertFalse(ZipCodeValidator.isValidZipCode("812 - 23 /A"));
        assertFalse(ZipCodeValidator.isValidZipCode("812,23"));
        assertFalse(ZipCodeValidator.isValidZipCode("812/23"));
    }

    @Test
    void isValidZipCode_shouldBeFalse_whenNullOrBlankCodes() {
        assertFalse(ZipCodeValidator.isValidZipCode(null));
        assertFalse(ZipCodeValidator.isValidZipCode(""));
        assertFalse(ZipCodeValidator.isValidZipCode("   "));
        assertFalse(ZipCodeValidator.isValidZipCode(" - "));
    }
}