package com.project.weather.service;

import com.project.weather.model.CurrentDto;
import com.project.weather.model.ForecastDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ForecastServiceTest {

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache forecastCache;

    @Mock
    private FetchForecastService fetchForecastService;

    @InjectMocks
    private ForecastService forecastService;

    private static final String ZIP_CODE = "88220000";
    private static final Float CURRENT_TEMP = 15.5F;

    @BeforeEach
    void setUp() {
        when(cacheManager.getCache(anyString())).thenReturn(forecastCache);
    }

    @Test
    void getForecast_shouldFetchFromApiAndPutCache_whenCacheMisses() {
        when(forecastCache.get(ZIP_CODE, ForecastDto.class)).thenReturn(null);
        when(fetchForecastService.fetchForecast(anyString())).thenReturn(createMockForecastDto());

        ForecastDto result = forecastService.getForecast(ZIP_CODE);

        verify(fetchForecastService, times(1)).fetchForecast(anyString());

        assertEquals(CURRENT_TEMP, result.getCurrent().temperature_2m(), "Temperature must match.");
        assertFalse(result.isFromCache(), "Result must NOT be from cache on first call.");
    }

    @Test
    void getForecast_shouldReturnFromCache_whenCacheHits() {
        when(forecastCache.get(ZIP_CODE, ForecastDto.class)).thenReturn(createMockForecastDto());

        ForecastDto result = forecastService.getForecast(ZIP_CODE);

        verify(fetchForecastService, never()).fetchForecast(anyString());
        verify(forecastCache, never()).put(anyString(), any(ForecastDto.class));
        verify(forecastCache, times(1)).get(ZIP_CODE, ForecastDto.class);

        assertEquals(CURRENT_TEMP, result.getCurrent().temperature_2m(), "Temperature must match cached value.");
        assertTrue(result.isFromCache(), "Result must be from cache.");
    }

    private ForecastDto createMockForecastDto() {
        return new ForecastDto(new CurrentDto(CURRENT_TEMP), null, false);
    }
}