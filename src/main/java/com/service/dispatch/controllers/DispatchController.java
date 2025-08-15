package com.service.dispatch.controllers;

import com.service.dispatch.dtos.requests.BookingRequest;
import com.service.dispatch.dtos.respones.BookingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.service.dispatch.service.DispatchService;

@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class DispatchController {


    private final DispatchService dispatchService;


    @PostMapping("/v1/dispatch/dispatches")
    public ResponseEntity<BookingResponse> createDispatch(@RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.ok(dispatchService.createDispatch(bookingRequest));
    }
}
