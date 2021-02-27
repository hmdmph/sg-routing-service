package sg.rail.router.sgroutingservice.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class HeuristicRouteTest {

  @Test
  void classTest() {
    HeuristicRoute route = new HeuristicRoute();
    int routeId = RandomUtils.nextInt(1, 50);
    int totalTravelTime = RandomUtils.nextInt(1, 12);
    int traveledStations = RandomUtils.nextInt();
    List<String> travelSteps = getRandomStringList();
    List<String> stations = getRandomStringList();

    route.setTotalTravelTime(totalTravelTime);
    route.setTraveledStations(traveledStations);
    route.setRouteId(routeId);
    route.setTravelSteps(travelSteps);
    route.setListOfStations(stations);

    assertEquals(routeId, route.getRouteId(), "Wrong route id");
    assertEquals(totalTravelTime, route.getTotalTravelTime(), "Wrong total travel time");
    assertEquals(traveledStations, route.getTraveledStations(), "Wrong travelled stations ");
    assertEquals(travelSteps, route.getTravelSteps(), "Wrong travel steps");
    assertEquals(stations, route.getListOfStations(), "Wrong station list");
  }

  private List<String> getRandomStringList() {
    return Arrays.asList(RandomStringUtils.random(5), RandomStringUtils.random(5), RandomStringUtils.random(5),
        RandomStringUtils.random(5));
  }

}
