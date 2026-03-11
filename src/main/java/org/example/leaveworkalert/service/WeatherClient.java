package org.example.leaveworkalert.service;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.leaveworkalert.DTO.WeatherInfoDTO;
import org.example.leaveworkalert.common.City;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherClient {

  @Value("${WEATHER_TOKEN}")
  private String weatherToken;

  public WeatherInfoDTO getWeather() {
    log.info("-------- getWeather called");

    Map<String, Object> geocoding = getGeocoding();
    String url = "https://api.openweathermap.org/data/2.5/weather";
    URI uri = UriComponentsBuilder
        .fromUriString(url)
        .queryParam("lat", geocoding.get("lat"))
        .queryParam("lon", geocoding.get("lon"))
        .queryParam("units", "metric")
        .queryParam("appid", weatherToken)
        .queryParam("lang", "kr")
        .build()
        .toUri();

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET,
        new HttpEntity<>(new HttpHeaders()), Map.class);

    if (CollectionUtils.isEmpty(response.getBody()) || !Objects.equals(response.getStatusCode(),
        HttpStatus.OK)) {
      throw new RuntimeException("getWeather failed" + response.getStatusCode());
    }

    List<Map<String,Object>> weather = (List<Map<String, Object>>) response.getBody().get("weather");
    Map<String,Object> main = (Map<String, Object>) response.getBody().get("main");
    Number temp = (Number) main.get("temp");
    Number feels_like = (Number) main.get("feels_like");
    String description = String.valueOf(weather.getFirst().get("description"));

    return WeatherInfoDTO.builder()
        .temperature(temp != null ? Math.round(temp.floatValue()) : 0)
        .description(description != null ? description : "")
        .feels_like(feels_like != null? Math.round(feels_like.floatValue()) : 0)
        .weather(String.valueOf(weather.getFirst().get("main")))
        .airPm(getAirPollution())
        .build()
        ;
  }

  public double getAirPollution() {
    log.info("-------- getAirPollution called");

    Map<String, Object> geocoding = getGeocoding();
    String url = "http://api.openweathermap.org/data/2.5/air_pollution";
    URI uri = UriComponentsBuilder
        .fromUriString(url)
        .queryParam("lat", geocoding.get("lat"))
        .queryParam("lon", geocoding.get("lon"))
        .queryParam("appid", weatherToken)
        .build()
        .toUri();

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET,
        new HttpEntity<>(new HttpHeaders()), Map.class);

    if (CollectionUtils.isEmpty(response.getBody()) || !Objects.equals(response.getStatusCode(),
        HttpStatus.OK)) {
      throw new RuntimeException("getAirPollution failed" + response.getStatusCode());

    }

    List<Map<String,Object>> list = (List<Map<String, Object>>) response.getBody().get("list");
    Map<String,Object> components = (Map<String, Object>) list.getFirst().get("components");
    return (double) components.get("pm2_5");
  }

  private Map<String,Object> getGeocoding() {
    String url = "http://api.openweathermap.org/geo/1.0/direct?";

    URI uri = UriComponentsBuilder
        .fromUriString(url)
        .queryParam("q", City.SEOUL)
        .queryParam("appid", weatherToken)
        .build()
        .toUri();

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<List> response = restTemplate.exchange(uri, HttpMethod.GET,
        new HttpEntity<>(new HttpHeaders()), List.class);

    if (CollectionUtils.isEmpty(response.getBody()) || !Objects.equals(response.getStatusCode(),
        HttpStatus.OK)) {
      throw new RuntimeException("getGeocoding failed" + response.getStatusCode());
    }

    Map<String, Object> result = (Map<String, Object>) response.getBody().getFirst();

    return result;
  }


}
