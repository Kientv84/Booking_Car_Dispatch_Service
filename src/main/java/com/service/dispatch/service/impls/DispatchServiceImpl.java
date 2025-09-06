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
import com.service.dispatch.dtos.respones.serviceResponse.VehicleResponse;
import com.service.dispatch.integration.VehicleClient;
import com.service.dispatch.mappers.DispatchMapper;
import com.service.dispatch.repositories.DispatchRepository;
import com.service.dispatch.dtos.requests.BookingRequest;
import com.service.dispatch.dtos.respones.BookingResponse;
import com.service.dispatch.entities.DispatchEntity;
import com.service.dispatch.service.RedisService;
import com.service.dispatch.utils.SuccessCode;
import feign.Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ObjectMapper objectMapper;
    private final CaculatorComponent caculatorComponent;
    private final DispatchCreator dispatchCreator;


    @Override
    public ResponseResults createDispatch(BookingRequest bookingRequest) {

        try {
            // Get dispatch from cache
            List<VehicleResponse> vehicleDispatch = redisService.getValue(
                    "vehicles::dispatch",
                    new TypeReference<List<VehicleResponse>>() {}
            );

            if (vehicleDispatch == null || vehicleDispatch.isEmpty()) {
                //Clone
                List<VehicleResponse> vehicleAll = redisService.getValue(
                        "vehicles::all",
                        new TypeReference<List<VehicleResponse>>() {}
                );
                if (vehicleAll == null || vehicleAll.isEmpty()) {
                    return new ResponseResults<>(SuccessCode.ERROR, "No vehicles available in Redis");
                }
                // Note nếu ko parse vehicleAll sang List và set thẳng vào redis, au đó thực hiện thao tác xóa xe khi driver reject:
                // Thì chính vehicleAll cũng bị xóa vì Java lưu trữ tham chiếu (reference) cho danh sách
                vehicleDispatch = new ArrayList<>(vehicleAll);
                redisService.setValue("vehicles::dispatch", vehicleDispatch, 3600);
                log.info("clone vehicles::all -> vehicles::dispatch with {} vehicles", vehicleDispatch.size());
            }

            int cycle = 1; // Đếm cycle
            boolean bookingDone = false;

            //TODO: --- CYCLE qua tất cả các xe ---
            while (!vehicleDispatch.isEmpty() && !bookingDone) {

                log.info("=== Start cycle {} ===", cycle);

                // Lấy lại dispatch từ Redis ...
                vehicleDispatch = redisService.getValue(
                        "vehicles::dispatch",
                        new TypeReference<List<VehicleResponse>>() {}
                );

                if (vehicleDispatch == null || vehicleDispatch.isEmpty()) {
                    log.info("No vehicles in dispatch, ending cycle");
                    break;
                }

                // Filter theo type
                List<VehicleResponse> filtered = vehicleDispatch.stream()
                        .filter(v -> bookingRequest.getVehicleType().equals(v.getVehicleType()))
                        .collect(Collectors.toList());

                if (filtered.isEmpty()) {
                    log.info("No vehicle matched type {} in cycle {}", bookingRequest.getVehicleType(), cycle);
                    return new ResponseResults<>(SuccessCode.ERROR,
                            "No vehicle matched type: " + bookingRequest.getVehicleType());
                }

                // Lọc theo vị trí
                List<VehicleResponse> sortedVehicles = caculatorComponent.findVehiclesByLocation(
                        bookingRequest.getStartLatitude(),
                        bookingRequest.getStartLongitude(),
                        filtered
                );

                if (sortedVehicles.isEmpty()) {
                    log.info("No vehicle available after location filter in cycle {}", cycle);
                    return new ResponseResults<>(SuccessCode.ERROR, "No vehicle available after location filter");
                }

                // Lấy xe đầu tiên ko cần loop
                VehicleResponse firstVehicle = sortedVehicles.get(0);

                Long vehicleId = firstVehicle.getVehicleId();
                double distance = caculatorComponent.calculateDistance(
                        bookingRequest.getStartLatitude(),
                        bookingRequest.getStartLongitude(),
                        firstVehicle.getLatitude(),
                        firstVehicle.getLongitude()
                );

                log.info("Cycle {}: Selected vehicle {} (Driver: {}) at distance {} km",
                        cycle,
                        firstVehicle.getVehicleName(),
                        firstVehicle.getDriver() != null ? firstVehicle.getDriver().getName() : "No Driver",
                        distance
                );

                try {
                    // TODO: call vehicle service --- driver accept/ reject api
                    Boolean isAccept = vehicleClient.isAcceptBooking(vehicleId, "reject");

                    if (Boolean.TRUE.equals(isAccept)) {
                        // Driver accept
                        dispatchCreator.createDispatch(bookingRequest, firstVehicle);
                        redisService.deleteByKey("vehicles::dispatch");
                        log.info("Cycle {}: Driver {} accepted booking", cycle, firstVehicle.getDriver().getName());

                        // Map data out put
                        DriverAcceptResponse driverAcceptResponse = dispatchMapper.mapVehicleToAcceptResponse(firstVehicle);

                        return new ResponseResults<>(
                                SuccessCode.SUCCESS,
                                " Driver " + firstVehicle.getDriver().getName() + " accepted booking" ,
                                driverAcceptResponse
                        );
                    } else {
                        // Driver reject
                        vehicleDispatch.removeIf(v -> v.getVehicleId().equals(vehicleId));
                        log.info("Cycle {}: Driver {} rejected booking. Remaining vehicles: {}",
                                cycle,
                                firstVehicle.getDriver() != null ? firstVehicle.getDriver().getName() : "No Driver",
                                vehicleDispatch.size()
                        );

                        // Update tọa độ mới từ vehicles::all
                        List<VehicleResponse> vehicleAll = redisService.getValue(
                                "vehicles::all",
                                new TypeReference<List<VehicleResponse>>() {}
                        );
                        if (vehicleAll != null) {
                            for (VehicleResponse v : vehicleDispatch) {
                                vehicleAll.stream()
                                        .filter(a -> a.getVehicleId().equals(v.getVehicleId()))
                                        .findFirst()
                                        .ifPresent(a -> {
                                            v.setLatitude(a.getLatitude());
                                            v.setLongitude(a.getLongitude());
                                        });
                            }
                        }

                        // Cập nhật lại cache
                        redisService.setValue("vehicles::dispatch", vehicleDispatch, 3600);

                        if (vehicleDispatch.isEmpty()) {
                            log.info("All vehicles rejected. Ending dispatch process.");
                            redisService.deleteByKey("vehicles::dispatch");
                            return new ResponseResults<>(SuccessCode.ERROR, "No driver accepted booking");
                        }

                        // Chuẩn bị cho cycle tiếp theo
                        cycle++;
                    }

                } catch (Exception e) {
                    log.error("Error while calling driver API: {}", e.getMessage(), e);
                    return new ResponseResults<>(SuccessCode.ERROR,
                            "Error while calling driver API: " + e.getMessage());
                }
            }

            // Hết xe mà không có ai accept
            return new ResponseResults<>(SuccessCode.ERROR, "No driver accepted booking");

        } catch (Exception e) {
            log.error("Error while processing dispatch: {}", e.getMessage(), e);
            return new ResponseResults<>(SuccessCode.ERROR, "Error while processing dispatch: " + e.getMessage());
        }
    }



    @Override
    public BookingResponse getDispatchById(Long id) {
        return null;
    }
}


// Người dùng --> book xe ( request: loại xe --> nơi đón --> ... các yêu cầu khác )
// 1) Filter theo loại xe --> trả về các xe
// 2) Từ các xe đó Lấy vị trí xe --> logic tính khoảng cách từ điểm đón của người dùng đến địa điểm của xe đang đứng ( Tạm thời tính theo đường chim bay)
// xe nào hợp lệ thì trả về xe đó