package com.service.dispatch.mappers;

import com.service.dispatch.dtos.respones.*;
import com.service.dispatch.dtos.respones.serviceResponse.VehicleResponse;
import com.service.dispatch.entities.DispatchEntity;
import com.service.dispatch.entities.DispatchLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DispatchMapper {
    @Mapping(target = "message", constant = "Dispatch created successfully")
    @Mapping(source = "id", target = "dispatchId")
    BookingResponse mapToBookingEntity(DispatchEntity dispatchEntity);
    DispatchLogResponse mapFromDispatchLogEntity(DispatchLogEntity dispatchLogEntity);
    DispatchResponse mapFromBookingResponse(BookingResponse dispatchEntityData);

}
