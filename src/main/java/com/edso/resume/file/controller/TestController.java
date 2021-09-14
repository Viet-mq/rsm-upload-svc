package com.edso.resume.file.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController extends BaseController {

    @GetMapping
    public String rest() {
        return "OK";
    }

}
