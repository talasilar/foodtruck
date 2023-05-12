package com.sf.foodtruck.controller;

import com.sf.foodtruck.service.FoodTruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FoodTruckAppStartup implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private FoodTruckService foodTruckService;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        loadData();
    }

    private void loadData() {
        try {
            foodTruckService.importData();
        } catch (IOException e) {
            System.out.println("Exception while loading the data " + e);
        }

    }
}
