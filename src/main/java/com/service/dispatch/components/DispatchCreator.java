package com.service.dispatch.components;

import com.service.dispatch.dtos.requests.BookingRequest;
import com.service.dispatch.dtos.respones.BookingResponse;
import com.service.dispatch.dtos.respones.serviceResponse.VehicleResponse;
import com.service.dispatch.entities.DispatchEntity;
import com.service.dispatch.mappers.DispatchMapper;
import com.service.dispatch.repositories.DispatchRepository;
import com.service.dispatch.utils.StatusEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DispatchCreator {

    private final DispatchRepository dispatchRepository;
    private final DispatchMapper dispatchMapper;

    public BookingResponse createDispatch(BookingRequest bookingRequest, VehicleResponse vehicleResponse, StatusEnum status) {
        DispatchEntity dispatch = new DispatchEntity();
        dispatch.setBookingId(bookingRequest.getBookingId());
        dispatch.setLatitude(bookingRequest.getStartLatitude());
        dispatch.setLongitude(bookingRequest.getStartLongitude());
        dispatch.setStatus(status);
        dispatch.setDriverId(vehicleResponse.getDriver().getDriverId());
        dispatch.setVehicleId(vehicleResponse.getVehicleId());

//        // driver và vehicle nên lấy tách biệt
//        dispatch.setDriver(vehicleResponse.getDriver().getName());
//        dispatch.setVehicle(vehicleResponse.getVehicleName());

        DispatchEntity saved = dispatchRepository.save(dispatch);

        return dispatchMapper.mapToBookingEntity(saved);
    }
}
