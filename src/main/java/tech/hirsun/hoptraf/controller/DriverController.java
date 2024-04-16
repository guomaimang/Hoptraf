package tech.hirsun.hoptraf.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.hirsun.hoptraf.Result.Result;
import tech.hirsun.hoptraf.service.DriverService;

@RestController
@RequestMapping("/api/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @GetMapping("/list")
    public Result list() {
        return Result.success(driverService.getDriverList());
    }

    @GetMapping("/info")
    public Result info(@RequestParam String driverId) {
        return Result.success(driverService.getDriverInfo(driverId));
    }

    @GetMapping("/diagram")
    public Result diagram(@RequestParam String driverId) {
        return Result.success(driverService.getDriverDiagram(driverId));
    }
}
