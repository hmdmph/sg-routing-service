package sg.rail.router.sgroutingservice.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RouteControllerTest {

  @LocalServerPort
  private int port;
  @Autowired
  private TestRestTemplate rest;

  private String URL;

  @org.junit.jupiter.api.BeforeEach
  @PostConstruct
  void setUp() {
    URL = "http://localhost:" + port + "/api/sg/rail";
  }

  @DisplayName("GET /routes with request data")
  @Test
  void getRoutes200Test() {
    String source = "Lakeside";
    String destination = "Bukit Batok";
    String dateTime = "2021-02-07T05:50:00";

    Map<String, String> urlVariables = new HashMap<>();
    urlVariables.put("source", source);
    urlVariables.put("destination", destination);
    urlVariables.put("time", dateTime);
    ResponseEntity<Map<String, Object>> result = rest.exchange(URL + "/routes?source={source" +
            "}&destination={destination}&time={time}",
        HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {
        }, urlVariables);

    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isNotNull();
    ArrayList resultBody = (ArrayList) result.getBody().get("routes");
    assertThat(result.getBody().size() == 2);
  }

  @DisplayName("GET /not-a-routes with request data")
  @Test
  void getRoutes404Test() {
    String source = "Lakeside";
    String destination = "Bukit Batok";
    String dateTime = "2021-02-07T05:50:00";

    Map<String, String> urlVariables = new HashMap<>();
    urlVariables.put("source", source);
    urlVariables.put("destination", destination);
    urlVariables.put("time", dateTime);
    ResponseEntity<Map<String, Object>> result = rest.exchange(URL + "/no-routes?source={source" +
            "}&destination={destination}&time={time}",
        HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {
        }, urlVariables);

    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @DisplayName("GET /routes with wrong stations name")
  @Test
  void getRoutesWrongInputTest() {
    String source = "Lake";
    String destination = "Bukit Batok";
    String dateTime = "2021-02-07T05:50:00";

    Map<String, String> urlVariables = new HashMap<>();
    urlVariables.put("source", source);
    urlVariables.put("destination", destination);
    urlVariables.put("time", dateTime);
    ResponseEntity<Map<String, Object>> result = rest.exchange(URL + "/no-routes?source={source" +
            "}&destination={destination}&time={time}",
        HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {
        }, urlVariables);

    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @DisplayName("GET /routes with wrong date time")
  @Test
  void getRoutesEmptyInputTest() {
    String source = "Lakeside";
    String destination = "Bukit Batok";
    String dateTime = "2021-02-45T05:50:00";

    Map<String, String> urlVariables = new HashMap<>();
    urlVariables.put("source", source);
    urlVariables.put("destination", destination);
    urlVariables.put("time", dateTime);
    ResponseEntity<Map<String, Object>> result = rest.exchange(URL + "/routes?source={source}&destination={destination}&time" +
            "={time}",
        HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {
        }, urlVariables);

    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }


}
