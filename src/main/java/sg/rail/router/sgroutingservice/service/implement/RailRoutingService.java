package sg.rail.router.sgroutingservice.service.implement;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.rail.router.sgroutingservice.models.RailNetworkGraph;
import sg.rail.router.sgroutingservice.models.RouteData;
import sg.rail.router.sgroutingservice.models.Station;
import sg.rail.router.sgroutingservice.service.RoutingService;
import sg.rail.router.sgroutingservice.service.TravelTimeOperationService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Most important class of the project. Here it uses the Breadth-First Search (BFS) algorithm to get the best paths possible.
 * Same time it checks all conditions required for the routes through stations and return the list all possible routes.
 */
@Service
public class RailRoutingService implements RoutingService {

  private final Logger logger = LoggerFactory.getLogger(RailRoutingService.class);
  private final RailNetworkGraph graph;
  private final TravelTimeOperationService travelTimeOperationService;

  @Autowired
  public RailRoutingService(RailNetworkGraph graph, TravelTimeOperationService travelTimeOperationService) {
    this.graph = graph;
    this.travelTimeOperationService = travelTimeOperationService;
  }

  /**
   * get all possible routes from start to destination based of input source and destination.
   * If journey starting time is provided then this uses the time to optimize the paths else uses number of stations to travel.
   */
  public ArrayList<ArrayList<Station>> getAllPossibleRoutes(Station source, Station destination,
                                                            Optional<LocalDateTime> journeyStartTime) {

    ArrayList<ArrayList<Station>> possibleRoutes = new ArrayList<>();
    LinkedList<ArrayList<Station>> queue = new LinkedList<>();
    ArrayList<Station> first = new ArrayList<>();
    Set<String> alreadyTraversedRoutes = new HashSet<>();

    first.add(source);
    queue.add(first);

    while (!queue.isEmpty()) {
      ArrayList<Station> path = queue.poll();
      Station currentStation = path.get(path.size() - 1);

      if (currentStation.equals(destination)) {
        possibleRoutes.add(path);
      } else {
        List<Station> stations = graph.getStationsMap().get(currentStation);

        for (Station nextStation : stations) {
          // generate unique id for a route, so paths will not go over same route again and again
          String routeId = generateRouteID(currentStation, nextStation);

          if (nextStation.isOpenedStation()) {
            if (!path.contains(nextStation)
                && !alreadyTraversedRoutes.contains(routeId)
                && !(source.isIntersectStation(currentStation)
                && currentStation.isIntersectStation(nextStation))
                && travelTimeOperationService.isLineOperational(nextStation.getLineCode(), journeyStartTime.orElse(LocalDateTime.now()))
            ) {
              ArrayList<Station> newPath = new ArrayList<>(path);
              newPath.add(nextStation);
              queue.add(newPath);
            }
          }
          alreadyTraversedRoutes.add(routeId);
        }
      }
    }
    return possibleRoutes;
  }

  private String generateRouteID(Station currentStation, Station nextStation) {

    String routeId;

    if (currentStation.isSameTrainLine(nextStation) && currentStation.getNumber() - nextStation.getNumber() > 0) {
      routeId = currentStation.getName() + "<-" + nextStation.getName();
    } else {
      routeId = currentStation.getName() + "->" + nextStation.getName();
    }
    return routeId;
  }

  public List<RouteData> extractRouteData(String source, String destination, ArrayList<ArrayList<Station>> possibleRoutes,
                                          LocalDateTime journeyStartTime) {
    List<RouteData> routesData = new ArrayList<>();
    possibleRoutes.forEach(route -> {
      RouteData data = new RouteData(source, destination);
      data.setTravelStationCount(route.size() - 1);
      data.setRoutes(route);
      data.setCountByTrainLine(getCountOfEachLine(route));
      data.setTotalTravelTime(getTotalTimeToTravel(route, journeyStartTime));
      routesData.add(data);
    });
    return routesData;
  }

  private Integer getTotalTimeToTravel(ArrayList<Station> stations, LocalDateTime dateTime) {
    int totalTime = 0;
    LocalDateTime currentStepDateTime = dateTime;
    for (int i = 0; i < stations.size() - 1; i++) {
      Station currentStation = stations.get(i);
      Integer travelTimeBetweenStations = travelTimeOperationService.getTravelTimeBetweenStations(currentStation.getLineCode(),
          dateTime);
      // (is a interchange) and (destination should not be interchange) and (should not be same line)
      if (i > 0 && !stations.get(i - 1).isSameTrainLine(stations.get(i + 1))) {
        Integer interchangeWaitTIme = travelTimeOperationService.getInterchangeWaitingTime(currentStepDateTime);

        currentStepDateTime = LocalDateTime.of(currentStepDateTime.toLocalDate(),
            currentStepDateTime.toLocalTime().plusMinutes(travelTimeBetweenStations.longValue() + interchangeWaitTIme.longValue()));
        totalTime = totalTime + travelTimeBetweenStations + interchangeWaitTIme; // travel time + wait time
        logger.debug("{} ( {} + {} ) = {} : {} ", stations.get(i).getCode(), travelTimeBetweenStations,
            interchangeWaitTIme, totalTime, currentStepDateTime);
      } else {
        currentStepDateTime = LocalDateTime.of(currentStepDateTime.toLocalDate(),
            currentStepDateTime.toLocalTime().plusMinutes(travelTimeBetweenStations));
        totalTime = totalTime + travelTimeBetweenStations; // travel time
        logger.debug("{} ( {} ) = {} : {} ", stations.get(i).getCode(), travelTimeBetweenStations, totalTime,
            currentStepDateTime);
      }
    }
    return totalTime;
  }

  private Map<String, Long> getCountOfEachLine(ArrayList<Station> stations) {
    return stations.stream()
        .collect(Collectors.groupingBy(Station::getLineCode, Collectors.counting()));
  }

  public List<RouteData> getRoutesOptimizedWithOutTime(List<RouteData> routes) {
    return routes.stream()
        .sorted(Comparator.comparingInt(RouteData::getTravelStationCount))
        .collect(Collectors.toList());
  }

  public List<RouteData> getRoutesOptimizedWithTimeCost(List<RouteData> routes) {
    return routes.stream()
        .sorted(Comparator.comparingInt(RouteData::getTotalTravelTime))
        .collect(Collectors.toList());
  }

}



