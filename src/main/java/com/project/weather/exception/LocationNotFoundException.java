package com.project.weather.exception;

public class LocationNotFoundException extends RuntimeException {

    public LocationNotFoundException(String zipCode) {
        super(String.format("Location not found for zip code: %s", zipCode));
    }
}
