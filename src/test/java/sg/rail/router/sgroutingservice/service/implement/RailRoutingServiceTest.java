package sg.rail.router.sgroutingservice.service.implement;


import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import sg.rail.router.sgroutingservice.models.HeuristicRoute;
import sg.rail.router.sgroutingservice.models.RailNetworkGraph;
import sg.rail.router.sgroutingservice.models.RouteData;
import sg.rail.router.sgroutingservice.models.Station;
import sg.rail.router.sgroutingservice.service.FileParser;
import sg.rail.router.sgroutingservice.service.NetworkBuilder;
import sg.rail.router.sgroutingservice.service.PathStringBuilder;
import sg.rail.router.sgroutingservice.service.TravelTimeOperationService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class RailRoutingServiceTest {

  @InjectMocks
  private FileParser csvParserService = new CsvParser();

  @InjectMocks
  private NetworkBuilder networkBuilder = new RailNetworkBuilder();

  @InjectMocks
  private RailNetworkGraph graph;

  @InjectMocks
  private final TravelTimeOperationService travelTimeOperationService = new MRTTravelTimeOperationService();

  @InjectMocks
  private final PathStringBuilder pathStringBuilder = new RoutePathStringBuilder();

  @InjectMocks
  private RailRoutingService routingService;

  @BeforeEach
  void init_mocks() {
    openMocks(this);
  }

  String fileName = "/stationmap.csv";

  @Test
  public void getAllPossibleRoutesTest() throws IOException {

    List<Station> stations = csvParserService.parseFile(fileName, ",");
    graph = networkBuilder.buildRailNetwork(stations);
    routingService = new RailRoutingService(graph, travelTimeOperationService);

    // peak times
    LocalDateTime peakTime1 = LocalDateTime.of(2021, 2, 17, 6, 10, 00);
    LocalDateTime peakTime2 = LocalDateTime.of(2021, 2, 16, 8, 40, 00);
    LocalDateTime peakTime3 = LocalDateTime.of(2021, 2, 17, 18, 40, 00);


    String source = "Boon Lay";
    String destination = "Little India";

    Map<String, List<Station>> stationsNameMap = graph.getStationsNameMap();

    Station sourceStation = stationsNameMap.get(source).get(0);
    Station destinationStation = stationsNameMap.get(destination).get(0);

    ArrayList<ArrayList<Station>> allPossibleRoutes = routingService.getAllPossibleRoutes(sourceStation, destinationStation,
        Optional.of(peakTime1));

    List<RouteData> routeData1 = routingService.extractRouteData("BoonLay", "Little India", allPossibleRoutes, peakTime1);
    List<RouteData> routesOptimized1 = routingService.getRoutesOptimizedWithOutTime(routeData1);
    List<HeuristicRoute> routeStepsText = pathStringBuilder.buildRouteSteps(source, destination, routesOptimized1, true);

    Assertions.assertEquals(6, allPossibleRoutes.size(), "wrong result of routes");
    Assertions.assertEquals(6, routeStepsText.size(), "total time should wrong");
    Assertions.assertEquals(180, (int) routeStepsText.get(0).getTotalTravelTime(), "total time should wrong");
    Assertions.assertEquals(12, (int) routeStepsText.get(0).getTraveledStations(), "total stations wrong");
    Assertions.assertTrue(routeStepsText.get(1).getTotalTravelTime() >= routeStepsText.get(0).getTotalTravelTime(), "best " +
        "possible path should be first");
    Assertions.assertEquals(186, (int) routeStepsText.get(1).getTotalTravelTime(), "total time should wrong");

    ArrayList<ArrayList<Station>> allPossibleRoutes2 = routingService.getAllPossibleRoutes(stationsNameMap.get(source).get(0),
        stationsNameMap.get(destination).get(0), Optional.of(peakTime2));

    List<RouteData> routeData2 = routingService.extractRouteData(source, destination, allPossibleRoutes2, peakTime1);
    List<RouteData> routesOptimized2 = routingService.getRoutesOptimizedWithOutTime(routeData2);

    List<HeuristicRoute> routeStepsText2 = pathStringBuilder.buildRouteSteps(source, destination, routesOptimized2, true);
    Assertions.assertEquals(6, routeStepsText2.size(), "total routes");

    source = "Tuas Link";
    destination = "Clementi";

    ArrayList<ArrayList<Station>> allPossibleRoutes3 = routingService.getAllPossibleRoutes(stationsNameMap.get(source).get(0),
        stationsNameMap.get(destination).get(0), Optional.of(peakTime3));

    List<RouteData> routeData3 = routingService.extractRouteData(source, destination, allPossibleRoutes3, peakTime3);
    List<RouteData> routesOptimized3 = routingService.getRoutesOptimizedWithOutTime(routeData3);
    List<HeuristicRoute> routeStepsText3 = pathStringBuilder.buildRouteSteps(source, destination, routesOptimized3,
        true);
    Assertions.assertEquals(3, routesOptimized3.size(), "total count of routes wrong");
    Assertions.assertEquals(10, (int) routesOptimized3.get(0).getTravelStationCount(), "total traveled station");

  }


  @Test
  public void testTravelInNightTime() throws IOException {

    List<Station> stations = csvParserService.parseFile(fileName, ",");
    graph = networkBuilder.buildRailNetwork(stations);
    routingService = new RailRoutingService(graph, travelTimeOperationService);
    Map<String, List<Station>> stationsNameMap = graph.getStationsNameMap();

    // night time
    LocalDateTime nightTime1 = LocalDateTime.of(2020, 4, 1, 1, 40, 00);
    LocalDateTime nightTime2 = LocalDateTime.of(2020, 4, 1, 22, 10, 00);
    LocalDateTime nightTime3 = LocalDateTime.of(2020, 4, 1, 5, 30, 00);

    String source = "Boon Lay";
    String destination = "Little India";

    ArrayList<ArrayList<Station>> allPossibleRoutes1 = routingService.getAllPossibleRoutes(stationsNameMap.get(source).get(0),
        stationsNameMap.get(destination).get(0), Optional.of(nightTime1));

    List<RouteData> routeData1 = routingService.extractRouteData(source, destination, allPossibleRoutes1, nightTime1);
    List<RouteData> routesOptimized1 = routingService.getRoutesOptimizedWithOutTime(routeData1);

    List<HeuristicRoute> routeStepsText = pathStringBuilder.buildRouteSteps(source, destination, routesOptimized1,
        true);

    Assertions.assertEquals(2, routeStepsText.size(), "total routes wrong");
    Assertions.assertEquals(170, (int) routeStepsText.get(0).getTotalTravelTime(), "total time of travel, wrong");

    source = "Boon Lay";
    destination = "Orchard Boulevard";

    ArrayList<ArrayList<Station>> allPossibleRoutes2 = routingService.getAllPossibleRoutes(stationsNameMap.get(source).get(0),
        stationsNameMap.get(destination).get(0), Optional.of(nightTime2));

    List<RouteData> routeData2 = routingService.extractRouteData(source, destination, allPossibleRoutes2, nightTime2);
    List<RouteData> routesOptimized2 = routingService.getRoutesOptimizedWithOutTime(routeData2);

    List<HeuristicRoute> routeStepsText2 = pathStringBuilder.buildRouteSteps(source, destination, routesOptimized2,
        true);

    Assertions.assertEquals(0, routeStepsText2.size(), "Orchard Boulevard is not operational yet");

    destination = "King Albert Park";

    ArrayList<ArrayList<Station>> allPossibleRoutes3 = routingService.getAllPossibleRoutes(stationsNameMap.get(source).get(0),
        stationsNameMap.get(destination).get(0), Optional.of(nightTime3));

    List<RouteData> routeData3 = routingService.extractRouteData(source, destination, allPossibleRoutes2, nightTime3);
    List<RouteData> routesOptimized3 = routingService.getRoutesOptimizedWithOutTime(routeData3);

    List<HeuristicRoute> routeStepsText3 = pathStringBuilder.buildRouteSteps(source, destination, routesOptimized3,
        true);

    Assertions.assertEquals(0, routeStepsText3.size(), "DT line is operational");

  }

  @Test
  public void testTravelInNonPeakTime() throws IOException {

    List<Station> stations = csvParserService.parseFile(fileName, ",");
    graph = networkBuilder.buildRailNetwork(stations);
    routingService = new RailRoutingService(graph, travelTimeOperationService);
    Map<String, List<Station>> stationsNameMap = graph.getStationsNameMap();

    // non-peak and non night times
    LocalDateTime usualTime0 = LocalDateTime.of(2020, 4, 18, 8, 40, 00); // Saturday
    LocalDateTime usualTime1 = LocalDateTime.of(2020, 4, 1, 10, 30, 00);
    LocalDateTime usualTime2 = LocalDateTime.of(2020, 4, 1, 13, 30, 00);
    LocalDateTime usualTime3 = LocalDateTime.of(2020, 4, 1, 4, 30, 00);

    String source = "Boon Lay";
    String destination = "Little India";

    ArrayList<ArrayList<Station>> allPossibleRoutes1 = routingService.getAllPossibleRoutes(stationsNameMap.get(source).get(0),
        stationsNameMap.get(destination).get(0), Optional.of(usualTime0));

    List<RouteData> routeData1 = routingService.extractRouteData(source, destination, allPossibleRoutes1, usualTime0);
    List<RouteData> routesOptimized1 = routingService.getRoutesOptimizedWithOutTime(routeData1);

    List<HeuristicRoute> routeStepsText = pathStringBuilder.buildRouteSteps(source, destination, routesOptimized1,
        true);

    Assertions.assertEquals(6, routeStepsText.size(), "total routes wrong");
    Assertions.assertEquals(156, (int) routeStepsText.get(0).getTotalTravelTime(), "total time of travel, wrong");

    source = "Cashew";
    destination = "Stevens";

    ArrayList<ArrayList<Station>> allPossibleRoutes2 = routingService.getAllPossibleRoutes(stationsNameMap.get(source).get(0),
        stationsNameMap.get(destination).get(0), Optional.of(usualTime1));

    List<RouteData> routeData2 = routingService.extractRouteData(source, destination, allPossibleRoutes2, usualTime1);
    List<RouteData> routesOptimized2 = routingService.getRoutesOptimizedWithOutTime(routeData2);

    List<HeuristicRoute> routeStepsText2 = pathStringBuilder.buildRouteSteps(source, destination, routesOptimized2,
        true);

    Assertions.assertEquals(2, routeStepsText2.size(), "total routes wrong");
    Assertions.assertEquals(56, routeStepsText2.get(0).getTotalTravelTime(), "total time of travel, wrong");

    source = "Chinatown";
    destination = "Marina Bay";

    ArrayList<ArrayList<Station>> allPossibleRoutes3 = routingService.getAllPossibleRoutes(stationsNameMap.get(source).get(0),
        stationsNameMap.get(destination).get(0), Optional.of(usualTime3));

    List<RouteData> routeData3 = routingService.extractRouteData(source, destination, allPossibleRoutes3, usualTime3);
    List<RouteData> routesOptimized3 = routingService.getRoutesOptimizedWithOutTime(routeData3);

    List<HeuristicRoute> routeStepsText3 = pathStringBuilder.buildRouteSteps(source, destination, routesOptimized3,
        true);

    Assertions.assertEquals(1, routeStepsText3.size(), "total routes wrong");
    Assertions.assertEquals(70, routeStepsText3.get(0).getTotalTravelTime(), "total time of travel, wrong");
  }

}
