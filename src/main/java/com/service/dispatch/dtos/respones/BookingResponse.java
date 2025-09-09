package com.service.dispatch.dtos.respones;


import com.service.dispatch.dtos.respones.serviceResponse.DriverDTO;
import com.service.dispatch.dtos.respones.serviceResponse.VehicleResponse;
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

    //Danh cho viet ghi log
//    private Long vehicleId;
//    private Long driverId;

    private Double latitude;     // tọa độ điểm xuất phát (vĩ độ)
    private Double longitude;
    private StatusEnum status;

    private DriverDTO driver;
    private VehicleResponse vehicle;

    private String message;
//    private String vehicleName;
//    private String driver;
//    private String licensePlate;
}
