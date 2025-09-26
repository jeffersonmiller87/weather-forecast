package com.project.weather.service;

import com.project.weather.client.NominatimClient;
import com.project.weather.client.OpenMeteoClient;
import com.project.weather.model.CurrentDto;
import com.project.weather.model.ForecastDto;
import com.project.weather.model.LocationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ForecastServiceTest {

    @Mock
    private NominatimClient nominatimClient;

    @Mock
    private OpenMeteoClient openMeteoClient;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache forecastCache;

    @InjectMocks
    private ForecastService forecastService;

    private static final String ZIP_CODE = "88220000";
    private static final Float TEST_LAT = -25.5F;
    private static final Float TEST_LON = -40.0F;
    private static final Float CURRENT_TEMP = 15.5F;

    @BeforeEach
    void setUp() {
        when(cacheManager.getCache(anyString())).thenReturn(forecastCache);
    }

    @Test
    void getForecast_shouldFetchFromApiAndPutCache_whenCacheMisses() {
        when(forecastCache.get(ZIP_CODE)).thenReturn(null);
        when(nominatimClient.getCoordinates(anyString()))
                .thenReturn(createMockLocationDto());
        when(openMeteoClient.getForecast(anyString(), anyString()))
                .thenReturn(createMockForecastDto());

        ForecastDto result = forecastService.getForecast(ZIP_CODE);

        verify(nominatimClient, times(1)).getCoordinates(anyString());
        verify(openMeteoClient, times(1)).getForecast(anyString(), anyString());
        verify(forecastCache, times(1)).put(eq(ZIP_CODE), any(ForecastDto.class));

        assertEquals(CURRENT_TEMP, result.getCurrent().temperature_2m(), "Temperature must match.");
        assertFalse(result.isFromCache(), "Result must NOT be from cache on first call.");
    }

    @Test
    void getForecast_shouldReturnFromCache_whenCacheHits() {
        when(forecastCache.get(ZIP_CODE)).thenReturn(this::createMockForecastDto);

        ForecastDto result = forecastService.getForecast(ZIP_CODE);

        verify(nominatimClient, never()).getCoordinates(anyString());
        verify(openMeteoClient, never()).getForecast(anyString(), anyString());
        verify(forecastCache, never()).put(anyString(), any(ForecastDto.class));

        assertEquals(CURRENT_TEMP, result.getCurrent().temperature_2m(), "Temperature must match cached value.");
        assertTrue(result.isFromCache(), "Result must be from cache.");
    }

    @Test
    void getForecast_shouldThrowException_whenGeocodingFails() {
        when(forecastCache.get(ZIP_CODE)).thenReturn(null);
        when(nominatimClient.getCoordinates(anyString())).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> forecastService.getForecast(ZIP_CODE),
                "Should throw if nominatim get coordinates returns no results.");

        verify(openMeteoClient, never()).getForecast(anyString(), anyString());
    }

    private List<LocationDto> createMockLocationDto() {
        return List.of(new LocationDto(String.valueOf(TEST_LAT), String.valueOf(TEST_LON)));
    }

    private ForecastDto createMockForecastDto() {
        return new ForecastDto(new CurrentDto(CURRENT_TEMP), null, false);
    }
}