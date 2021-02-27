package sg.rail.router.sgroutingservice.service;

import org.springframework.stereotype.Service;
import sg.rail.router.sgroutingservice.models.HeuristicRoute;
import sg.rail.router.sgroutingservice.models.RouteData;

import java.util.List;

@Service
public interface PathStringBuilder {
  List<HeuristicRoute> buildRouteSteps(String source, String destination, List<RouteData> routeData, boolean withTimeCost);
}
