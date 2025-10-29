//package com.service.dispatch.components;
//
//import com.service.dispatch.dtos.requests.BookingRequest;
//import com.service.dispatch.dtos.respones.BookingResponse;
//import com.service.dispatch.dtos.respones.serviceResponse.VehicleResponse;
//import com.service.dispatch.entities.DispatchEntity;
//import com.service.dispatch.mappers.DispatchMapper;
//import com.service.dispatch.repositories.DispatchRepository;
//import com.service.dispatch.utils.StatusEnum;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class DispatchCreatorTest {
//
//    @Mock
//    private DispatchRepository dispatchRepository;
//
//    @Mock
//    private DispatchMapper dispatchMapper;
//
//    @InjectMocks
//    private DispatchCreator dispatchCreator;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreateDispatch_Success() {
//        BookingRequest bookingRequest = new BookingRequest();
//        bookingRequest.setVehicleType(1L);
//        bookingRequest.setBookingId(10L);
//        bookingRequest.setStartLatitude(12.34);
//        bookingRequest.setStartLongitude(56.78);
//
//        // Fake driver
//        VehicleResponse.DriverDTO driver = new VehicleResponse.DriverDTO();
//        driver.setDriverId(101L);
//
//        VehicleResponse vehicle = new VehicleResponse(1L, "Car A", "ABC-123");
//        vehicle.setDriver(driver);
//
//        DispatchEntity fakeEntity = new DispatchEntity();
//        fakeEntity.setId(100L);
//
//        BookingResponse fakeResponse = new BookingResponse();
//        fakeResponse.setVehicle(vehicle);
//
//        when(dispatchRepository.save(any(DispatchEntity.class))).thenReturn(fakeEntity);
//        when(dispatchMapper.mapToBookingEntity(any(DispatchEntity.class))).thenReturn(fakeResponse);
//
//        BookingResponse dispatch = dispatchCreator.createDispatch(bookingRequest, vehicle, StatusEnum.ACCEPT);
//
//        assertNotNull(dispatch);
//        assertEquals(1L, bookingRequest.getVehicleType());
//        assertEquals(1L, dispatch.getVehicle().getVehicleId());
//        assertEquals("ABC-123", dispatch.getVehicle().getLicensePlate());
//        assertEquals(101L, dispatch.getVehicle().getDriver().getDriverId());
//
//        verify(dispatchRepository, times(1)).save(any(DispatchEntity.class));
//        verify(dispatchMapper, times(1)).mapToBookingEntity(any(DispatchEntity.class));
//    }
//
//    @Test
//    void testCreateDispatch_NullVehicle_ThrowsException() {
//        BookingRequest bookingRequest = new BookingRequest();
//        bookingRequest.setVehicleType(1L);
//
//        assertThrows(RuntimeException.class,
//                () -> dispatchCreator.createDispatch(bookingRequest, null, StatusEnum.PENDING));
//    }
//}
