package com.service.dispatch.dtos.respones;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseResults<T> {
    private int success;
    private String message;
    private T dataVehicle;
    private T dataDispatch;

    public ResponseResults(int success, T dataVehicle,  T dataDispatch) {
        this.success = success;
        this.dataVehicle = dataVehicle;
        this.dataDispatch = getDataDispatch();
    }

    public ResponseResults(int success, String message) {
        this.success = success;
        this.message = message;
    }
}
