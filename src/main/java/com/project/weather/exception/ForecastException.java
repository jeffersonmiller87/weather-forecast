package com.project.weather.exception;

import lombok.Getter;

@Getter
public class ForecastException extends RuntimeException {
    private final String message;

    /**
     * Constructor with a message to the exception.
     *
     * @param message the error message
     */
    public ForecastException(String message) {
        super(message);
        this.message = message;
    }
}
