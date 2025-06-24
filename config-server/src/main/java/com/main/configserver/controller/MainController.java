package com.main.configserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping
    public ResponseEntity<String> getResponse() {
        return ResponseEntity.ok("Config-server started!");
    }
}
