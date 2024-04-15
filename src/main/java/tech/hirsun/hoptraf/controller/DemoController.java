package tech.hirsun.hoptraf.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tech.hirsun.hoptraf.demo.WordCount;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @Resource
    private WordCount wordCount;

    @ResponseBody
    @RequestMapping("/wordcount")
    public String wordCount() {
        return wordCount.testSparkText();
    }

}
