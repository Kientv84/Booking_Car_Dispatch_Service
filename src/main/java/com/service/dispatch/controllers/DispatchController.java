package com.service.dispatch.controllers;

import com.service.dispatch.dtos.requests.BookingRequest;
import com.service.dispatch.dtos.respones.BookingResponse;
import com.service.dispatch.dtos.respones.ResponseResults;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.service.dispatch.service.DispatchService;

@RestController
@RequestMapping("/v1/dispatch")
@RequiredArgsConstructor
public class DispatchController {


    private final DispatchService dispatchService;


    @PostMapping("/dispatches")
    public ResponseResults createDispatch(@RequestBody BookingRequest bookingRequest) {
        return dispatchService.createDispatch(bookingRequest);
    }
}
