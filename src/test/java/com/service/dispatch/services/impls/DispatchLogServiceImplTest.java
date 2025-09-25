//package com.service.dispatch.services.impls;
//
//import com.service.dispatch.dtos.respones.BookingResponse;
//import com.service.dispatch.dtos.respones.serviceResponse.DriverDTO;
//import com.service.dispatch.dtos.respones.serviceResponse.VehicleResponse;
//import com.service.dispatch.entities.DispatchEntity;
//import com.service.dispatch.entities.DispatchLogEntity;
//import com.service.dispatch.repositories.DispatchLogRepository;
//import com.service.dispatch.repositories.DispatchRepository;
//import com.service.dispatch.service.impls.DispatchLogServiceImpl;
//import com.service.dispatch.utils.StatusEnum;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Date;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class DispatchLogServiceImplTest {
//
//    @Mock
//    private DispatchLogRepository dispatchLogRepository;
//
//    @Mock
//    private DispatchRepository dispatchRepository;
//
//    @InjectMocks
//    private DispatchLogServiceImpl dispatchLogService;
//
//    private BookingResponse bookingResponse;
//    private DispatchLogEntity dispatchLogEntity;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        bookingResponse = new BookingResponse();
//        bookingResponse.setBookingId(1L);
//        bookingResponse.setDispatchId(2L);
//        bookingResponse.setStatus(StatusEnum.ACCEPT);
//
//        dispatchLogEntity = new DispatchLogEntity();
//        dispatchLogEntity.setId(1L);
//        dispatchLogEntity.setBookingId(1L);
//        dispatchLogEntity.setDispatchId(2L);
//        dispatchLogEntity.setStatus(StatusEnum.ACCEPT);
//    }
//
//    @Test
//    void testCreateLog_Success() {
//        when(dispatchLogRepository.save(any(DispatchLogEntity.class))).thenReturn(dispatchLogEntity);
//
////        DispatchLogEntity response = dispatchLogService.createLog(bookingResponse, 1);
//
////        assertNotNull(response);
////        assertEquals(200, response.getStatusCodeValue());
////        assertEquals(1L, response.getBody().getBookingId());
////        assertEquals(2L, response.getBody().getDispatchId());
////        assertEquals(StatusEnum.ACCEPT, response.getBody().getStatus());
//
//        verify(dispatchLogRepository, times(1)).save(any(DispatchLogEntity.class));
//    }
//
//
//    @Test
//    void testUpdateLogStatus_Success() {
//        DispatchEntity existing = new DispatchEntity();
//        existing.setBookingId(10L);
//        existing.setStatus(StatusEnum.ACCEPT);
//
//        when(dispatchRepository.findByBookingId(10L)).thenReturn(Optional.of(existing));
//        when(dispatchRepository.save(any(DispatchEntity.class))).thenReturn(existing);
//
//        dispatchLogService.updateLogStatus(10L, StatusEnum.ACCEPT);
//
//        assertEquals(StatusEnum.ACCEPT, existing.getStatus());
//        assertNotNull(existing.getUpdatedAt());
//
//        verify(dispatchRepository, times(1)).findByBookingId(10L);
//        verify(dispatchRepository, times(1)).save(existing);
//    }
//
//    @Test
//    void testUpdateLogStatus_NotFound() {
//        when(dispatchRepository.findByBookingId(999L)).thenReturn(Optional.empty());
//
//        RuntimeException ex = assertThrows(RuntimeException.class,
//                () -> dispatchLogService.updateLogStatus(999L, StatusEnum.REJECTED));
//
//        assertEquals("Dispatch not found with bookingId 999", ex.getMessage());
//
//        verify(dispatchRepository, times(1)).findByBookingId(999L);
//        verify(dispatchRepository, never()).save(any());
//    }
//
//
//    @Test
//    void testCreateLog_WithDriverAndVehicle() {
//        // Fake driver + vehicle trong bookingResponse
//        DriverDTO driver = new DriverDTO();
//        driver.setDriverId(1L);
//        driver.setName("Driver A");
//
//        VehicleResponse vehicle = new VehicleResponse();
//        vehicle.setVehicleId(2L);
//        vehicle.setVehicleName("Car A");
//        vehicle.setLicensePlate("ABC-123");
//
//        bookingResponse.setDriver(driver);
//        bookingResponse.setVehicle(vehicle);
//
//        DispatchLogEntity savedLog = new DispatchLogEntity();
//        savedLog.setId(2L);
//        savedLog.setBookingId(1L);
//        savedLog.setDispatchId(2L);
//        savedLog.setDriverId(1L);
//        savedLog.setVehicleId(2L);
//        savedLog.setStatus(StatusEnum.ACCEPT);
//
//        when(dispatchLogRepository.save(any(DispatchLogEntity.class))).thenReturn(savedLog);
//
////        ResponseEntity<DispatchLogEntity> response = dispatchLogService.createLog(bookingResponse, 2);
//
////        assertNotNull(response);
////        assertEquals(200, response.getStatusCodeValue());
////        assertEquals(1L, response.getBody().getBookingId());
////        assertEquals(2L, response.getBody().getDispatchId());
////        assertEquals(1L, response.getBody().getDriverId());
////        assertEquals(2L, response.getBody().getVehicleId());
//
//        verify(dispatchLogRepository, times(1)).save(any(DispatchLogEntity.class));
//    }
//
//}
