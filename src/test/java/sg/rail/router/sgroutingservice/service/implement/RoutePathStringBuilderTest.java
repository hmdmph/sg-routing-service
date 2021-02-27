package sg.rail.router.sgroutingservice.service.implement;


import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import sg.rail.router.sgroutingservice.models.HeuristicRoute;
import sg.rail.router.sgroutingservice.models.RouteData;
import sg.rail.router.sgroutingservice.models.Station;
import sg.rail.router.sgroutingservice.service.PathStringBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RunWith(MockitoJUnitRunner.class)
public class RoutePathStringBuilderTest {

  @InjectMocks
  PathStringBuilder routePathStringBuilder = new RoutePathStringBuilder();

  @Test
  public void buildRouteStepsTest() {

    String source = "Bukit Batok";
    String destination = "Chinese Garden";
    List<RouteData> routeData1 = getRoutesData(source, destination);

    List<HeuristicRoute> heuristicRoutes = routePathStringBuilder.buildRouteSteps(source, destination, routeData1, false);
    Assertions.assertEquals(1, heuristicRoutes.size(), "wrong number of routes");
    Assertions.assertEquals(2, heuristicRoutes.get(0).getTraveledStations(), "wrong number stations");
    Assertions.assertEquals(3, heuristicRoutes.get(0).getListOfStations().size(), "wrong # of list of stations");
    Assertions.assertEquals(3, heuristicRoutes.get(0).getTravelSteps().size(), "wrong number steps");

  }

  private List<RouteData> getRoutesData(String source, String destination) {
    return IntStream.range(0, 1).mapToObj(index -> getRandomRouteData(source, destination)).collect(Collectors.toList());
  }

  private RouteData getRandomRouteData(String source, String destination) {
    RouteData routeData = new RouteData(source, destination);
    routeData.setTravelStationCount(RandomUtils.nextInt(1, 20));
    routeData.setTotalTravelTime(RandomUtils.nextInt(1, 150));
    routeData.setRoutes(getNs2ToEw25());
    routeData.setCountByTrainLine(getRandomMapOfCountByTrainLine());
    return routeData;
  }

  private Map<String, Long> getRandomMapOfCountByTrainLine() {
    Map<String, Long> map = new HashMap<>();
    map.put("EW", RandomUtils.nextLong(1, 10));
    map.put("NS", RandomUtils.nextLong(1, 10));
    return map;
  }

//  private ArrayList<Station> getListOfStations() {
//    return new ArrayList<>(Arrays.asList(
//        new Station("EW22", "Dover", LocalDate.of(2001, 10, 18)),
//        new Station("EW23", "Clementi", LocalDate.of(1988, 3, 12)),
//        new Station("EW24", "Jurong East", LocalDate.of(1988, 11, 5)),
//        new Station("EW25", "Chinese Garden", LocalDate.of(1988, 11, 5)),
//        new Station("EW26", "Lakeside", LocalDate.of(1988, 11, 5)),
//        new Station("EW27", "Boon Lay", LocalDate.of(1988, 11, 5)),
//        new Station("NS1", "Jurong East", LocalDate.of(1990, 3, 10)),
//        new Station("NS2", "Bukit Batok", LocalDate.of(1990, 3, 10)),
//        new Station("NS3", "Bukit Gombak", LocalDate.of(1990, 3, 10))
//    ));
//  }

  private ArrayList<Station> getNs2ToEw25() {
    return new ArrayList<>(Arrays.asList(
        new Station("NS2", "Bukit Batok", LocalDate.of(1990, 3, 10)),
        new Station("NS1", "Jurong East", LocalDate.of(1990, 3, 10)),
        new Station("EW25", "Chinese Garden", LocalDate.of(1988, 11, 5))
    ));
  }

  private ArrayList<Station> getEw22ToEw25() {
    return new ArrayList<>(Arrays.asList(
        new Station("EW22", "Dover", LocalDate.of(2001, 10, 18)),
        new Station("EW23", "Clementi", LocalDate.of(1988, 3, 12)),
        new Station("EW24", "Jurong East", LocalDate.of(1988, 11, 5)),
        new Station("EW25", "Chinese Garden", LocalDate.of(1988, 11, 5))
    ));
  }


  //{
  //  "title": "Travel from Bukit Batok to Chinese Garden",
  //  "routes": [
  //    {
  //      "routeId": 1,
  //      "totalTravelTime": 30,
  //      "traveledStations": 2,
  //      "travelSteps": [
  //        "Take NS line from Bukit Batok to Jurong East",
  //        "Change from NS line to EW line",
  //        "Take EW line from Jurong East to Chinese Garden"
  //      ],
  //      "listOfStations": [
  //        "NS2",
  //        "NS1",
  //        "EW25"
  //      ]
  //    }
  //  ]
  //}

  //{
  //  "title": "Travel from Dover to Chinese Garden",
  //  "routes": [
  //    {
  //      "routeId": 1,
  //      "totalTravelTime": 30,
  //      "traveledStations": 3,
  //      "travelSteps": [
  //        "Take EW line from Dover to Clementi",
  //        "Take EW line from Clementi to Jurong East",
  //        "Take EW line from Jurong East to Chinese Garden"
  //      ],
  //      "listOfStations": [
  //        "EW22",
  //        "EW23",
  //        "EW24",
  //        "EW25"
  //      ]
  //    }
  //  ]
  //}


}
