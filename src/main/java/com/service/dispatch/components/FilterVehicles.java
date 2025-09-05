package com.service.dispatch.components;

import com.service.dispatch.dtos.requests.BookingRequest;
import com.service.dispatch.dtos.respones.ResponseResults;
import com.service.dispatch.dtos.respones.serviceResponse.VehicleResponse;
import com.service.dispatch.utils.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FilterVehicles {

    public List<VehicleResponse> filterVehicles(List<VehicleResponse> vehicles, BookingRequest bookingRequest) {
        // B1: Lọc theo type
        List<VehicleResponse> filtered = vehicles.stream()
                .filter(v -> bookingRequest.getVehicleType().equals(v.getVehicleType()))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            return filtered;
        }

        // B2: Lọc và sắp xếp theo vị trí
        List<VehicleResponse> sortedVehicles =
                findVehiclesByLocation(bookingRequest.getStartLatitude(), bookingRequest.getStartLongitude(), filtered);

        // Trả về danh sách đã sắp xếp (có thể empty nếu không tìm được xe nào phù hợp)
        return sortedVehicles;
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

    // Tính khoảng cách theo công thức Haversine (đường chim bay)
    private double calculateDistance(double latStart, double longStart, double latVehicle, double longVehicle) {
        final int R = 6371; // Bán kính Trái đất (km)

        double latDistance = Math.toRadians(latVehicle - latStart);
        double lonDistance = Math.toRadians(longVehicle - longStart);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latStart)) * Math.cos(Math.toRadians(latVehicle))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // khoảng cách (km)
    }
}
