package com.service.dispatch.service.impls;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.dispatch.components.CaculatorComponent;
import com.service.dispatch.components.CronJobSchedule;
import com.service.dispatch.components.DispatchCreator;
import com.service.dispatch.dtos.respones.DriverAcceptResponse;
import com.service.dispatch.dtos.respones.ResponseResults;
import com.service.dispatch.dtos.respones.serviceResponse.DriverBookingRespone;
import com.service.dispatch.dtos.respones.serviceResponse.DriverDTO;
import com.service.dispatch.dtos.respones.serviceResponse.VehicleResponse;
import com.service.dispatch.integration.VehicleClient;
import com.service.dispatch.mappers.DispatchMapper;
import com.service.dispatch.repositories.DispatchRepository;
import com.service.dispatch.dtos.requests.BookingRequest;
import com.service.dispatch.dtos.respones.BookingResponse;
import com.service.dispatch.entities.DispatchEntity;
import com.service.dispatch.service.DispatchLogService;
import com.service.dispatch.service.RedisService;
import com.service.dispatch.utils.StatusEnum;
import com.service.dispatch.utils.SuccessCode;
import feign.Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.service.dispatch.service.DispatchService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class DispatchServiceImpl implements DispatchService {

    private final DispatchMapper dispatchMapper;
    private final DispatchRepository dispatchRepository;
    private final VehicleClient vehicleClient;
    private final RedisService redisService;
    private final CaculatorComponent caculatorComponent;
    private final DispatchCreator dispatchCreator;


    private final DispatchLogService dispatchLogService;

    @Override
    public BookingResponse createDispatch(BookingRequest bookingRequest) {

        //TODO 1 : default tạo 1 record với status Pending ....

        DispatchEntity dispatchEntityData = initDispatch(bookingRequest).getBody();

        //TODO 2 : Lưu log cho lần dispatch này ....

        BookingResponse mapToSaveLog = dispatchMapper.mapToBookingEntity(dispatchEntityData);
        log.info("Ghi log lần đầu");

        dispatchLogService.createLog(mapToSaveLog, 0);

        //TODO 3 : Update lại Dispatch entity Gọi api create Dispatch thực hiện logic gọi tài xế ....

        try {
            List<VehicleResponse> vehicleDispatch = redisService.getValue(
                    "vehicles::dispatch",
                    new TypeReference<List<VehicleResponse>>() {}
            );

            if (vehicleDispatch == null || vehicleDispatch.isEmpty()) {
                List<VehicleResponse> vehicleAll = redisService.getValue(
                        "vehicles::all",
                        new TypeReference<List<VehicleResponse>>() {}
                );
                if (vehicleAll == null || vehicleAll.isEmpty()) {
                    return new BookingResponse( null, null,  null, null, null, null, null, "No vehicles available in Redis");
                }

                vehicleDispatch = new ArrayList<>(vehicleAll);
                redisService.setValue("vehicles::dispatch", vehicleDispatch, 3600);
            }

            int cycle = 1;

            while (!vehicleDispatch.isEmpty()) {
                log.info("=== Start cycle {} ===", cycle);

                vehicleDispatch = redisService.getValue(
                        "vehicles::dispatch",
                        new TypeReference<List<VehicleResponse>>() {}
                );

                if (vehicleDispatch == null || vehicleDispatch.isEmpty()) {
                    break;
                }

                // Filter theo type
                List<VehicleResponse> filtered = vehicleDispatch.stream()
                        .filter(v -> bookingRequest.getVehicleType().equals(v.getVehicleType()))
                        .collect(Collectors.toList());

                if (filtered.isEmpty()) {
                    return new BookingResponse( null, null, null, null, null, null, null,
                                    "No vehicle matched type: " + bookingRequest.getVehicleType());
                }

                // Filter theo location
                List<VehicleResponse> sortedVehicles = caculatorComponent.findVehiclesByLocation(
                        bookingRequest.getStartLatitude(),
                        bookingRequest.getStartLongitude(),
                        filtered
                );

                if (sortedVehicles.isEmpty()) {
                    return new BookingResponse( null, null, null, null, null, null, null,
                                    "No vehicle available after location filter");
                }

                VehicleResponse firstVehicle = sortedVehicles.get(0);

                try {
                    String action = (cycle == 3) ? "accept" : "reject";

                    //TODO: gọi api
                    Boolean isAccept = vehicleClient.isAcceptBooking(firstVehicle.getVehicleId(), action);

                    if (Boolean.TRUE.equals(isAccept)) {
                        // Driver accept --> Luu db

                       updateDispatch(dispatchEntityData);

                       BookingResponse response = dispatchCreator.createDispatch(bookingRequest, firstVehicle, StatusEnum.ACCEPTED);
                        redisService.deleteByKey("vehicles::dispatch");

                        DriverDTO driver = new DriverDTO(firstVehicle.getDriver().getDriverId(), firstVehicle.getDriver().getName());

                        VehicleResponse vehicle = new VehicleResponse(firstVehicle.getVehicleId(), firstVehicle.getVehicleName(), firstVehicle.getLicensePlate());

                        response.setDriver(driver);
                        response.setVehicle(vehicle);

                        // Luu log
                        try {
                            dispatchLogService.createLog(response, cycle);
                        } catch (Exception e) {
                            log.info("Have err" + e.getMessage());
                        }
                        mapToSaveLog.setStatus(StatusEnum.ACCEPT);

                        return response;
                    } else {
                        // Driver reject → remove khỏi list
                        vehicleDispatch.removeIf(v -> v.getVehicleId().equals(firstVehicle.getVehicleId()));


                        dispatchEntityData.setStatus(StatusEnum.REJECTED);
                        updateDispatch(dispatchEntityData);

                        // Ghi log

                        VehicleResponse vehicleResponse = new VehicleResponse(firstVehicle.getVehicleId(), firstVehicle.getVehicleName(), firstVehicle.getLicensePlate());
                        DriverDTO driverDTO = new DriverDTO(firstVehicle.getDriver().getDriverId(), firstVehicle.getDriver().getName());

                        BookingResponse response = new BookingResponse();
                        response.setBookingId(bookingRequest.getBookingId());
                        response.setDriver(driverDTO);
                        response.setVehicle(vehicleResponse);
                        response.setStatus(StatusEnum.REJECTED);

                        try {
                            dispatchLogService.createLog(response, cycle);
                        } catch (Exception e) {
                            log.info("Have err" + e.getMessage());
                        }

                                // Update lại cache
                        redisService.setValue("vehicles::dispatch", vehicleDispatch, 3600);

                        if (vehicleDispatch.isEmpty()) {
                            redisService.deleteByKey("vehicles::dispatch");
                            return new BookingResponse(  null, null, null,  null, null, null, null, "No driver accepted booking");
                        }

                        cycle++;
                    }

                } catch (Exception e) {
                    return new BookingResponse(  null, null, null, null, null, null, null,
                                    "Error while calling driver API: " + e.getMessage());
                }
            }

            return new BookingResponse(  null, null, null, null, null, null, null, "No driver accepted booking");

        } catch (Exception e) {
            return new BookingResponse( null, null, null, null, null,  null, null,
                            "Error while processing dispatch: " + e.getMessage());
        }
    }



    @Override
    public BookingResponse getDispatchById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<DispatchEntity> initDispatch(BookingRequest bookingRequest) {
        // init 1 record into db

        DispatchEntity dispatch = new DispatchEntity();

        dispatch.setBookingId(bookingRequest.getBookingId());
        dispatch.setStatus(StatusEnum.PENDING);

        DispatchEntity dispatchEntity = dispatchRepository.save(dispatch);


        return ResponseEntity.ok(dispatchEntity);

    }

    public DispatchEntity updateDispatch(DispatchEntity dispatchEntityData) {
        // Tìm record theo id
        DispatchEntity existing = dispatchRepository.findById(dispatchEntityData.getId())
                .orElseThrow(() -> new RuntimeException("Dispatch not found"));

        // Cập nhật các trường cần update
        existing.setStatus(dispatchEntityData.getStatus());
//        existing.setDriverId(dispatchEntityData.getDriverId());
//        existing.setLatitude(dispatchEntityData.getLatitude());
//        existing.setLongitude(dispatchEntityData.getLongitude());

        // Lưu lại (sẽ gọi UPDATE chứ không phải INSERT)
        return dispatchRepository.save(existing);
    }
}


// Người dùng --> book xe ( request: loại xe --> nơi đón --> ... các yêu cầu khác )
// 1) Filter theo loại xe --> trả về các xe
// 2) Từ các xe đó Lấy vị trí xe --> logic tính khoảng cách từ điểm đón của người dùng đến địa điểm của xe đang đứng ( Tạm thời tính theo đường chim bay)
// xe nào hợp lệ thì trả về xe đó