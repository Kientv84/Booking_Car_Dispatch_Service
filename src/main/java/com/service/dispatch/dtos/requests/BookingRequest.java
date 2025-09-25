package com.service.dispatch.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {

    @NotNull(message = "{booking.id.notnull}")
    private Long bookingId;
    @NotNull(message = "{vehicle.type.notnull}")
    private Long vehicleType;
    @NotNull(message = "{start.latitude.notnull}")
    private Double startLatitude;
    @NotNull(message = "{start.longitude.notnull}")
    private Double startLongitude;
//    @NotNull(message = "{start.longitude.notnull}")
    private Double endLatitude;
//    @NotNull(message = "{start.longitude.notnull}")
    private Double endLongitude;
}
