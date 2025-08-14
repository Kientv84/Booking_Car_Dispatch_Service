package dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class BookingRequest {
    private String startAt;
    private String endAt;
    private String vehicleTye;
}
