package hei.school.prog3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {
    @GetMapping("/")
    public String test() {
        return "test";
    }
    @GetMapping("/ping")
    public String test2() {
        return "pong";
    }
}
