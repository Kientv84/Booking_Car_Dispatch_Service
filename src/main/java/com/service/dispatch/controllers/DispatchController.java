package com.service.dispatch.controllers;

import com.service.dispatch.dtos.requests.BookingRequest;
import com.service.dispatch.dtos.respones.BookingResponse;
import com.service.dispatch.dtos.respones.ResponseResults;
import com.service.dispatch.entities.DispatchEntity;
import com.service.dispatch.entities.DispatchLogEntity;
import com.service.dispatch.mappers.DispatchMapper;
import com.service.dispatch.service.DispatchLogService;
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
    public ResponseEntity createJob(@RequestBody BookingRequest bookingRequest) {
        //TODO 1 : default tạo 1 record với status Pending ....

        DispatchEntity dispatchEntityData = dispatchService.initDispatch(bookingRequest).getBody();

        //TODO 2 : Lưu log cho lần dispatch này ....

        BookingResponse mapToSaveLog = dispatchMapper.mapToBookingEntity(dispatchEntityData);

        dispatchLogService.createLog(mapToSaveLog, 0);

        //TODO 3 : Update lại Dispatch entity Gọi api create Dispatch thực hiện logic gọi tài xế ....

        return dispatchService.createDispatch(bookingRequest);
    }
}


// api create job lưu dispatch entity từ booking - status pending -> goi createDispatch ( Lưu bảng dispatchLog -  xe, taixe, cycle, status ) -> update lại dispatch entity