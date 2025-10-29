package com.service.dispatch.dtos.respones;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDispatch {
    Long vehicleId;

    String vehicleName;

    String licensePlate;
}
