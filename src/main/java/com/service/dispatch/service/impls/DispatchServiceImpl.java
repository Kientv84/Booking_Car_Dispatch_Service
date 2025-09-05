package com.service.dispatch.service.impls;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.dispatch.components.CaculatorComponent;
import com.service.dispatch.components.CronJobSchedule;
import com.service.dispatch.components.DispatchCreator;
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

        // TODO: get data from cache ...
        try {
            List<VehicleResponse> vehicleCache = redisService.getValue("vehicles::all",new TypeReference<List<VehicleResponse>>() {});
            log.info("Get redis data success" + vehicleCache.toString());

            // TODO: search vehicle ...
            // TODO: Cycle 1 ...

            if (vehicleCache == null) {
                return new ResponseResults<>(SuccessCode.ERROR, "Data is not exist");
            }

            List<VehicleResponse> filtered = vehicleCache.stream()
                    .filter(v -> bookingRequest.getVehicleType().equals(v.getVehicleType()))
                    .collect(Collectors.toList());

            if (filtered.isEmpty()) {
                return new ResponseResults<>(SuccessCode.ERROR, "Not vehicle matched type: "
                        + bookingRequest.getVehicleType());
            }

            // B2: Lọc theo vị trí
            List<VehicleResponse> sortedVehicles = caculatorComponent.findVehiclesByLocation(
                    bookingRequest.getStartLatitude(),
                    bookingRequest.getStartLongitude(),
                    filtered
            );

            sortedVehicles.forEach(vehicle ->
                    log.info("VehicleId: {}, VehicleName: {}, Distance: {} km",
                            vehicle.getVehicleId(),
                            vehicle.getVehicleName(),
                            caculatorComponent.calculateDistance(
                                    bookingRequest.getStartLatitude(),
                                    bookingRequest.getStartLongitude(),
                                    vehicle.getLatitude(),
                                    vehicle.getLongitude()
                            )
                    )
            );

            // TODO: --- Gọi api driver ---
            for (VehicleResponse vehicleResponse : sortedVehicles) {
                Long idDrvier = vehicleResponse.getVehicleId();

                try {
                    Boolean isAccept = vehicleClient.isAcceptBooking(idDrvier, "reject");

                    if (isAccept != null) {
                        log.info("please wait driver " + vehicleResponse.getDriver().getName() + " is response");
                    }

                    // Check status booking
                    if (Boolean.TRUE.equals(isAccept)) { // Driver accept booking -> Lưu DB
                        BookingResponse disPatchResponse = dispatchCreator.createDispatch(bookingRequest, vehicleResponse);

                        // Xóa cache
                        redisService.deleteByKey("vehicles::all");

                        return new ResponseResults<>(
                                SuccessCode.SUCCESS,
                                "Driver " + vehicleResponse.getVehicleName() + " accepted booking",
                                vehicleResponse // trả thêm thông tin vehicle
                        );
                    } else {
                        // Nếu driver từ chối thì xóa khỏi redis và tiếp tục vòng lặp
                        log.info("Driver " + vehicleResponse.getDriver().getName() + " rejected booking");

                        if (sortedVehicles != null) {
                            sortedVehicles = sortedVehicles.stream()
                                    .filter(v -> !v.getVehicleId().equals(vehicleResponse.getVehicleId()))
                                    .collect(Collectors.toList());

                            // Update lại redis
                            redisService.setValue("vehicles::all", sortedVehicles, 3600);
                        }

                        // TODO: thực hiện tiếp cycle 2 ...


                    }
                } catch (Exception e) {
                    return new ResponseResults<>(SuccessCode.ERROR, "Have not data in cache" + e);
                }
            }
            return null;

        } catch (Exception e) {
            return new ResponseResults<>(SuccessCode.ERROR, "Error while getting data: " + e.getMessage());
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

//@Override
//public ResponseResults createDispatch(BookingRequest bookingRequest) {
//    log.info("Start get create dispatch ...");
//
////TODO: Gọi service vehicle ...
//
//    List<VehicleResponse> vehicles = Collections.emptyList();
//
//    try {
//
////                ResponseEntity<List<VehicleResponse>> response = vehicleClient.getAllVehicles();
//
//        vehicles = vehicleClient.getAllVehicles();
//
//        log.info("vehicles" + vehicles.toString());
//
//
//        if (vehicles == null && vehicles.isEmpty()) {
//            log.warn("Vehicle service returned empty list");
//            return new ResponseResults<>(SuccessCode.ERROR, "create dispatch fail - no vehicles available");
//        } else {
//            log.info("Vehicle service called successfull!!");
//
//            // Lưu vào cache (Redis)
//
//            redisService.setValue("vehicles::all", vehicles, 3600);
//
//        }
//    } catch (Exception e) {
//        log.warn("Get all from vehicle service failed!", e); // log cả stack trace
//        vehicles = Collections.emptyList(); // fallback để code còn chạy
//    }
//
////TODO: Logic filter ....
//
//    List<VehicleResponse> vehiclesCache =  redisService.getValue("vehicles::all", new TypeReference<List<VehicleResponse>>() {});
//
//    if (vehiclesCache == null) { return  new ResponseResults<>(SuccessCode.ERROR, "Have error");};
//
//    List<VehicleResponse> filtered = vehiclesCache.stream()
//            .filter(v -> bookingRequest.getVehicleType().equals(v.getVehicleType().getName()))
//            .collect(Collectors.toList());
//
//    if (filtered.isEmpty()) {
//        return new ResponseResults<>(SuccessCode.ERROR, "create dispatch fail - no vehicle matched type: "
//                + bookingRequest.getVehicleType());
//    }
//
//
//    // B2: Lọc theo vị trí
//
//    List<VehicleResponse> sortedVehicles = caculatorComponent.findVehiclesByLocation(bookingRequest.getStartLatitude(), bookingRequest.getStartLongitude(), filtered);
//
//    if (sortedVehicles.isEmpty()) {
//        return new ResponseResults<>(SuccessCode.ERROR,
//                "create dispatch fail - no vehicle found by location");
//    }
//
//
//    // Log danh sách xe và khoảng cách
//    sortedVehicles.forEach(vehicle ->
//            log.info("VehicleId: {}, VehicleName: {}, Distance: {} km",
//                    vehicle.getVehicleId(),
//                    vehicle.getVehicleName(),
//                    caculatorComponent.calculateDistance(bookingRequest.getStartLatitude(), bookingRequest.getStartLongitude(), vehicle.getLatitude(), vehicle.getLongitude()))  // đảm bảo trong VehicleResponse có field distance
//    );
//
//
//
////TODO: --- Gọi api driver ---
//
//
//    for (VehicleResponse vehicleResponse : sortedVehicles) {
//
//        Long idDrvier = vehicleResponse.getVehicleId();
//
//        try {
//
////                ResponseEntity<Boolean> response = vehicleClient.isAcceptBooking(idDrvier, "pending" );
//
//            Boolean isAccept = vehicleClient.isAcceptBooking(idDrvier, "pending" );
//
//            if ( isAccept != null ) {
//                log.info("please wait driver" + vehicleResponse.getVehicleName() + " is response");
//            }
//
//            // Check status booking
//
//            if ( isAccept == true ) {  // Driver accept booking -> Lưu DB
//                DispatchEntity dispatch = new DispatchEntity();
//                dispatch.setLatitude(bookingRequest.getStartLatitude());
//                dispatch.setLongitude(bookingRequest.getStartLongitude());
//                dispatch.setVehicleType(bookingRequest.getVehicleType());
//                dispatch.setDriver(vehicleResponse.getVehicleName());
//                dispatch.setVehicle(vehicleResponse.getVehicleName());
//
//                DispatchEntity newDispatch = dispatchRepository.save(dispatch);
//
//                BookingResponse disPatchResponse = dispatchMapper.mapToBookingEntity(newDispatch);
//
//                // Xóa cache
//                redisService.deleteByKey("vehicles::all");
//
//                return new ResponseResults<>(
//                        SuccessCode.SUCCESS,
//                        "Driver " + vehicleResponse.getVehicleName() + " accepted booking",
//                        vehicleResponse,   // trả thêm thông tin vehicle
//                        disPatchResponse   // trả thêm thông tin booking đã lưu
//                ); } else {
//
//                // Nếu driver từ chối thì xóa khỏi redis và tiếp tục vòng lặp
//                log.warn("Driver {} ({}) rejected booking", vehicleResponse.getVehicleId(), vehicleResponse.getVehicleName());
//
//                // Xóa driver này khỏi cache Redis
//                List<VehicleResponse> cachedVehicles = redisService.getValue("vehicles::all", List.class);
//                if (cachedVehicles != null) {
//                    cachedVehicles = cachedVehicles.stream()
//                            .filter(v -> !v.getVehicleId().equals(vehicleResponse.getVehicleId()))
//                            .collect(Collectors.toList());
//
//                    // Update lại redis
//                    redisService.setValue("vehicles::all", cachedVehicles, 3600);
//                }
//
//                //TODO: tiếp tục Cycle 2
//
//            }
//
//
//
//        } catch (Exception e) {
//            return new ResponseResults<>(SuccessCode.ERROR, "Call api driver fail with" + e.getMessage());
//        }
//
//
//    }
//
////        DriverBookingRespone driverDecision = cronJobSchedule.scheduleDriverDecision(sortedVehicles);
////
////        if (!driverDecision.isAccepted()) {
////            log.warn("Driver rejected booking");
////            return new ResponseResults<>(SuccessCode.ERROR,
////                    "create dispatch fail - No driver accept this booking");
////        }
//
//
////        // Nếu driver accept thì mới lưu DB && xóa cache
////
////        DispatchEntity dispatch = new DispatchEntity();
////
////        dispatch.setLatitude(bookingRequest.getStartLatitude());
////        dispatch.setLongitude(bookingRequest.getStartLatitude());
////        dispatch.setDriver();
////        dispatch.setVehicleType(bookingRequest.getVehicleType());
////
////        DispatchEntity newDispatch = dispatchRepository.save(dispatch);
////
////        BookingResponse disPatchResponse = dispatchMapper.mapToBookingEntity(newDispatch);
////
////        // xóa cache
////
////        redisService.deleteByKey("vehicles::all");
//
//    return new ResponseResults<>(SuccessCode.ERROR,
//            "create dispatch fail - no driver accepted booking");
//}