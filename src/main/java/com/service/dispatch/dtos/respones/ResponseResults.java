package com.service.dispatch.dtos.respones;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseResults<T> {
    private int success;
    private String message;
    private T data;
    private T dataDispatch;

    public ResponseResults(int success, T data,  T dataDispatch) {
        this.success = success;
        this.data = data;
        this.dataDispatch = getDataDispatch();
    }

    public ResponseResults(int success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ResponseResults(int success, String message) {
        this.success = success;
        this.message = message;
    }
}
