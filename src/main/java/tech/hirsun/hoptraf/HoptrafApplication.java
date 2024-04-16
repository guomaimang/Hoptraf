package tech.hirsun.hoptraf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tech.hirsun.hoptraf.service.AppService;

@SpringBootApplication
public class HoptrafApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoptrafApplication.class, args);

    }

}
