package learn.hoopAlert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "learn.hoopAlert")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}