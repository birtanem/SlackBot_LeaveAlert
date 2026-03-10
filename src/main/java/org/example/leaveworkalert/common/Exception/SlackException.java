package org.example.leaveworkalert.common.Exception;

import com.slack.api.methods.response.chat.ChatPostMessageResponse;

public class SlackException extends RuntimeException{

  private final ChatPostMessageResponse response;

  public SlackException(ChatPostMessageResponse response) {
    super("Slack API Error: " + response.getError());
    this.response = response;
  }

}
