package hei.school.prog3.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MatchRestController {

    @PostMapping("/matchMaker/{seasonYear}")
    public void createMatch() {

    }
}
