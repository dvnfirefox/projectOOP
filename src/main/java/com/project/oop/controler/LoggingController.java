package com.project.oop.controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoggingController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
