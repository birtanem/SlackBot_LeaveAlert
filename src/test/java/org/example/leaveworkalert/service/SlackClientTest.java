package org.example.leaveworkalert.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SlackClientTest {
  @Autowired
  private SlackClient slackClient;
  @Test
  @DisplayName("send message")
  public void sendMessage() throws SlackApiException, IOException {
    //given
    String text = "test alert";
    //when
    ChatPostMessageResponse response = slackClient.sendMessage(text);
    //then
    Assertions.assertNotNull(response);
    assertThat(response.isOk()).isTrue();
  }


}
