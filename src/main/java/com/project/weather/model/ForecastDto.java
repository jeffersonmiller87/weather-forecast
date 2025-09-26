package com.project.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ForecastDto {
    private CurrentDto current;
    private DailyDto daily;
    private boolean fromCache;
}
