package org.example.leaveworkalert.service;

import java.util.Objects;
import org.example.leaveworkalert.DTO.WeatherInfoDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WeatherClientTest {

  @Autowired
  private WeatherClient weatherClient;

  private WeatherInfoDTO weatherInfoDTO;

  @BeforeEach
  void setUp() {
    weatherInfoDTO = new WeatherInfoDTO();
    weatherInfoDTO.builder()
        .weather("Clear")
        .feels_like(15)
        .description("맑음")
        .temperature(15)
        .airPm(14)
        .build();
  }

  @Test
  @DisplayName("OpenWeather API 호출")
  void getWeather(){
    //when
    weatherInfoDTO = weatherClient.getWeather();
    //then
    boolean isTrue = Objects.equals(weatherInfoDTO.getTemperature(),"Seoul");
    Assertions.assertNotNull(weatherInfoDTO);
//    Assertions.assertEquals("Clear", weatherInfoDTO.getWeather());

  }
}
