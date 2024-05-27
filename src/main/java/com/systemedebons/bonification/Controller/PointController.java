package com.systemedebons.bonification.Controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/points")
public class PointController {

    @GetMapping("/sayhello")
    public  String sayhello(){
    return "Hello World";
    }






}
