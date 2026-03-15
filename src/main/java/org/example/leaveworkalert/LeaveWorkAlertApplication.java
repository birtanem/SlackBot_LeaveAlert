package org.example.leaveworkalert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LeaveWorkAlertApplication {

  public static void main(String[] args) {
    SpringApplication.run(LeaveWorkAlertApplication.class, args);
  }

}
