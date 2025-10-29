//package com.service.dispatch.components;
//
//import com.service.dispatch.dtos.requests.BookingRequest;
//import com.service.dispatch.dtos.respones.serviceResponse.VehicleResponse;
//import com.service.dispatch.entities.DispatchEntity;
//import com.service.dispatch.utils.StatusEnum;
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class FilterVehiclesTest {
//
//    FilterVehicles filterVehicles = new FilterVehicles();
//
//    @Test
//    void testFilterVehicles_EmptyList() {
//        List<VehicleResponse> result = filterVehicles.findVehiclesByLocation(
//                10.0, 20.0, List.of() );
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void testFilterVehicles_ByType() {
//        VehicleResponse car = new VehicleResponse(1L, "Car A", "111");
//        car.setLatitude(10.0);
//        car.setLongitude(20.0);
//        car.setVehicleType(1L);
//
//        VehicleResponse bike = new VehicleResponse(2L, "Bike A", "222");
//        bike.setLatitude(11.0);
//        bike.setLongitude(21.0);
//        bike.setVehicleType(2L);
//
//        List<VehicleResponse> vehicles = List.of(car, bike);
//
//        List<VehicleResponse> result = filterVehicles.findVehiclesByLocation(
//                10.0, 20.0, vehicles);
//
//        assertEquals(1, result.size());
//        assertEquals("Car A", result.get(0).getVehicleName());
//    }
//}