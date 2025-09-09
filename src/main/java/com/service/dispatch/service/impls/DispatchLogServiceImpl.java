package com.service.dispatch.service.impls;

import com.service.dispatch.dtos.respones.BookingResponse;
import com.service.dispatch.entities.DispatchEntity;
import com.service.dispatch.entities.DispatchLogEntity;
import com.service.dispatch.repositories.DispatchLogRepository;
import com.service.dispatch.service.DispatchLogService;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.internal.DisabledCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DispatchLogServiceImpl implements DispatchLogService {
    private final DispatchLogRepository dispatchLogRepository;

    @Override
    public ResponseEntity<DispatchLogEntity> createLog(BookingResponse bookingResponse, int cyle) {

        DispatchLogEntity dispatchLogEntity = new DispatchLogEntity();

        dispatchLogEntity.setBookingId(bookingResponse.getBookingId());
        dispatchLogEntity.setStatus(bookingResponse.getStatus());
//        dispatchLogEntity.setDriverId(bookingResponse.getDriver().getDriverId());
        dispatchLogEntity.setDispatchId(bookingResponse.getDispatchId());
//        dispatchLogEntity.setVehicleId(bookingResponse.getVehicle().getVehicleId());
        dispatchLogEntity.setCycle(cyle);

        if (bookingResponse.getDriver() != null) {
            dispatchLogEntity.setDriverId(bookingResponse.getDriver().getDriverId());
        }

        if (bookingResponse.getVehicle() != null) {
            dispatchLogEntity.setVehicleId(bookingResponse.getVehicle().getVehicleId());
        }

        DispatchLogEntity response = dispatchLogRepository.save(dispatchLogEntity);

        return ResponseEntity.ok(response);
    }
}
