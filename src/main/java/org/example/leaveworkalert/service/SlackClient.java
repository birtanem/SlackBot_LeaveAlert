package org.example.leaveworkalert.service;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.block.DividerBlock;
import com.slack.api.model.block.HeaderBlock;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.PlainTextObject;
import java.io.IOException;
import java.util.List;
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

  private final Slack slack;

  public ChatPostMessageResponse sendMessage(String message)
      throws SlackApiException, IOException {

    List<LayoutBlock> messageBlock = List.of(
        //제목
        HeaderBlock.builder()
            .text(PlainTextObject.builder().text("📢 퇴근 5분전 & 날씨 알리미").build())
            .build(),

        //구분선
        new DividerBlock(),

        //본문
        SectionBlock.builder()
            .text(MarkdownTextObject.builder().text(message).build())
            .build()
    );

    ChatPostMessageResponse response = slack.methods(slackToken).chatPostMessage(
        ChatPostMessageRequest.builder()
            .channel(channel)
            .blocks(messageBlock)
            .build());

    if (!response.isOk()){
      throw new SlackException(response);
    }
    return response;
  }
}
