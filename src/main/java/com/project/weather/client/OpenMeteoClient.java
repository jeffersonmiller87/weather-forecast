package com.project.weather.client;

import com.project.weather.model.ForecastDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "openmeteo", url = "${app.openmeteo.url}")
public interface OpenMeteoClient {

    @GetMapping(value = "/forecast?current=temperature_2m&daily=temperature_2m_max,temperature_2m_min")
    ForecastDto getForecast(@RequestParam("latitude") String latitude, @RequestParam("longitude") String longitude);
}
