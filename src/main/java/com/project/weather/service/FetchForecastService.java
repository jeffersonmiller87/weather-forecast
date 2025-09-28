package com.project.weather.service;

import com.project.weather.client.NominatimClient;
import com.project.weather.client.OpenMeteoClient;
import com.project.weather.config.CacheConfig;
import com.project.weather.model.ForecastDto;
import com.project.weather.model.LocationDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FetchForecastService {
    private final NominatimClient nominatimClient;
    private final OpenMeteoClient openMeteoClient;

    public FetchForecastService(NominatimClient nominatimClient, OpenMeteoClient openMeteoClient) {
        this.nominatimClient = nominatimClient;
        this.openMeteoClient = openMeteoClient;
    }

    @Cacheable(value = CacheConfig.FORECASTS_CACHE, key = "#zipCode")
    public ForecastDto fetchForecast(String zipCode) {
        List<LocationDto> locationResponse = nominatimClient.getCoordinates(zipCode);
        if (locationResponse == null || locationResponse.isEmpty()) {
            throw new IllegalArgumentException(String.format("Location not found for zip code: %s", zipCode));
        }
        LocationDto locationDto = locationResponse.getFirst();
        ForecastDto forecastDto = openMeteoClient.getForecast(locationDto.lat(), locationDto.lon());

        if (forecastDto == null) {
            throw new IllegalArgumentException(String.format("Could not get Weather Forecast for latitude: %s and longitude %s",
                    locationDto.lat(), locationDto.lon()));
        }

        return forecastDto;
    }
}
