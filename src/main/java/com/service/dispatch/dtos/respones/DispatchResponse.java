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
public class DispatchResponse {
    private Long dispatchId;
    private Double latitude;
    private Double longitude;
    private StatusEnum status;
    private DriverDTO driver;
    private VehicleDispatch vehicle;
    private String message;
}

