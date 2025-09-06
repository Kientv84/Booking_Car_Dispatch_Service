package com.service.dispatch.mappers;

import com.service.dispatch.dtos.respones.BookingResponse;
import com.service.dispatch.dtos.respones.DriverAcceptResponse;
import com.service.dispatch.dtos.respones.serviceResponse.VehicleResponse;
import com.service.dispatch.entities.DispatchEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DispatchMapper {
    BookingResponse mapToBookingEntity(DispatchEntity dispatchEntity);

    DriverAcceptResponse mapVehicleToAcceptResponse(VehicleResponse vehicle);
}
