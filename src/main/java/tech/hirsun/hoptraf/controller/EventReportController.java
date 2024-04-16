package tech.hirsun.hoptraf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.hirsun.hoptraf.Result.Result;
import tech.hirsun.hoptraf.service.EventReportService;

@RestController
@RequestMapping("/api/eventreport")
public class EventReportController {

    @Autowired
    private EventReportService eventReportService;

    @GetMapping("/list")
    public Result list(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                       @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize,
                       @RequestParam(name = "driverId", required = false) String driverId,
                       @RequestParam(name = "keyword", required = false) String keyword) {
        return Result.success(eventReportService.page(pageNum, pageSize, driverId, keyword));
    }


}
