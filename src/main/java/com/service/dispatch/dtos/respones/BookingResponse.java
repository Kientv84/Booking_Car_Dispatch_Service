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
//    private String vehicleName;
//    private String licensePlate;
//    private String driver;
    private Long vehicleType;
    private Double latitude;     // tọa độ điểm xuất phát (vĩ độ)
    private Double longitude;
//    private String vehicleName;
//    private String driver;
//    private String licensePlate;
}
