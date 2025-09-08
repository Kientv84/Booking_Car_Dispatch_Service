package com.service.dispatch.dtos.respones;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DriverAcceptResponse {

    private Long vehicleId;
    private String vehicleName;
    private String licensePlate;

}
