package com.project.weather.service;

import com.project.weather.client.NominatimClient;
import com.project.weather.client.OpenMeteoClient;
import com.project.weather.config.CacheConfig;
import com.project.weather.model.ForecastDto;
import com.project.weather.model.LocationDto;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForecastService {
    private final NominatimClient nominatimClient;
    private final OpenMeteoClient openMeteoClient;
    private final CacheManager cacheManager;

    public ForecastService(NominatimClient nominatimClient, OpenMeteoClient openMeteoClient, CacheManager cacheManager) {
        this.nominatimClient = nominatimClient;
        this.openMeteoClient = openMeteoClient;
        this.cacheManager = cacheManager;
    }

    public ForecastDto getForecast(String zipCode) {
        Cache cache = cacheManager.getCache(CacheConfig.FORECASTS_CACHE);
        if (cache != null) {
            ForecastDto cachedForecast = cache.get(zipCode, ForecastDto.class);
                if (cachedForecast != null) {
                    cachedForecast.setFromCache(true);
                    return cachedForecast;
                }
        }
        return fetchForecast(zipCode);
    }

    @CachePut(value = CacheConfig.FORECASTS_CACHE, key = "#zipCode")
    private ForecastDto fetchForecast(String zipCode) {
        List<LocationDto> locationResponse = nominatimClient.getCoordinates(zipCode);
        if (locationResponse == null || locationResponse.isEmpty()) {
            throw new RuntimeException(String.format("Location not found for zip code: %s", zipCode));
        }
        LocationDto locationDto = locationResponse.getFirst();
        ForecastDto forecastDto = openMeteoClient.getForecast(locationDto.lat(), locationDto.lon());

        if (forecastDto == null) {
            throw new RuntimeException(String.format("Could not get Weather Forecast for latitude: %s and longitude %s",
                    locationDto.lat(), locationDto.lon()));
        }

        return forecastDto;
    }
}
