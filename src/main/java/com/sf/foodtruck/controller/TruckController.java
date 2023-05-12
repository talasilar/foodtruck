package com.sf.foodtruck.controller;

import com.sf.foodtruck.model.FoodTruck;
import com.sf.foodtruck.service.FoodTruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("webapi")
public class TruckController {

    @Autowired
    private FoodTruckService foodTruckService;


    @GetMapping("all")
    public ResponseEntity<List<FoodTruck>> getAllFoodTrucks() {
        return ResponseEntity.ok(foodTruckService.getAllFoodTrucks());
    }

    @GetMapping("applicant/{applicant}")
    public ResponseEntity<List<FoodTruck>> getFoodTrucksByApplicant(@PathVariable("applicant") String applicant) {
        return ResponseEntity.ok(foodTruckService.getFoodTrucksByApplicant(applicant));
    }

    @GetMapping("facility-type/{facilityType}")
    public ResponseEntity<List<FoodTruck>> getbyFacilityType(@PathVariable("facilityType") String facilityType) {
        return ResponseEntity.ok(foodTruckService.getFoodTrucksByFacilityType(facilityType));
    }

    @GetMapping("food-item/{foodItem}")
    public ResponseEntity<List<FoodTruck>> getByFoodItems(@PathVariable("foodItem") String foodItems) {
        return ResponseEntity.ok(foodTruckService.getFoodTrucksByFoodItems(foodItems));
    }

    @GetMapping("nearbytrucks/{latitude}/{longitude}/{distance}")
    public ResponseEntity<List<FoodTruck>> getByFoodItems(@PathVariable("latitude") double latitude,
                                                          @PathVariable("longitude") double longitude, @PathVariable("distance") double distance) {
        return ResponseEntity.ok(foodTruckService.getFoodTrucksNearLocation(latitude, longitude, distance));
    }
}
