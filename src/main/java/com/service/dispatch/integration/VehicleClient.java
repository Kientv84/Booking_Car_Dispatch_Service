package com.service.dispatch.integration;

import com.service.dispatch.dtos.respones.ApiResponse;
import com.service.dispatch.dtos.respones.serviceResponse.VehicleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(  name = "${openfeign.vehicleClient.name}",
        url = "${spring.cloud.vehicleClient.client.config.vehicleClient.url}")
public interface VehicleClient {
    @GetMapping(
            value = "${openfeign.vehicleClient.url.get-all-vehicle}",
            consumes = "application/json")
//    ResponseEntity<List<VehicleResponse>> getAllVehicles();
    List<VehicleResponse> getAllVehicles();

    @PutMapping(
            value = "${openfeign.vehicleClient.url.driver-conform}",
            consumes = "application/json")
//    ResponseEntity<Boolean> isAcceptBooking(@PathVariable Long driverId, @RequestParam String action);
    ResponseEntity<ApiResponse<Boolean>> isAcceptBooking(@PathVariable Long driverId, @RequestParam String action);
}
