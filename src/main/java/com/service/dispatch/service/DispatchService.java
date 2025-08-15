package com.service.dispatch.service;

import com.service.dispatch.dtos.requests.BookingRequest;
import com.service.dispatch.dtos.respones.BookingResponse;

public interface DispatchService {
    public BookingResponse createDispatch(BookingRequest bookingRequest);

    public BookingResponse getDispatchById(Long id);


}
