package com.service.dispatch.dtos.respones;

import com.service.dispatch.utils.StatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DispatchLogResponse {
    private Long id;
    private Long dispatchId;
    private Long bookingId;
    private Long vehicleId;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    private Long driverId;
    private int cycle;
}
