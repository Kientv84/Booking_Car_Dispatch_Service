//package com.service.dispatch.controllers.mocks;
//
//import com.service.dispatch.dtos.respones.serviceResponse.DriverDTO;
//import com.service.dispatch.dtos.respones.serviceResponse.VehicleResponse;
//import com.service.dispatch.integration.VehicleClient;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//@RestController
//public class MockVehicleController {
//    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//
//    @GetMapping( value = "${openfeign.vehicleClient.url.get-all-vehicle}")
//    public ResponseEntity<List<VehicleResponse>> getVehicles() {
//        List<VehicleResponse> vehicles = new ArrayList<>();
//        return ResponseEntity.ok(vehicles);
//    }
//
//    // Mock API accept/reject booking
//    @PutMapping("/{driverId}/accept")
//    public ResponseEntity<Boolean> isAcceptBooking(
//            @PathVariable Long driverId,
//            @RequestParam String action
//    ) {
//        boolean result;
//
//        if ("accept".equalsIgnoreCase(action)) {
//            result = true;
//        } else if ("reject".equalsIgnoreCase(action)) {
//            result = false;
//        } else {
//            result = false;
//        }
//
//        return ResponseEntity.ok(result);
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<List<VehicleResponse>> getVehicles() {
//
//        // Tạo driver cho từng xe
//        DriverDTO driver1 = DriverDTO.builder()
//                .driverId(1L)
//                .name("Nguyen Van A")
//                .age("28")
//                .phoneNumber("0901234567")
//                .address("Hà Nội")
//                .identityCard("012345678")
//                .sex("nam")
//                .driverLicense("HN123456")
//                .status("ACTIVE")
//                .avgRating(4.85)
//                .build();
//
//        DriverDTO driver2 = DriverDTO.builder()
//                .driverId(2L)
//                .name("Tran Thi B")
//                .age("30")
//                .phoneNumber("0912345678")
//                .address("Đà Nẵng")
//                .identityCard("123456789")
//                .sex("nu")
//                .driverLicense("DN654321")
//                .status("ACTIVE")
//                .avgRating(4.70)
//                .build();
//
//        DriverDTO driver3 = DriverDTO.builder()
//                .driverId(3L)
//                .name("Le Van C")
//                .age("32")
//                .phoneNumber("0923456789")
//                .address("Hồ Chí Minh")
//                .identityCard("234567890")
//                .sex("nam")
//                .driverLicense("HCM987654")
//                .status("BANNED")
//                .avgRating(4.20)
//                .build();
//
//        DriverDTO driver4 = DriverDTO.builder()
//                .driverId(4L)
//                .name("Hoang Van E")
//                .age("35")
//                .phoneNumber("0945678901")
//                .address("Hải Phòng")
//                .identityCard("456789012")
//                .sex("nam")
//                .driverLicense("HP112233")
//                .status("ACTIVE")
//                .avgRating(4.95)
//                .build();
//
//        // Mapping mỗi vehicle với 1 driver tương ứng
//        List<VehicleResponse> vehicles = Arrays.asList(
//                VehicleResponse.builder()
//                        .vehicleId(1L)
//                        .vehicleName("Xe Airblade")
//                        .licensePlate("29A-12345")
//                        .status("ACTIVE")
//                        .signupDate(LocalDateTime.parse("16-08-2025 13:48:32", FORMATTER))
//                        .latitude(10.753964759828001)
//                        .longitude(106.67208484005295)
//                        .vehicleType(1)
//                        .driver(driver1)
//                        .build(),
//
//                VehicleResponse.builder()
//                        .vehicleId(2L)
//                        .vehicleName("Xe Wave Alpha")
//                        .licensePlate("34F-55667")
//                        .status("ACTIVE")
//                        .signupDate(LocalDateTime.parse("01-09-2025 13:49:34", FORMATTER))
//                        .latitude(10.833224694282714)
//                        .longitude(106.60939515515639)
//                        .vehicleType(1)
//                        .driver(driver2)
//                        .build(),
//
//                VehicleResponse.builder()
//                        .vehicleId(3L)
//                        .vehicleName("Xe Grande")
//                        .licensePlate("37I-22334")
//                        .status("BANNED")
//                        .signupDate(LocalDateTime.parse("04-09-2025 13:49:58", FORMATTER))
//                        .latitude(10.778163254989725)
//                        .longitude(106.61840102479705)
//                        .vehicleType(1)
//                        .driver(driver3)
//                        .build(),
//
//                VehicleResponse.builder()
//                        .vehicleId(4L)
//                        .vehicleName("Xe Janus")
//                        .licensePlate("38K-44556")
//                        .status("ACTIVE")
//                        .signupDate(LocalDateTime.parse("05-09-2025 13:50:14", FORMATTER))
//                        .latitude(10.845849115993074)
//                        .longitude(106.7353380633574)
//                        .vehicleType(1)
//                        .driver(driver4)
//                        .build()
//        );
//
//        return ResponseEntity.ok(vehicles);
//    }
//
//
//}
