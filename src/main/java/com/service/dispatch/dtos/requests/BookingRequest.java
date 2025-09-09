package com.service.dispatch.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class BookingRequest {
//    private String startAt;
//    private String endAt;
    private Long bookingId;
    private Long vehicleType;
    private Double startLatitude;     // tọa độ điểm xuất phát (vĩ độ)
    private Double startLongitude;
    private Double endLatitude;     // tọa độ điểm kết thúc (vĩ độ)
    private Double endLongitude;
}
