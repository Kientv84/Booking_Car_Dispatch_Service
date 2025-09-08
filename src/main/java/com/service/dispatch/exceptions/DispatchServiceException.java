package com.service.dispatch.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DispatchServiceException extends  RuntimeException {
    private final String errorCode;
    private final String messageCode;
}
