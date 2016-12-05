package org.ivanursul.terraform.ansible;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Timed
    @ExceptionMetered
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String demo() {
        return "Hello big world!";
    }


}
