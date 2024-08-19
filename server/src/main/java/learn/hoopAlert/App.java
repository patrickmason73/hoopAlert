package learn.hoopAlert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class App {

    public static void main(String[] args) {
        System.out.println("Current JVM timezone: " + java.util.TimeZone.getDefault().getID());
        SpringApplication.run(App.class, args);
    }
}