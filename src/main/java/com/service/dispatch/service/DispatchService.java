package com.service.dispatch.service;

import com.service.dispatch.dtos.requests.BookingRequest;
import com.service.dispatch.dtos.respones.BookingResponse;
import com.service.dispatch.dtos.respones.ResponseResults;

public interface DispatchService {
    public ResponseResults createDispatch(BookingRequest bookingRequest);

    public BookingResponse getDispatchById(Long id);


}
