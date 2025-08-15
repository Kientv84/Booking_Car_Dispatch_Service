package com.service.dispatch.service.impls;

import com.service.dispatch.mappers.DispatchMapper;
import com.service.dispatch.repositories.DispatchRepository;
import com.service.dispatch.dtos.requests.BookingRequest;
import com.service.dispatch.dtos.respones.BookingResponse;
import com.service.dispatch.entities.DispatchEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.service.dispatch.service.DispatchService;

@Component
@RequiredArgsConstructor
public class DispatchServiceImpl implements DispatchService {

    private final DispatchMapper dispatchMapper;
    private final DispatchRepository dispatchRepository;

    @Override
    public BookingResponse createDispatch(BookingRequest bookingRequest) {

        System.out.println("bookingRequest" + bookingRequest.toString());

        DispatchEntity dispatch = new DispatchEntity();

        dispatch.setStartAt(bookingRequest.getStartAt());
        dispatch.setEndAt(bookingRequest.getEndAt());
        dispatch.setVehicleType(bookingRequest.getVehicleType());

        DispatchEntity newDispatch = dispatchRepository.save(dispatch);

        System.out.println("newDispatch" + newDispatch.toString());

        BookingResponse response = dispatchMapper.mapToBookingEntity(newDispatch);

        return response;
    }

    @Override
    public BookingResponse getDispatchById(Long id) {
        return null;
    }
}
