package com.project.weather.controller;

import com.project.weather.model.ForecastDto;
import com.project.weather.service.ForecastService;
import com.project.weather.shared.ZipCodeValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/forecast")
public class ForecastController {
    private final ForecastService forecastService;

    public ForecastController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @GetMapping("/{zipCode}")
    public ResponseEntity<ForecastDto> getWeatherForecast(@PathVariable String zipCode) {
        if (zipCode == null || zipCode.isBlank() || !ZipCodeValidator.isValidZipCode(zipCode)) {
            throw new IllegalArgumentException(String.format("Invalid zip code: %s", zipCode));
        }

        ForecastDto forecast = forecastService.getForecast(zipCode);

        return ResponseEntity.ok(forecast);
    }
}
