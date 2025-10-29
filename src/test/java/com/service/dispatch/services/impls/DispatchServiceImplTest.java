//package com.service.dispatch.services.impls;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.service.dispatch.components.CaculatorComponent;
//import com.service.dispatch.components.DispatchCreator;
//import com.service.dispatch.dtos.requests.BookingRequest;
//import com.service.dispatch.dtos.respones.BookingResponse;
//import com.service.dispatch.dtos.respones.DispatchResponse;
//import com.service.dispatch.dtos.respones.serviceResponse.DriverDTO;
//import com.service.dispatch.dtos.respones.serviceResponse.VehicleResponse;
//import com.service.dispatch.entities.DispatchEntity;
//import com.service.dispatch.exceptions.DispatchServiceException;
//import com.service.dispatch.integration.VehicleClient;
//import com.service.dispatch.mappers.DispatchMapper;
//import com.service.dispatch.repositories.DispatchRepository;
//import com.service.dispatch.service.DispatchLogService;
//import com.service.dispatch.service.RedisService;
//import com.service.dispatch.service.impls.DispatchServiceImpl;
//import com.service.dispatch.utils.StatusEnum;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class DispatchServiceImplTest {
//
//    @InjectMocks
//    private DispatchServiceImpl dispatchService;
//
//    @Mock
//    private DispatchMapper dispatchMapper;
//
//    @Mock
//    private DispatchRepository dispatchRepository;
//
//    @Mock
//    private VehicleClient vehicleClient;
//
//    @Mock
//    private CaculatorComponent caculatorComponent;
//
//    @Mock
//    private DispatchCreator dispatchCreator;
//
//    @Mock
//    private DispatchLogService dispatchLogService;
//
//    @Mock
//    private RedisService redisService;
//
//    private BookingRequest bookingRequest;
//    private VehicleResponse vehicle;
//    private DispatchEntity dispatchEntity;
//    private BookingResponse bookingResponse;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        bookingRequest = new BookingRequest();
//        bookingRequest.setBookingId(1L);
//        bookingRequest.setVehicleType(1L);
//        bookingRequest.setStartLatitude(10.0);
//        bookingRequest.setStartLongitude(20.0);
//
//        vehicle = VehicleResponse.builder()
//                .vehicleId(1L)
//                .vehicleName("Car A")
//                .licensePlate("XYZ-123")
//                .vehicleType(1L)
//                .latitude(11.0)
//                .longitude(21.0)
//                .driver(new VehicleResponse.DriverDTO(100L, "Driver A"))
//                .build();
//
//        dispatchEntity = new DispatchEntity();
//        dispatchEntity.setId(99L);
//        dispatchEntity.setBookingId(1L);
//        dispatchEntity.setStatus(StatusEnum.PENDING);
//
//        bookingResponse = new BookingResponse();
//        bookingResponse.setBookingId(1L);
//        bookingResponse.setDispatchId(99L);
//        bookingResponse.setDriver(new DriverDTO(100L, "Driver A"));
//        bookingResponse.setVehicle(vehicle);
//    }
//
//    @Test
//    void testCreateDispatch_HappyPath_FirstDriverAccepts() {
//        // initDispatch save
//        when(dispatchRepository.save(any())).thenReturn(dispatchEntity);
//        when(dispatchMapper.mapToBookingEntity(any())).thenReturn(bookingResponse);
//
//        // Redis có danh sách xe
//        when(redisService.getValue(eq("vehicles::dispatch"), any(TypeReference.class)))
//                .thenReturn(Collections.singletonList(vehicle));
//
//        // filter ra đúng vehicle
//        when(caculatorComponent.findVehiclesByLocation(anyDouble(), anyDouble(), anyList()))
//                .thenReturn(Collections.singletonList(vehicle));
//
//        // driver ACCEPT booking
//        when(vehicleClient.isAcceptBooking(eq(1L), anyString())).thenReturn(true);
//
//        DispatchResponse result = dispatchService.createDispatch(bookingRequest);
//
//        assertNotNull(result);
////        assertEquals(1L, result.getBookingId());
//        assertEquals("Car A", result.getVehicle().getVehicleName());
//        verify(dispatchLogService, atLeast(2)).createLog(any(), anyInt());
//        verify(redisService).deleteByKey("vehicles::dispatch");
//    }
//
//    @Test
//    void testCreateDispatch_NoVehiclesInCache_AndInAll() {
//        when(dispatchRepository.save(any())).thenReturn(dispatchEntity);
//        when(dispatchMapper.mapToBookingEntity(any())).thenReturn(bookingResponse);
//
//        when(redisService.getValue(eq("vehicles::dispatch"), any(TypeReference.class)))
//                .thenReturn(null);
//        when(redisService.getValue(eq("vehicles::all"), any(TypeReference.class)))
//                .thenReturn(null);
//
//        DispatchServiceException ex = assertThrows(DispatchServiceException.class,
//                () -> dispatchService.createDispatch(bookingRequest));
//
//        assertEquals("D001", ex.getErrorCode());
//    }
//
//    @Test
//    void testCreateDispatch_NoVehicleOfType() {
//        when(dispatchRepository.save(any())).thenReturn(dispatchEntity);
//        when(dispatchMapper.mapToBookingEntity(any())).thenReturn(bookingResponse);
//
//        VehicleResponse otherType = VehicleResponse.builder()
//                .vehicleId(2L)
//                .vehicleType(2L)
//                .build();
//
//        when(redisService.getValue(eq("vehicles::dispatch"), any(TypeReference.class)))
//                .thenReturn(Collections.singletonList(otherType));
//
//        DispatchServiceException ex = assertThrows(DispatchServiceException.class,
//                () -> dispatchService.createDispatch(bookingRequest));
//
//        assertEquals("D002", ex.getErrorCode());
//    }
//
//    @Test
//    void testCreateDispatch_NoNearbyVehicles() {
//        when(dispatchRepository.save(any())).thenReturn(dispatchEntity);
//        when(dispatchMapper.mapToBookingEntity(any())).thenReturn(bookingResponse);
//
//        when(redisService.getValue(eq("vehicles::dispatch"), any(TypeReference.class)))
//                .thenReturn(Collections.singletonList(vehicle));
//
//        when(caculatorComponent.findVehiclesByLocation(anyDouble(), anyDouble(), anyList()))
//                .thenReturn(Collections.emptyList());
//
//        DispatchServiceException ex = assertThrows(DispatchServiceException.class,
//                () -> dispatchService.createDispatch(bookingRequest));
//
//        assertEquals("D003", ex.getErrorCode());
//    }
//
//    @Test
//    void testCreateDispatch_AllDriversReject() {
//        when(dispatchRepository.save(any())).thenReturn(dispatchEntity);
//        when(dispatchMapper.mapToBookingEntity(any())).thenReturn(bookingResponse);
//
//        when(redisService.getValue(eq("vehicles::dispatch"), any(TypeReference.class)))
//                .thenReturn(new ArrayList<>(Collections.singletonList(vehicle)));
//
//        when(caculatorComponent.findVehiclesByLocation(anyDouble(), anyDouble(), anyList()))
//                .thenReturn(Collections.singletonList(vehicle));
//
//        when(vehicleClient.isAcceptBooking(anyLong(), anyString())).thenReturn(false);
//
//        DispatchServiceException ex = assertThrows(DispatchServiceException.class,
//                () -> dispatchService.createDispatch(bookingRequest));
//
//        assertEquals("D004", ex.getErrorCode());
//    }
//
//    @Test
//    void testCreateDispatch_VehicleClientThrows() {
//        when(dispatchRepository.save(any())).thenReturn(dispatchEntity);
//        when(dispatchMapper.mapToBookingEntity(any())).thenReturn(bookingResponse);
//
//        when(redisService.getValue(eq("vehicles::dispatch"), any(TypeReference.class)))
//                .thenReturn(Collections.singletonList(vehicle));
//
//        when(caculatorComponent.findVehiclesByLocation(anyDouble(), anyDouble(), anyList()))
//                .thenReturn(Collections.singletonList(vehicle));
//
//        when(vehicleClient.isAcceptBooking(anyLong(), anyString()))
//                .thenThrow(new RuntimeException("service down"));
//
//        DispatchServiceException ex = assertThrows(DispatchServiceException.class,
//                () -> dispatchService.createDispatch(bookingRequest));
//
//        assertEquals("D005", ex.getErrorCode());
//    }
//}
