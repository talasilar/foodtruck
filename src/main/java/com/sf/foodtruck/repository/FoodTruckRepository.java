package com.sf.foodtruck.repository;

import com.sf.foodtruck.model.FoodTruck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodTruckRepository extends JpaRepository<FoodTruck, Long> {
    List<FoodTruck> findByFoodItemsContainingIgnoreCase(String foodItems);

    List<FoodTruck> findByApplicantContainingIgnoreCase(String applicant);

    List<FoodTruck> findByFacilityTypeContainingIgnoreCase(String facilityType);

    @Query(value = "SELECT f.* FROM FOOD_TRUCK f WHERE ST_Distance(POINT(LONGITUDE, LATITUDE), POINT(:lat, :lon)) <= :dist ", nativeQuery = true)
    List<FoodTruck> findByLocationNear(@Param("lat") double lat, @Param("lon") double lon, @Param("dist") double dist);

}
