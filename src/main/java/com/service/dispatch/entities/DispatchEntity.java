package com.service.dispatch.entities;

import com.service.dispatch.utils.StatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Table(name = "dispatch")
@EntityListeners(AuditingEntityListener.class)
public class DispatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookingId;

    Double latitude;

    Double longitude;

    private Long vehicleId;

    private Long driverId;

    @Enumerated(EnumType.STRING) // @Emuerated giúp hiểu lưu kiể enum ntn, có 2 cách Enumtype.ORIGINAL -> lưu index, Enumtype.String lưu kiểu string
    private StatusEnum status; // enum

    @CreatedDate
    @Column(updatable = false)
    private Date createdAt;

    @Column(updatable = false)
    @LastModifiedDate
    private Date updatedAt;
}

//Sửa lại lưu dispatch entity chỉ lưu 1 record. hiện tại ang 4 record,
