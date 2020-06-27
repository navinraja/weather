package com.sapient.weather.connector;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalWeatherSystemConnector {

  private RestTemplate restTemplate;

  private static final String appId = "b1b15e88fa797225412429c1c50c122a1";
  private static final String url = "https://samples.openweathermap.org/data/2.5/weather";

  @PostConstruct
  public void init() {
    try {
      this.restTemplate = buildRestConnection();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  private RestTemplate buildRestConnection() throws NoSuchAlgorithmException {
    RestTemplate template = new RestTemplate();
    HttpClientBuilder clientBuilder = HttpClientBuilder.create();
    SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(SSLContext.getDefault(),
        new String[]{"TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
    clientBuilder.setSSLSocketFactory(sslConnectionSocketFactory);
    CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
    HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
    factory.setHttpClient(client);
    template.setRequestFactory(factory);
    return template;
  }

  public ResponseEntity<String> getWeatherData(String cityName) throws URISyntaxException {
    URI uri = new URI(url);
    uri = appendUri(uri.toString(), "q="+cityName);
    uri = appendUri(uri.toString(), "appid=b1b15e88fa797225412429c1c50c122a1");
    HttpEntity<?> requestEntity = new HttpEntity<>(null);
    return restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
  }

  private URI appendUri(String uri, String appendQuery) throws URISyntaxException {
    URI oldUri = new URI(uri);
    return new URI(oldUri.getScheme(), oldUri.getAuthority(), oldUri.getPath(),
        oldUri.getQuery() == null ? appendQuery : oldUri.getQuery() + "&" + appendQuery, oldUri.getFragment());
  }
}
