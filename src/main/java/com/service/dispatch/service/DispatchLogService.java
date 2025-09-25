package com.service.dispatch.service;
import com.service.dispatch.dtos.respones.BookingResponse;
import com.service.dispatch.dtos.respones.DispatchLogResponse;
import com.service.dispatch.entities.DispatchLogEntity;
import com.service.dispatch.utils.StatusEnum;


public interface DispatchLogService {
    DispatchLogResponse createLog(BookingResponse bookingResponse, int cycle);

    void updateLogStatus(Long bookingId, StatusEnum stats);
}
