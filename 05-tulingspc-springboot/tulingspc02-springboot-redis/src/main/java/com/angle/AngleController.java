package com.angle;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by smlz on 2019/9/4.
 */
@RestController
public class AngleController {

    @RequestMapping("/helloAngle")
    public String helloAngle() {
        return "angle";
    }
}
