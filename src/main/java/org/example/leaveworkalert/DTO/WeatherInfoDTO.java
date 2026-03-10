package org.example.leaveworkalert.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class WeatherInfoDTO {
  int temperature;
  int feels_like;
  String description;
  String weather;
  double airPm;
}
