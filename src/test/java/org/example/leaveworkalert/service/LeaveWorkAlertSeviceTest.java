package org.example.leaveworkalert.service;

import com.slack.api.methods.SlackApiException;
import java.io.IOException;
import org.example.leaveworkalert.DTO.WeatherInfoDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LeaveWorkAlertSeviceTest {

  @Mock
  private WeatherClient weatherClient;
  @Mock
  private SlackClient slackClient;
  @InjectMocks
  private LeaveWorkAlertService leaveWorkAlertService;


  @Test
  void sendAlertSuccess() throws SlackApiException, IOException {
    //given
    WeatherInfoDTO mockDto = new WeatherInfoDTO();
    mockDto.setWeather("Clear");
    mockDto.setTemperature(27);
    mockDto.setFeels_like(25);
    mockDto.setDescription("맑음");
    mockDto.setAirPm(14);

    Mockito.when(weatherClient.getWeather()).thenReturn(mockDto);

    leaveWorkAlertService.sendAlert();

    Mockito.verify(slackClient, Mockito.times(1)).sendMessage(Mockito.anyString());


  }
}
