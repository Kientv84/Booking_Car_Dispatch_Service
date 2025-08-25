package com.service.dispatch.FakeApi;

import com.service.dispatch.dtos.respones.serviceResponse.DriverBookingRespone;
import com.service.dispatch.dtos.respones.serviceResponse.DriverDTO;
import com.service.dispatch.dtos.respones.serviceResponse.VehicleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class FakeApi {

    @Bean
    public DriverBookingRespone conformBooking(List<VehicleResponse> listVehicleResponse) {
        log.info("Call fake driver API with {} vehicles", listVehicleResponse.size());

        Random random = new Random();

        // Loop qua từng xe
        for (VehicleResponse vehicleResponse : listVehicleResponse) {

            // loop qua drivers của xe
            for (DriverDTO driver : vehicleResponse.getDrivers()) {
                boolean accepted = random.nextBoolean(); // random accept/reject

                if (accepted) {
                    log.info("Driver {} (vehicleId={}) accepted booking", driver.getDriverId(), vehicleResponse.getVehicleId());
                    return new DriverBookingRespone(true, driver); // trả về driver accept
                } else {
                    log.info("Driver {} (vehicleId={}) rejected booking", driver.getDriverId(), vehicleResponse.getVehicleId());
                }
            }
        }

        // Nếu duyệt hết mà không có ai accept
        log.warn("No driver accepted booking ");
        return new DriverBookingRespone(false, null);
    }

}
