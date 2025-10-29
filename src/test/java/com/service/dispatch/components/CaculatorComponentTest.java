//package com.service.dispatch.components;
//
//import com.service.dispatch.dtos.respones.serviceResponse.VehicleResponse;
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class CaculatorComponentTest {
//
//    CaculatorComponent caculator = new CaculatorComponent();
//
//    @Test
//    void testCalculateDistance_SamePoint() {
//        double distance = caculator.calculateDistance(10.0, 20.0, 10.0, 20.0);
//        assertEquals(0.0, distance, 0.0001);
//    }
//
//    @Test
//    void testCalculateDistance_DifferentPoints() {
//        double distance = caculator.calculateDistance(10.0, 20.0, 11.0, 21.0);
//        assertTrue(distance > 0);
//    }
//
//    @Test
//    void testFindVehiclesByLocation_EmptyList() {
//        List<VehicleResponse> result = caculator.findVehiclesByLocation(10.0, 20.0, null);
//        assertTrue(result.isEmpty());
//
//        result = caculator.findVehiclesByLocation(10.0, 20.0, Arrays.asList());
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void testFindVehiclesByLocation_SortByDistance() {
//        VehicleResponse v1 = new VehicleResponse();
//        v1.setVehicleId(1L);
//        v1.setLatitude(10.0);
//        v1.setLongitude(20.0); // gần
//
//        VehicleResponse v2 = new VehicleResponse();
//        v2.setVehicleId(2L);
//        v2.setLatitude(15.0);
//        v2.setLongitude(25.0); // xa
//
//        List<VehicleResponse> vehicles = Arrays.asList(v2, v1);
//
//        List<VehicleResponse> sorted = caculator.findVehiclesByLocation(10.0, 20.0, vehicles);
//
//        assertEquals(2, sorted.size());
//        assertEquals(1L, sorted.get(0).getVehicleId()); // xe gần nhất đứng đầu
//        assertEquals(2L, sorted.get(1).getVehicleId());
//    }
//}
