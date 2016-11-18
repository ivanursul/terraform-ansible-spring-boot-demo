package org.ivanursul.terraform.ansible;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String demo() {
        return "Hello world!";
    }


}
