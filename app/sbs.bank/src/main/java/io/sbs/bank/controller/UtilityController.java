package io.sbs.bank.controller;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class UtilityController {
    @GetMapping("health")
    public Map<String, String> getHealth() {
        return Map.of("status", "OK", "time", LocalDateTime.now().toString());
    }
    
    
}
