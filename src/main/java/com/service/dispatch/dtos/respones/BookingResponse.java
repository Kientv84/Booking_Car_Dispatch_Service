package com.service.dispatch.dtos.respones;


import com.service.dispatch.utils.StatusEnum;
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
private Long bookingId;
    private Long dispatchId;
    private Double latitude;     // tọa độ điểm xuất phát (vĩ độ)
    private Double longitude;
    private StatusEnum status;

    private Long vehicleId;
    private Long driverId;

    private String message;
//    private String vehicleName;
//    private String driver;
//    private String licensePlate;
}
