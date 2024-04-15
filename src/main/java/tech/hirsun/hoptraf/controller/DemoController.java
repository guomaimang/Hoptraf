package tech.hirsun.hoptraf.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tech.hirsun.hoptraf.demo.WordCount;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @ResponseBody
    @RequestMapping("/wordcount")
    public String wordCount() {
        WordCount wordCount = new WordCount();
        return wordCount.testSparkText();
    }

}
