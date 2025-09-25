package com.service.dispatch.service;

import com.service.dispatch.dtos.requests.BookingRequest;
import com.service.dispatch.dtos.respones.BookingResponse;
import com.service.dispatch.dtos.respones.DispatchResponse;

public interface DispatchService {
     DispatchResponse createDispatch(BookingRequest bookingRequest);

     BookingResponse getDispatchById(Long id);


}
