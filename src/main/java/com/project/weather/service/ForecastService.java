package com.project.weather.service;

import com.project.weather.config.CacheConfig;
import com.project.weather.model.ForecastDto;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class ForecastService {
    private final CacheManager cacheManager;
    private final FetchForecastService fetchForecastService;

    public ForecastService(CacheManager cacheManager, FetchForecastService fetchForecastService) {
        this.cacheManager = cacheManager;
        this.fetchForecastService = fetchForecastService;
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
        return fetchForecastService.fetchForecast(zipCode);
    }
}
