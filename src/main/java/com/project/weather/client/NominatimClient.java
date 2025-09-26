package com.project.weather.client;

import com.project.weather.model.LocationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "nominatim", url = "${app.nominatin.url}")
public interface NominatimClient {

    @GetMapping(value = "/search?format=json&limit=1&postalcode={postalcode}")
    List<LocationDto> getCoordinates(@PathVariable("postalcode") String postalcode);
}
