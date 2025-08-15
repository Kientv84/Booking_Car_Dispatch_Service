package com.service.dispatch.dtos.respones;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private String startAt;
    private String endAt;
    private String vehicleType;
//    private String vehicleName;
//    private String driver;
//    private String licensePlate;
}
