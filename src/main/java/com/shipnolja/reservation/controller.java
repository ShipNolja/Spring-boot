package com.shipnolja.reservation;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controller {

    @GetMapping("/welcome")
    public String welcome() {
        return "test123";
    }
}
