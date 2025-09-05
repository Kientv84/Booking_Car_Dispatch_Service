package com.service.dispatch.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Table(name = "dispatch")
public class DispatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private String bookingId;

//    private String customerId;

    private String startAt;  // { lưu bằng tọa độ GPS "lat": 10.762622, "lng": 106.660172}

    private String endAt;

    Double latitude;

    Double longitude;

    List<String> driverCanceled;

    private String vehicle;

    private Long vehicleType;

    private String driver;

    private String status;

    private Date createdAt;

    private Date updatedAt;

    private Date expireAt;
}
