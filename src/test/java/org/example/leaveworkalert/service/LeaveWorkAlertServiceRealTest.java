package org.example.leaveworkalert.service;

import com.slack.api.methods.SlackApiException;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LeaveWorkAlertServiceRealTest {

  @Autowired
  private LeaveWorkAlertService leaveWorkAlertService;

  @Test
  @DisplayName("sendLeaveAlert")
  public void sendAlertReal() throws SlackApiException, IOException {
   leaveWorkAlertService.sendAlert();
  }

}
