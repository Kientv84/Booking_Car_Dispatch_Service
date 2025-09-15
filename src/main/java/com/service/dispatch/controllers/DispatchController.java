package com.service.dispatch.controllers;

import com.service.dispatch.dtos.requests.BookingRequest;
import com.service.dispatch.dtos.respones.BookingResponse;
import com.service.dispatch.dtos.respones.ResponseResults;
import com.service.dispatch.entities.DispatchEntity;
import com.service.dispatch.entities.DispatchLogEntity;
import com.service.dispatch.mappers.DispatchMapper;
import com.service.dispatch.service.DispatchLogService;
import jakarta.validation.Valid;
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
    private final DispatchLogService dispatchLogService;
    private final DispatchMapper dispatchMapper;
//    @PostMapping("/dispatches")
//    public ResponseResults createDispatch(@RequestBody BookingRequest bookingRequest) {
//        return dispatchService.createDispatch(bookingRequest);
//    }

    @PostMapping("/job")
    public ResponseEntity<BookingResponse> createJob( @Valid @RequestBody BookingRequest bookingRequest) {

        return ResponseEntity.ok(dispatchService.createDispatch(bookingRequest));
    }
}


// api create job lưu dispatch entity từ booking - status pending -> goi createDispatch ( Lưu bảng dispatchLog -  xe, taixe, cycle, status ) -> update lại dispatch entity