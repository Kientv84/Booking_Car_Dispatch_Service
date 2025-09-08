package com.service.dispatch.service;

import com.service.dispatch.dtos.requests.BookingRequest;
import com.service.dispatch.dtos.respones.BookingResponse;
import com.service.dispatch.dtos.respones.ResponseResults;
import com.service.dispatch.entities.DispatchEntity;
import com.service.dispatch.entities.DispatchLogEntity;
import org.springframework.http.ResponseEntity;

public interface DispatchService {
//     ResponseResults createDispatch(BookingRequest bookingRequest);

     ResponseEntity<BookingResponse> createDispatch(BookingRequest bookingRequest);

     BookingResponse getDispatchById(Long id);

     ResponseEntity<DispatchEntity> initDispatch(BookingRequest bookingRequest);

}
