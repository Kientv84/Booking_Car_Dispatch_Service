package com.service.dispatch.service.impls;

import com.service.dispatch.dtos.respones.serviceResponse.VehicleResponse;
import com.service.dispatch.integration.VehicleClient;
import com.service.dispatch.mappers.DispatchMapper;
import com.service.dispatch.repositories.DispatchRepository;
import com.service.dispatch.dtos.requests.BookingRequest;
import com.service.dispatch.dtos.respones.BookingResponse;
import com.service.dispatch.entities.DispatchEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.service.dispatch.service.DispatchService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DispatchServiceImpl implements DispatchService {

    private final DispatchMapper dispatchMapper;
    private final DispatchRepository dispatchRepository;
    private final VehicleClient vehicleClient;

    @Override
    public BookingResponse createDispatch(BookingRequest bookingRequest) {



        // Gọi service vehicle

        List<VehicleResponse> vehicles = Collections.emptyList();

        try {
            vehicles = vehicleClient.getAllVehicles();
            if (vehicles != null && !vehicles.isEmpty()) {
                log.info("Vehicle service returned: " + vehicles.size() + " vehicles");
                vehicles.forEach(v -> log.info(v.toString())); // log từng vehicle để quan sát
            } else {
                log.info("Vehicle service returned empty list");
            }
        } catch (Exception e) {
            log.warn("Get all from vehicle service failed!", e); // log cả stack trace
            vehicles = Collections.emptyList(); // fallback để code còn chạy
        }



        //

        DispatchEntity dispatch = new DispatchEntity();

        dispatch.setStartAt(bookingRequest.getStartAt());
        dispatch.setEndAt(bookingRequest.getEndAt());
        dispatch.setVehicleType(bookingRequest.getVehicleType());

        DispatchEntity newDispatch = dispatchRepository.save(dispatch);


        BookingResponse response = dispatchMapper.mapToBookingEntity(newDispatch);

        return response;
    }

    @Override
    public BookingResponse getDispatchById(Long id) {
        return null;
    }
}
