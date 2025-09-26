package com.project.weather.model;

import java.util.List;

public record DailyDto(List<String> time, List<Float> temperature_2m_min, List<Float> temperature_2m_max) {}
