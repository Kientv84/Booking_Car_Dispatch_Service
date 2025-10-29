package com.service.dispatch.service.impls;

import com.service.dispatch.dtos.respones.BookingResponse;
import com.service.dispatch.dtos.respones.DispatchLogResponse;
import com.service.dispatch.entities.DispatchEntity;
import com.service.dispatch.entities.DispatchLogEntity;
import com.service.dispatch.mappers.DispatchMapper;
import com.service.dispatch.repositories.DispatchLogRepository;
import com.service.dispatch.repositories.DispatchRepository;
import com.service.dispatch.service.DispatchLogService;
import com.service.dispatch.utils.StatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cache.internal.DisabledCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class DispatchLogServiceImpl implements DispatchLogService {
    private final DispatchLogRepository dispatchLogRepository;
    private final DispatchRepository dispatchRepository;
    private final DispatchMapper dispatchMapper;


    @Override
    public DispatchLogResponse createLog(BookingResponse bookingResponse, int cyle) {
        log.info("Creating log with cycle  {} " , cyle);
        DispatchLogEntity dispatchLogEntity = new DispatchLogEntity();
        dispatchLogEntity.setBookingId(bookingResponse.getBookingId());
        dispatchLogEntity.setStatus(bookingResponse.getStatus());
        dispatchLogEntity.setDispatchId(bookingResponse.getDispatchId());
        dispatchLogEntity.setCycle(cyle);
        if (bookingResponse.getDriver() != null) {
            dispatchLogEntity.setDriverId(bookingResponse.getDriver().getDriverId());
        }
        if (bookingResponse.getVehicle() != null) {
            dispatchLogEntity.setVehicleId(bookingResponse.getVehicle().getVehicleId());
        }
        DispatchLogEntity response = dispatchLogRepository.save(dispatchLogEntity);

        DispatchLogResponse dispatchLogResponse = dispatchMapper.mapFromDispatchLogEntity(response);
        return dispatchLogResponse;
    }

    // Hàm update status cho bookingId đã có
    public void updateLogStatus(Long bookingId, StatusEnum status) {
        log.info("Updating booking record with {} " , bookingId);
        DispatchEntity existing = dispatchRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Dispatch not found with bookingId " + bookingId));
        existing.setStatus(status);
        existing.setUpdatedAt(new Date());
        dispatchRepository.save(existing);
    }
}
