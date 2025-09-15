package com.service.dispatch.service.impls;

import com.service.dispatch.dtos.respones.BookingResponse;
import com.service.dispatch.entities.DispatchEntity;
import com.service.dispatch.entities.DispatchLogEntity;
import com.service.dispatch.repositories.DispatchLogRepository;
import com.service.dispatch.repositories.DispatchRepository;
import com.service.dispatch.service.DispatchLogService;
import com.service.dispatch.utils.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.internal.DisabledCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class DispatchLogServiceImpl implements DispatchLogService {
    private final DispatchLogRepository dispatchLogRepository;
    private final DispatchRepository dispatchRepository;
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

    // Hàm update status cho bookingId đã có
    public void updateLogStatus(Long bookingId, StatusEnum status) {
        DispatchEntity existing = dispatchRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Dispatch not found with bookingId " + bookingId));

        existing.setStatus(status);
        existing.setUpdatedAt(new Date());


        dispatchRepository.save(existing);
    }
}
