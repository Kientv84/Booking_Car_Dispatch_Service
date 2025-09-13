package com.service.dispatch.service;

import com.service.dispatch.dtos.requests.BookingRequest;
import com.service.dispatch.dtos.respones.BookingResponse;
import com.service.dispatch.dtos.respones.ResponseResults;
import com.service.dispatch.entities.DispatchEntity;
import com.service.dispatch.entities.DispatchLogEntity;
import com.service.dispatch.utils.StatusEnum;
import org.springframework.http.ResponseEntity;

public interface DispatchService {
//     ResponseResults createDispatch(BookingRequest bookingRequest);

     BookingResponse createDispatch(BookingRequest bookingRequest);

     BookingResponse getDispatchById(Long id);

     DispatchEntity updateDispatch(DispatchEntity dispatchEntity);

     ResponseEntity<DispatchEntity> initDispatch(BookingRequest bookingRequest);

}
