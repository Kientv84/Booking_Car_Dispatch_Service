package com.service.dispatch.entities;

import com.service.dispatch.utils.StatusEnum;
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

    private Long bookingId;

//    private String customerId;

//    private String startAt;  // { lưu bằng tọa độ GPS "lat": 10.762622, "lng": 106.660172}
//
//    private String endAt;

    Double latitude;

    Double longitude;

//    List<String> driverCanceled;

    private Long vehicleId;

//    private Long vehicleType;

    private Long driverId;

    @Enumerated(EnumType.STRING) // @Emuerated giúp hiểu lưu kiể enum ntn, có 2 cách Enumtype.ORIGINAL -> lưu index, Enumtype.String lưu kiểu string
    private StatusEnum status; // enum

    private Date createdAt;

    private Date updatedAt;
//
//    private Date expireAt;
}
