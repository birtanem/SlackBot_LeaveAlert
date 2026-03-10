package org.example.leaveworkalert.service;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.leaveworkalert.common.Exception.SlackException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackClient {
  @Value("${SLACK_BOT_TOKEN}")
  private String slackToken;
  @Value("${SLACK_CHANNEL_ID}")
  private String channel;

  public ChatPostMessageResponse sendMessage(String message)
      throws SlackApiException, IOException {
    Slack slack = Slack.getInstance();
    ChatPostMessageResponse response = slack.methods(slackToken).chatPostMessage(
        ChatPostMessageRequest.builder()
            .channel(channel)
            .text(message)
            .build());

    if (!response.isOk()){
      throw new SlackException(response);
    }
    return response;
  }
}
