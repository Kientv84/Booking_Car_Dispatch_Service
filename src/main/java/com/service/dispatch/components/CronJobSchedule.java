package com.service.dispatch.components;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CronJobSchedule {



    //  @Scheduled(cron = "${scheduler.interval}", zone = "${scheduler.interval.zone}") // Mỗi 5s 1
    // lần
    //  public void logEveryMinuteA() throws InterruptedException {
    //    log.info("Task A chạy lúc: " + OffsetDateTime.now());
    //    Thread.sleep(8000); // Giải định task chạy lâu
    //    log.info("Task A end lúc: " + OffsetDateTime.now());
    //  }
    //
    //  @Scheduled(cron = "${scheduler.interval}", zone = "${scheduler.interval.zone}")
    //  public void logEveryMinuteB() {
    //    log.info("Task B chạy lúc: " + OffsetDateTime.now());
    //  }

//    // TODO: use @Async
//    @Scheduled(cron = "${scheduler.interval}", zone = "${scheduler.interval.zone}")
//    public void triggerTask() {
//        log.info("Scheduled call at : {} | {}", OffsetDateTime.now(), Thread.currentThread().getName());
//        doAsyncWork();
//    }
//
//    @Async
//    public void doAsyncWork() {
//        log.info(" Start async task - {}", Thread.currentThread().getName());
//        try {
//            Thread.sleep(7000);
//        } catch (InterruptedException exception) {
//            Thread.currentThread().interrupt();
//        }
//        log.info("End process async - {}", Thread.currentThread().getName());
//    }

    // Gọi apk fake ở đây

//    @Async
//    public DriverBookingRespone scheduleDriverDecision( List<VehicleResponse> vehicleResponse) {
//
//        log.info("Scheduled driver decision for bookingId={} with {} candidate vehicles", vehicleResponse.size());
//
//
//        try {
//            for (int i = 5; i > 0; i--) {
//                log.info("Countdown time to driver accept: {}", i);
//                Thread.sleep(1000);
//            }
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        return fakeApi.conformBooking(vehicleResponse);
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "http://localhost:3001/v1/driver/booking"; // endpoint của service driver
//        ResponseEntity<DriverBookingRespone> response =
//                restTemplate.postForEntity(url, vehicleResponse, DriverBookingRespone.class);
//
//        return response.getBody();
    }

//    private DriverResponse conformBooking(BookingRequest bookingRequest, VehicleResponse vehicleResponse, Long id) {
//        boolean accepted = new Random().nextBoolean();
//        DriverResponse response = new DriverResponse();
//        response.setDriverId(id);
//        response.setVehicleId(vehicleResponse.getId());
//        response.setAccepted(accepted);
//        return response;
//    }


