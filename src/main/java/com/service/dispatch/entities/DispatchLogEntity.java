package com.service.dispatch.entities;

import com.service.dispatch.utils.StatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
public class DispatchLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long dispatchId;

    @Column( updatable = false)
    private Long bookingId;

    private Long vehicleId;

    @Enumerated(EnumType.STRING) // @Emuerated giúp hiểu lưu kiể enum ntn, có 2 cách Enumtype.ORIGINAL -> lưu index, Enumtype.String lưu kiểu string
    private StatusEnum status;

    private Long driverId;

    private int cycle;
}
