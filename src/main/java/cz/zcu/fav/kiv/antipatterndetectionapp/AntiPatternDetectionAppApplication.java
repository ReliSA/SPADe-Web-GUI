package cz.zcu.fav.kiv.antipatterndetectionapp;

import cz.zcu.fav.kiv.antipatterndetectionapp.spring.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(AppConfig.class)
public class AntiPatternDetectionAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AntiPatternDetectionAppApplication.class, args);
    }

}
