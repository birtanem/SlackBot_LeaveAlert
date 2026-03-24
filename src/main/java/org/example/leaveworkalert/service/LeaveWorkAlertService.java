package org.example.leaveworkalert.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.leaveworkalert.DTO.WeatherInfoDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaveWorkAlertService {

  private final SlackClient slackClient;
  private final WeatherClient weatherClient;
  private static final List<String> BAD_WEATHER = List.of("Rain","Snow","Tornado","Squall","Drizzle");


  // 화-토 4시55분에 실행
  @Scheduled(cron = "00 55 16 * * 2-6")
  public void sendAlert() {
    try {
      WeatherInfoDTO weatherInfoDTO = weatherClient.getWeather();
      String message = buildWeatherMessage(weatherInfoDTO);
      log.info("[Scheduler] alerting start... ");
      slackClient.sendMessage(message);
    } catch (Exception e) {
      log.error("[Scheduler] alerting failed: ", e);
    }
  }

  private String buildWeatherMessage(WeatherInfoDTO weatherInfoDTO) {
    String advice = getActivityAdvice(weatherInfoDTO);
    return String.format(
        """
        현재 날씨는 `%s` , 현재 온도 `%d℃` (체감 `%d℃`)입니다.
        초미세먼지 농도는 `%.1f㎍/㎥`로 `%s` (WHO기준).
        %s
        
        얼른 퇴근합시다!🏃‍♀️🏃‍♂️
        """
        , weatherInfoDTO.getDescription(), weatherInfoDTO.getTemperature(), weatherInfoDTO.getFeels_like()
        ,weatherInfoDTO.getAirPm(), getAirCondition(weatherInfoDTO.getAirPm()), advice
    );
  }

  private String getActivityAdvice(WeatherInfoDTO weatherInfoDTO) {

    double airPm = weatherInfoDTO.getAirPm();

    if (BAD_WEATHER.contains(weatherInfoDTO.getWeather()) || airPm > 25) {
      return "🏡 실내 운동을 추천합니다.(마스크 착용하세용😷)";
    }
    if ((weatherInfoDTO.getWeather().equals("Clear")||weatherInfoDTO.getWeather().equals("Clouds")) && airPm <= 15) {
      return "달리기나 자전거 타기에 딱 좋은 날씨네요!🚴🏃‍♀";
    }

    return "☁️야외활동 하기에 무난한 날씨예요 ☁️";
  }

  private String getAirCondition(double airPm){
    if (airPm <= 15) {
      return "좋음";
    }
    if (airPm >25){
      return "나쁨";
    }
    return "보통";
  }
}
