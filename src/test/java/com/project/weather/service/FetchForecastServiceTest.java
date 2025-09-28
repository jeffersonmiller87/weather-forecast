package com.project.weather.service;

import com.project.weather.client.NominatimClient;
import com.project.weather.client.OpenMeteoClient;
import com.project.weather.model.CurrentDto;
import com.project.weather.model.ForecastDto;
import com.project.weather.model.LocationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchForecastServiceTest {

    @Mock
    private NominatimClient nominatimClient;

    @Mock
    private OpenMeteoClient openMeteoClient;

    @InjectMocks
    private FetchForecastService fetchForecastService;

    private static final String ZIP_CODE = "88220000";
    private static final Float TEST_LAT = -25.5F;
    private static final Float TEST_LON = -40.0F;
    private static final Float CURRENT_TEMP = 15.5F;

    @Test
    void fetchForecast_shouldFetchFromApi_successfully() {
        when(nominatimClient.getCoordinates(anyString()))
                .thenReturn(createMockLocationDto());
        when(openMeteoClient.getForecast(anyString(), anyString()))
                .thenReturn(createMockForecastDto());

        ForecastDto result = fetchForecastService.fetchForecast(ZIP_CODE);

        verify(nominatimClient, times(1)).getCoordinates(anyString());
        verify(openMeteoClient, times(1)).getForecast(anyString(), anyString());

        assertEquals(CURRENT_TEMP, result.getCurrent().temperature_2m(), "Temperature must match.");
    }

    @Test
    void getForecast_shouldThrowException_whenGeocodingFails() {
        when(nominatimClient.getCoordinates(anyString())).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> fetchForecastService.fetchForecast(ZIP_CODE),
                "Should throw if nominatim get coordinates returns no results.");

        verify(openMeteoClient, never()).getForecast(anyString(), anyString());
    }

    @Test
    void getForecast_shouldThrowException_whenForecastFails() {
        when(nominatimClient.getCoordinates(anyString())).thenReturn(createMockLocationDto());
        when(openMeteoClient.getForecast(anyString(), anyString())).thenReturn(null);

        assertThrows(RuntimeException.class, () -> fetchForecastService.fetchForecast(ZIP_CODE),
                "Should throw if openMeteo forecast returns no results.");

        verify(nominatimClient, times(1)).getCoordinates(anyString());
        verify(openMeteoClient, times(1)).getForecast(anyString(), anyString());
    }

    private List<LocationDto> createMockLocationDto() {
        return List.of(new LocationDto(String.valueOf(TEST_LAT), String.valueOf(TEST_LON)));
    }

    private ForecastDto createMockForecastDto() {
        return new ForecastDto(new CurrentDto(CURRENT_TEMP), null, false);
    }
}