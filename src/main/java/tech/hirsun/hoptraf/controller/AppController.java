package tech.hirsun.hoptraf.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.hirsun.hoptraf.Result.Result;
import tech.hirsun.hoptraf.config.TimeConfig;

@RestController
@RequestMapping("/api/app")
public class AppController {

    @Autowired
    private TimeConfig timeConfig;

    @GetMapping("/getcutofftime")
    private Result getCutoffTime() {
        return Result.success(timeConfig.getCurrentTime());
    }


}
