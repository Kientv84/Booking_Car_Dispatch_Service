package com.service.dispatch.integration;

import com.service.dispatch.dtos.respones.serviceResponse.VehicleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(  name = "${openfeign.vehicleClient.name}",
        url = "${spring.cloud.vehicleClient.client.config.vehicleClient.url}")
public interface VehicleClient {
    @GetMapping(
            value = "${openfeign.vehicleClient.url.get-all-vehicle}",
            consumes = "application/json")
    List<VehicleResponse> getAllVehicles();
}
