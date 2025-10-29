package com.service.dispatch.dtos.respones;


import com.service.dispatch.utils.StatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;


import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DispatchInitData {
    private Long id;
    private Long bookingId;
    Double latitude;
    Double longitude;
    private Long vehicleId;
    private Long driverId;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    private Date createdAt;
    private Date updatedAt;
}