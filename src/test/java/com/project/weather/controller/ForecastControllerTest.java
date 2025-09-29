package com.project.weather.controller;

import com.project.weather.model.CurrentDto;
import com.project.weather.model.ForecastDto;
import com.project.weather.service.ForecastService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ForecastController.class)
@AutoConfigureMockMvc
class ForecastControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ForecastService forecastService;

    private static final String ZIP_CODE = "88220000";
    private static final Float CURRENT_TEMP = 15.5F;

    @Test
    void getForecast_shouldReturnForecast_andStatusOk_onSuccess() throws Exception {
        when(forecastService.getForecast(ZIP_CODE)).thenReturn(createMockForecastDto());

        mockMvc.perform(get("/api/forecast/{zipCode}", ZIP_CODE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(forecastService, times(1)).getForecast(ZIP_CODE);
    }

    @Test
    void getForecast_shouldReturnNotFound_onNullZipCode() throws Exception {
        mockMvc.perform(get("/api/forecast/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(forecastService, never()).getForecast(anyString());
    }

    @Test
    void getForecast_shouldReturnNotFound_onBlankZipCode() throws Exception {
        mockMvc.perform(get("/api/forecast/ ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());

        verify(forecastService, never()).getForecast(anyString());
    }

    @Test
    void getForecast_shouldReturnNotFound_onInvalidFormatZipCode() throws Exception {
        mockMvc.perform(get("/api/forecast/823.123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());

        verify(forecastService, never()).getForecast(anyString());
    }

    private ForecastDto createMockForecastDto() {
        return new ForecastDto(new CurrentDto(CURRENT_TEMP), null, false);
    }
}