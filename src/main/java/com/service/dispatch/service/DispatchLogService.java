package com.service.dispatch.service;

import com.service.dispatch.dtos.respones.BookingResponse;
import com.service.dispatch.entities.DispatchEntity;
import com.service.dispatch.entities.DispatchLogEntity;
import org.springframework.http.ResponseEntity;

public interface DispatchLogService {
    ResponseEntity<DispatchLogEntity> createLog(BookingResponse bookingResponse, int cycle);
}
