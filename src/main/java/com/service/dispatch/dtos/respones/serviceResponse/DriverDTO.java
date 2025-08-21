package com.service.dispatch.dtos.respones.serviceResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {
    private long driverId;

    private String name;

    private String age;

    private String phoneNumber;

    private String address;

    private String identityCard;

    private String sex;

    private String driverLicense;

    private String status;

    private BigDecimal avgRating;
}
