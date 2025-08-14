package service.impls;

import dtos.requests.BookingRequest;
import dtos.respones.BookingResponse;

public interface DispatchService {
    public BookingResponse createDispatch(BookingRequest bookingRequest);

    public BookingResponse getDispatchById(Long id);


}
