package com.service.dispatch.dtos.respones.serviceResponse;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleResponse {
    Long vehicleId;

    String vehicleName;

    String licensePlate;

    String status;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime signupDate;

    DriverDTO driver;

    Double latitude;

    Double longitude;

    long vehicleType; // chứa ID
//    long vehicleType; // chứa ID
//
//    List<Long> drivers;
}
