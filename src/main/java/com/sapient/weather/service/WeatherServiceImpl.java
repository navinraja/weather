package com.sapient.weather.service;

import com.sapient.weather.connector.ExternalWeatherSystemConnector;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class WeatherServiceImpl implements WeatherService{

  @Autowired
  ExternalWeatherSystemConnector externalWeatherSystemConnector;

  @Override
  public String getWeather(String city) {
    try {
      ResponseEntity<String> responseEntity = externalWeatherSystemConnector.getWeatherData(city);
      return responseEntity.getBody();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return null;
  }
}
