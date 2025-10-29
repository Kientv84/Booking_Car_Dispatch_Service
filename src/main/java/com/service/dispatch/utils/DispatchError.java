package com.service.dispatch.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DispatchError {

    D001("D001", "No vehicles available in Redis", HttpStatus.BAD_REQUEST),
    D002("D002", "No vehicle matched type", HttpStatus.BAD_REQUEST),
    D003("D003", "No vehicle available after location filter", HttpStatus.BAD_REQUEST),
    D004("D004", "No driver accepted booking", HttpStatus.NOT_FOUND),
    D005("D005", "Error while calling driver API", HttpStatus.BAD_GATEWAY),
    D006("D006", "Error while processing dispatch", HttpStatus.INTERNAL_SERVER_ERROR),
    D007("D007", "Dispatch not found with id", HttpStatus.NOT_FOUND),
    D008("D008", "Failed to retrieve vehicle data from Redis", HttpStatus.INTERNAL_SERVER_ERROR),
    D009("D009", "Error while saving dispatch log", HttpStatus.INTERNAL_SERVER_ERROR),
    D010("D010", "Driver accepted the booking successfully", HttpStatus.OK),
    D011("D011", "Dispatch updated successfully", HttpStatus.OK);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus httpStatus;



    public static DispatchError fromCode(String code) {
        for (DispatchError e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Unknown DispatchError code: " + code);
    }
}