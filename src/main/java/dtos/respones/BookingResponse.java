package dtos.respones;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class BookingResponse {
    private String vehicleName;
    private String driver;
    private String licensePlate;
}
