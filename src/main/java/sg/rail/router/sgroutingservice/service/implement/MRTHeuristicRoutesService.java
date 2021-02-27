package sg.rail.router.sgroutingservice.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sg.rail.router.sgroutingservice.models.HeuristicRoute;
import sg.rail.router.sgroutingservice.models.RailNetworkGraph;
import sg.rail.router.sgroutingservice.models.RouteData;
import sg.rail.router.sgroutingservice.models.Station;
import sg.rail.router.sgroutingservice.service.HeuristicRoutesService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MRTHeuristicRoutesService implements HeuristicRoutesService {

  private final RailNetworkGraph graph;
  private final RailRoutingService railRoutingService;
  private final RoutePathStringBuilder routeStringBuilderService;

  @Autowired
  public MRTHeuristicRoutesService(RailNetworkGraph graph, RailRoutingService railRoutingService,
                                   RoutePathStringBuilder routeStringBuilderService) {
    this.graph = graph;
    this.railRoutingService = railRoutingService;
    this.routeStringBuilderService = routeStringBuilderService;
  }

  @Override
  public List<HeuristicRoute> findRoutes(String startStationName, String destinationStationName, LocalDateTime dateTime) {

    ArrayList<ArrayList<Station>> allPossibleRoutes =
        railRoutingService.getAllPossibleRoutes(getStation(startStationName), getStation(destinationStationName),
            Optional.of(dateTime));

    List<RouteData> routeData = railRoutingService.extractRouteData(startStationName, destinationStationName,
        allPossibleRoutes, dateTime);
    List<RouteData> routesOptimizedWithOutTime = railRoutingService.getRoutesOptimizedWithOutTime(routeData);

    return routeStringBuilderService.buildRouteSteps(startStationName, destinationStationName, routesOptimizedWithOutTime, true);
  }

  private Station getStation(String stationName) {
    Map<String, List<Station>> stationsNameMap = graph.getStationsNameMap();
    if (!CollectionUtils.isEmpty(stationsNameMap.get(stationName)) && stationsNameMap.get(stationName).get(0) != null) {
      return stationsNameMap.get(stationName).get(0);
    }
    throw new RouteServiceException("Can not find valid station for the given name " + stationName);
  }

  static class RouteServiceException extends RuntimeException {
    public RouteServiceException(String errorMessage) {
      super(errorMessage);
    }
  }

}
