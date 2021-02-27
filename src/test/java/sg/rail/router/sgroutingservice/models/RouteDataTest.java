package sg.rail.router.sgroutingservice.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RouteDataTest {

  @Test
  void RouteDataFieldTest() {
    String source = RandomStringUtils.random(10);
    String destination = RandomStringUtils.random(10);
    RouteData routeData = new RouteData(source, destination);
    ArrayList<Station> listOfStations = getListOfStations();
    routeData.setRoutes(listOfStations);
    routeData.setTotalTravelTime(20);
    routeData.setTravelStationCount(8);
    Map<String, Long> lineMap = getLineMap();
    routeData.setCountByTrainLine(lineMap);

    assertEquals(source, routeData.getSource(), "source value is different");
    assertEquals(destination, routeData.getDestination(), "destination value is different");
    assertEquals(20, routeData.getTotalTravelTime(), "Travel time is different");
    assertEquals(8, routeData.getTravelStationCount(), "travel stations count is different");
    assertEquals(lineMap, routeData.getCountByTrainLine(), "travel line map is different");
    assertEquals(listOfStations, routeData.getRoutes(), "routes are different");

    RouteData routeDataEmpty = new RouteData();
    routeDataEmpty.setSource(source);
    routeDataEmpty.setDestination(destination);

    assertEquals(source, routeDataEmpty.getSource(), "source value is different");
    assertEquals(destination, routeDataEmpty.getDestination(), "destination value is different");
    assertNotNull(routeData.toString(), "toString not empty");
    assertNotNull(routeDataEmpty.toString(), "toString not empty");
  }

  private Map<String, Long> getLineMap() {
    Map<String, Long> map = new HashMap<>();
    map.put(RandomStringUtils.random(5), 10L);
    map.put(RandomStringUtils.random(5), 24L);
    map.put(RandomStringUtils.random(5), 5L);
    return map;
  }

  private ArrayList<Station> getListOfStations() {
    Station ns01 = new Station("NS01", "Jurong East", LocalDateTime.of(1990, 3, 10, 0, 0, 0).toLocalDate());
    Station ew24 = new Station("EW24", "Jurong East", LocalDateTime.of(1998, 11, 5, 0, 0, 0).toLocalDate());
    Station ns15 = new Station("NS15", "Yio Chu Kang", LocalDateTime.of(1987, 11, 18, 0, 0, 0).toLocalDate());
    Station cc12 = new Station("CC12", "Bartley", LocalDateTime.of(2009, 5, 28, 0, 0, 0).toLocalDate());
    Station dt12 = new Station("DT02", "Cashew", LocalDateTime.of(2015, 12, 27, 0, 0, 0).toLocalDate());
    Station ew21 = new Station("EW21", "Buona Vista", LocalDateTime.of(1998, 03, 12, 0, 0, 0).toLocalDate());
    Station ew22 = new Station("EW22", "Dover", LocalDateTime.of(2001, 10, 18, 0, 0, 0).toLocalDate());
    Station ew23 = new Station("EW23", "Clementi", LocalDateTime.of(1988, 03, 12, 0, 0, 0).toLocalDate());
    Station ew25 = new Station("EW25", "Chinese Garden", LocalDateTime.of(1988, 11, 5, 0, 0, 0).toLocalDate());
    return new ArrayList<>(Arrays.asList(ns01, ns15, ew24, cc12, dt12, ew21, ew22, ew23, ew25));
  }
}
