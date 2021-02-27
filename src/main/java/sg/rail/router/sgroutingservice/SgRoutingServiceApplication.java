package sg.rail.router.sgroutingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class SgRoutingServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SgRoutingServiceApplication.class, args);
  }

}
