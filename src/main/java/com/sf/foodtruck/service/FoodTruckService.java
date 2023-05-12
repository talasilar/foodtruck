package com.sf.foodtruck.service;

import com.sf.foodtruck.model.FoodTruck;
import com.sf.foodtruck.repository.FoodTruckRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

;

@Service
public class FoodTruckService {
    public static final double EARTH_RADIUS = 6371; // in kilometers



    @Autowired
    private FoodTruckRepository foodTruckRepository;

    public void importData() throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/Mobile_Food_Facility_Permit.csv")));
        CSVFormat csvFormat = CSVFormat.DEFAULT
                .withDelimiter(',')
                .withQuote('"')
                .withEscape('\\')
                .withIgnoreEmptyLines(true)
                .withFirstRecordAsHeader()
                .withTrim()
                .withHeader("locationid", "applicant", "facilitytype", "cnn", "locationdescription", "address",
                        "blocklot", "block", "lot", "permit", "status", "fooditems", "x", "y", "latitude", "longitude",
                        "schedule", "dayshours", "noi_sent", "approved", "received", "priorpermit", "expirationdate", "location");

        Iterable<CSVRecord> records = csvFormat.parse(reader);
        List<FoodTruck> foodTrucks = new ArrayList<>();
        for (CSVRecord record : records) {
            FoodTruck foodTruck = new FoodTruck();
            foodTruck.setLocationId(record.get("locationid"));
            foodTruck.setApplicant(record.get("applicant"));
            foodTruck.setFacilityType(record.get("facilitytype"));
            foodTruck.setCnn(Long.parseLong(record.get("cnn")));
            foodTruck.setLocationDescription(record.get("locationdescription"));
            foodTruck.setAddress(record.get("address"));
            foodTruck.setBlockLot(record.get("blocklot"));
            foodTruck.setBlock(isNumber(record.get("block")) ? Integer.parseInt(record.get("block")) : null);
            foodTruck.setLot(record.get("lot"));
            foodTruck.setPermit(record.get("permit"));
            foodTruck.setStatus(record.get("status"));
            foodTruck.setFoodItems(trimTo255Chars(record.get("fooditems")));
            foodTruck.setX(isNumber(record.get("x")) ? Double.parseDouble(record.get("x")) : 0);
            foodTruck.setY(isNumber(record.get("y")) ? Double.parseDouble(record.get("y")) : 0);
            foodTruck.setLatitude(Double.parseDouble(record.get("latitude")));
            foodTruck.setLongitude(Double.parseDouble(record.get("longitude")));
            foodTruck.setSchedule(record.get("schedule"));
            foodTruck.setDaysHours(record.get("dayshours"));
            foodTruck.setNoiSent(record.get("noi_sent"));
            foodTruck.setApproved(record.get("approved"));
            foodTruck.setReceived(record.get("received"));
            foodTruck.setPriorPermit(record.get("priorpermit"));
            foodTruck.setExpirationDate(record.get("expirationdate"));
            foodTruck.setLocation(record.get("location"));
            foodTrucks.add(foodTruck);
        }
         foodTruckRepository.saveAll(foodTrucks);
    }

    public List<FoodTruck> getAllFoodTrucks() {
        return foodTruckRepository.findAll();
    }
    /* this one requires backend function ST_Distance, needs to install
    public List<FoodTruck> getFoodTrucksNearLocation(double latitude, double longitude, double radiusInMeters) {
        return foodTruckRepository.findByLocationNear(latitude, longitude, radiusInMeters);
    }*/
    public List<FoodTruck> getFoodTrucksNearLocation(double latitude, double longitude, double radiusInMeters) {
        List<FoodTruck> nearbyFoodTrucks = new ArrayList<>();

        Point givenLocation = new Point(latitude, longitude);
        List<FoodTruck> all = foodTruckRepository.findAll();
        for (FoodTruck foodTruck : all) {
            Distance distance = new Distance(radiusInMeters, Metrics.NEUTRAL);
            if (calculateDistance(latitude, longitude, foodTruck.getLatitude(), foodTruck.getLongitude() ) <distance.getValue()){
                nearbyFoodTrucks.add(foodTruck);
            }
        }
        return nearbyFoodTrucks;
    }

    public List<FoodTruck> getFoodTrucksByFoodItems(String foodItems) {
        return foodTruckRepository.findByFoodItemsContainingIgnoreCase(foodItems);
    }


    public List<FoodTruck> getFoodTrucksByApplicant(String applicant) {
        return foodTruckRepository.findByApplicantContainingIgnoreCase(applicant);
    }

    public List<FoodTruck> getFoodTrucksByFacilityType(String facilityType) {
        return foodTruckRepository.findByFacilityTypeContainingIgnoreCase(facilityType);
    }

    private boolean isNumber(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String trimTo255Chars(String input) {
        if (input.length() <= 255) {
            return input;
        } else {
            return input.substring(0, 255);
        }
    }

    public  double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;
        return distance;
    }
}
