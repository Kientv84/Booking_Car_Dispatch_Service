package com.service.dispatch.service.impls;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.dispatch.components.CronJobSchedule;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.service.dispatch.service.DispatchService;
import org.springframework.stereotype.Service;

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
    private final CronJobSchedule cronJobSchedule;

    @Override
    public ResponseResults createDispatch(BookingRequest bookingRequest) {
        log.info("Start get create dispatch ...");

            // Gọi service vehicle
            List<VehicleResponse> vehicles = Collections.emptyList();

            try {
                vehicles = vehicleClient.getAllVehicles();
                if (vehicles == null && vehicles.isEmpty()) {
                    log.warn("Vehicle service returned empty list");
                    return new ResponseResults<>(SuccessCode.ERROR, "create dispatch fail - no vehicles available");
                } else {
                    log.info("Vehicle service called successfull!!");
                }
            } catch (Exception e) {
                log.warn("Get all from vehicle service failed!", e); // log cả stack trace
                vehicles = Collections.emptyList(); // fallback để code còn chạy
            }

            // filter theo type
        List<VehicleResponse> filtered = vehicles.stream()
                .filter(v -> bookingRequest.getVehicleType().equals(v.getVehicleType().getName()))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            return new ResponseResults<>(SuccessCode.ERROR, "create dispatch fail - no vehicle matched type: "
                    + bookingRequest.getVehicleType());
        }


        // B2: Lọc theo vị trí

        List<VehicleResponse> sortedVehicles = findVehiclesByLocation(bookingRequest.getStartLatitude(), bookingRequest.getStartLongitude(), filtered);

        if (sortedVehicles.isEmpty()) {
            return new ResponseResults<>(SuccessCode.ERROR,
                    "create dispatch fail - no vehicle found by location");
        }


        // Log danh sách xe và khoảng cách
        sortedVehicles.forEach(vehicle ->
                        log.info("VehicleId: {}, VehicleName: {}, Distance: {} km",
                                vehicle.getVehicleId(),
                                vehicle.getVehicleName(),
                                calculateDistance(bookingRequest.getStartLatitude(), bookingRequest.getStartLongitude(), vehicle.getLatitude(), vehicle.getLongitude()))  // đảm bảo trong VehicleResponse có field distance
                        );


        // Lưu vào cache (Redis)

        redisService.setValue("vehicles::all", sortedVehicles, 3600);

        // --- Gọi api driver ---

        for (VehicleResponse vehicleResponse : sortedVehicles) {

            Long idDrvier = vehicleResponse.getVehicleId();

            try {


                Boolean isAccept = vehicleClient.isAcceptBooking(idDrvier, "pending" );

                if ( isAccept != null ) {
                    log.info("please wait driver" + vehicleResponse.getVehicleName() + " is response");
                }

                // Check status booking

                if ( isAccept == true ) { return new ResponseResults<>(SuccessCode.SUCCESS, " Driver" + vehicleResponse.getVehicleName() + " is accepted"); }

            } catch (Exception e) {
                return new ResponseResults<>(SuccessCode.ERROR, "Call api driver fail with" + e.getMessage());
            }


        }

//        DriverBookingRespone driverDecision = cronJobSchedule.scheduleDriverDecision(sortedVehicles);
//
//        if (!driverDecision.isAccepted()) {
//            log.warn("Driver rejected booking");
//            return new ResponseResults<>(SuccessCode.ERROR,
//                    "create dispatch fail - No driver accept this booking");
//        }


        // Nếu driver accept thì mới lưu DB && xóa cache

        DispatchEntity dispatch = new DispatchEntity();

        dispatch.setLatitude(bookingRequest.getStartLatitude());
        dispatch.setLongitude(bookingRequest.getStartLatitude());
        dispatch.setVehicleType(bookingRequest.getVehicleType());

        DispatchEntity newDispatch = dispatchRepository.save(dispatch);

        BookingResponse disPatchResponse = dispatchMapper.mapToBookingEntity(newDispatch);

        // xóa cache

        redisService.deleteByKey("vehicles::all");

        return new ResponseResults<>(SuccessCode.SUCCESS, "create dispatch success", driverDecision, disPatchResponse);
    }

    @Override
    public BookingResponse getDispatchById(Long id) {
        return null;
    }

    // Tính theo đường chim bay
    private double calculateDistance(double latStart, double longStart, double latVehicle, double longVehicle) {
        final int R = 6371; // Bán kính Trái đất (km)

        double latDistance = Math.toRadians(latVehicle - latStart); // Tính chênh lệch vĩ độ và chênh lệch kinh độ giữa 2 điểm. Đơn vị Radian
        double lonDistance = Math.toRadians(longVehicle - longStart); // Chuyển từ độ san radian

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latStart)) * Math.cos(Math.toRadians(latVehicle)) // công thức Haversine: tính a
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2); // = sin^2(alpha/2) + cos(alpha)cos(beta) + cos^2(beta)

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)); // Tính góc

        return R * c; // Tính ra km để so sánh
    }

    // Sắp xếp xe gần nhất
    public List<VehicleResponse> findVehiclesByLocation(Double latStart, Double longStart, List<VehicleResponse> vehicles) {
        if (vehicles == null || vehicles.isEmpty()) {
            return Collections.emptyList();
        }

        return vehicles.stream()
                .sorted((v1, v2) -> {
                    double dist1 = calculateDistance(latStart, longStart, v1.getLatitude(), v1.getLongitude());
                    double dist2 = calculateDistance(latStart, longStart, v2.getLatitude(), v2.getLongitude());
                    return Double.compare(dist1, dist2); // nhỏ hơn thì gần hơn
                })
                .collect(Collectors.toList());
    }


}


// Người dùng --> book xe ( request: loại xe --> nơi đón --> ... các yêu cầu khác )
// 1) Filter theo loại xe --> trả về các xe
// 2) Từ các xe đó Lấy vị trí xe --> logic tính khoảng cách từ điểm đón của người dùng đến địa điểm của xe đang đứng ( Tạm thời tính theo đường chim bay)
// xe nào hợp lệ thì trả về xe đó