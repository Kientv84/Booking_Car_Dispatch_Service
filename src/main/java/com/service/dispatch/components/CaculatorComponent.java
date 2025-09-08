package com.service.dispatch.components;

import com.service.dispatch.dtos.respones.serviceResponse.VehicleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CaculatorComponent {

    // Tính theo đường chim bay
    public double calculateDistance(double latStart, double longStart, double latVehicle, double longVehicle) {
        final int R = 6371; // Bán kính Trái đất (km)

        double latDistance = Math.toRadians(latVehicle - latStart); // Tính chênh lệch vĩ độ và chênh lệch kinh độ giữa 2 điểm. Đơn vị Radian
        double lonDistance = Math.toRadians(longVehicle - longStart); // Chuyển từ độ san radian

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latStart)) * Math.cos(Math.toRadians(latVehicle)) // công thức Haversine: tính a
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2); // = sin^2(alpha/2) + cos(alpha)cos(beta) + cos^2(beta)

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)); // Tính góc

        return R * c; // Tính ra km để so sánh
    }

    // Sắp xếp xe gần nhất
    public List<VehicleResponse> findVehiclesByLocation(Double latStart, Double longStart, List<VehicleResponse> vehicles) {
        if (vehicles == null || vehicles.isEmpty()) {
            return Collections.emptyList();
        }

        return vehicles.stream()
                .sorted((v1, v2) -> {
                    double dist1 = calculateDistance(latStart, longStart, v1.getLatitude(), v1.getLongitude());
                    double dist2 = calculateDistance(latStart, longStart, v2.getLatitude(), v2.getLongitude());
                    return Double.compare(dist1, dist2); // nhỏ hơn thì gần hơn
                })
                .collect(Collectors.toList());
    }

}
