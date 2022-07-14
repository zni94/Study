package com.example.demo.controller;

import com.example.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class DemoController {

    @Autowired
    DemoService demoService;

    @GetMapping("/holiday")
    public List getHoliday(){
        List<String> holidayData = demoService.getHolliday();

        return holidayData;
    }

    @GetMapping("/test")
    public LocalDateTime getFinishDate(){
        return demoService.getFinishDate(LocalDateTime.of(2022,9,8,12,18,18), 3);
    }
}
