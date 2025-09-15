package com.service.dispatch.service.impls;

import com.fasterxml.jackson.core.type.TypeReference;
import com.service.dispatch.components.CaculatorComponent;
import com.service.dispatch.components.DispatchCreator;
import com.service.dispatch.dtos.respones.serviceResponse.DriverDTO;
import com.service.dispatch.dtos.respones.serviceResponse.VehicleResponse;
import com.service.dispatch.exceptions.DispatchServiceException;
import com.service.dispatch.integration.VehicleClient;
import com.service.dispatch.mappers.DispatchMapper;
import com.service.dispatch.repositories.DispatchRepository;
import com.service.dispatch.dtos.requests.BookingRequest;
import com.service.dispatch.dtos.respones.BookingResponse;
import com.service.dispatch.entities.DispatchEntity;
import com.service.dispatch.service.DispatchLogService;
import com.service.dispatch.service.RedisService;
import com.service.dispatch.utils.StatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import com.service.dispatch.service.DispatchService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        DispatchEntity dispatchEntityData = initDispatch(bookingRequest).getBody();

        BookingResponse mapToSaveLog = dispatchMapper.mapToBookingEntity(dispatchEntityData);
        log.info("Ghi log lần đầu");

        dispatchLogService.createLog(mapToSaveLog, 0);

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
                    throw new DispatchServiceException("D001", "D001");
                }

                // chỉ chạy khi vehicleAll != null
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

                List<VehicleResponse> filtered = vehicleDispatch.stream()
                        .filter(v -> bookingRequest.getVehicleType().equals(v.getVehicleType()))
                        .collect(Collectors.toList());

                if (filtered.isEmpty()) {
                    throw new DispatchServiceException("D002", "D002");
                }

                List<VehicleResponse> sortedVehicles = caculatorComponent.findVehiclesByLocation(
                        bookingRequest.getStartLatitude(),
                        bookingRequest.getStartLongitude(),
                        filtered
                );

                if (sortedVehicles.isEmpty()) {
                    throw new DispatchServiceException("D003", "D003");
                }

                VehicleResponse firstVehicle = sortedVehicles.get(0);

                try {
                    String action = (cycle == 3) ? "accept" : "reject";
                    Boolean isAccept = vehicleClient.isAcceptBooking(firstVehicle.getVehicleId(), action);

                    if (Boolean.TRUE.equals(isAccept)) {

                        //dispatchEntityData


                        BookingResponse response =  dispatchMapper.mapToBookingEntity(updateDispatch( dispatchEntityData,bookingRequest, firstVehicle,   StatusEnum.ACCEPTED));

                        redisService.deleteByKey("vehicles::dispatch");

                        response.setDriver(new DriverDTO(firstVehicle.getDriver().getDriverId(),
                                firstVehicle.getDriver().getName()));
                        response.setVehicle(new VehicleResponse(firstVehicle.getVehicleId(),
                                firstVehicle.getVehicleName(),
                                firstVehicle.getLicensePlate()));

                        try {
                            dispatchLogService.createLog(response, cycle);
                        } catch (Exception e) {
                            log.info("Have err" + e.getMessage());
                        }
                        mapToSaveLog.setStatus(StatusEnum.ACCEPT);

                        return response;
                    } else {
                        vehicleDispatch.removeIf(v -> v.getVehicleId().equals(firstVehicle.getVehicleId()));

                        updateDispatch(dispatchEntityData, null, null, StatusEnum.REJECTED);

                        BookingResponse response = new BookingResponse();
                        response.setBookingId(bookingRequest.getBookingId());
                        response.setDriver(new DriverDTO(firstVehicle.getDriver().getDriverId(),
                                firstVehicle.getDriver().getName()));
                        response.setVehicle(new VehicleResponse(firstVehicle.getVehicleId(),
                                firstVehicle.getVehicleName(),
                                firstVehicle.getLicensePlate()));
                        response.setStatus(StatusEnum.REJECTED);

                        try {
                            dispatchLogService.createLog(response, cycle);
                        } catch (Exception e) {
                            log.info("Have err" + e.getMessage());
                        }

                        redisService.setValue("vehicles::dispatch", vehicleDispatch, 3600);

                        if (vehicleDispatch.isEmpty()) {
                            redisService.deleteByKey("vehicles::dispatch");
                            throw new DispatchServiceException("D004", "D004");
                        }
                        cycle++;
                    }

                } catch (Exception e) {
                    throw new DispatchServiceException("D005", "D005");
                }
            }

            throw new DispatchServiceException("D004", "D004");

        } catch (DispatchServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new DispatchServiceException("D006", "D006");
        }
    }






    @Override
    public BookingResponse getDispatchById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<DispatchEntity> initDispatch( BookingRequest bookingRequest) {
        // init 1 record into db

        DispatchEntity dispatch = new DispatchEntity();

        dispatch.setBookingId(bookingRequest.getBookingId());
        dispatch.setStatus(StatusEnum.PENDING);

        DispatchEntity dispatchEntity = dispatchRepository.save(dispatch);


        return ResponseEntity.ok(dispatchEntity);

    }

    public DispatchEntity updateDispatch(DispatchEntity dispatchEntityData, BookingRequest bookingRequest, VehicleResponse vehicleResponse, StatusEnum statusEnum) {
        // Tìm record theo id
        DispatchEntity existing = dispatchRepository.findById(dispatchEntityData.getId())
                .orElseThrow(() -> new RuntimeException("Dispatch not found"));

        if ( bookingRequest != null) {

            existing.setLatitude(bookingRequest.getStartLatitude());
            existing.setLongitude(bookingRequest.getStartLongitude());
        }

        if (vehicleResponse != null ) {
            existing.setDriverId(vehicleResponse.getDriver().getDriverId());
            existing.setVehicleId(vehicleResponse.getVehicleId());
        }

        if ( statusEnum != null ) {
            existing.setStatus(statusEnum);
        }

        // Cập nhật các trường cần update
        existing.setStatus(dispatchEntityData.getStatus());
//

        // Lưu lại (sẽ gọi UPDATE chứ không phải INSERT)
        return dispatchRepository.save(existing);
    }
}


// Người dùng --> book xe ( request: loại xe --> nơi đón --> ... các yêu cầu khác )
// 1) Filter theo loại xe --> trả về các xe
// 2) Từ các xe đó Lấy vị trí xe --> logic tính khoảng cách từ điểm đón của người dùng đến địa điểm của xe đang đứng ( Tạm thời tính theo đường chim bay)
// xe nào hợp lệ thì trả về xe đó