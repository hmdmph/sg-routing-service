package sg.rail.router.sgroutingservice.service.implement;

import org.springframework.stereotype.Service;
import sg.rail.router.sgroutingservice.models.HeuristicRoute;
import sg.rail.router.sgroutingservice.models.RouteData;
import sg.rail.router.sgroutingservice.models.Station;
import sg.rail.router.sgroutingservice.service.PathStringBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoutePathStringBuilder implements PathStringBuilder {


  @Override
  public List<HeuristicRoute> buildRouteSteps(String source, String destination, List<RouteData> routeData,
                                              boolean withTimeCost) {
    List<HeuristicRoute> routes = new ArrayList<>();

    String title = "Travel from " + source + " to " + destination;
    StringBuilder tempString = new StringBuilder(title + "\n\n");

    int counter = 1;

    for (RouteData data : routeData) {
      HeuristicRoute route = new HeuristicRoute();
      route.setRouteId(counter);
      tempString.append("--------- [ Route ").append(counter).append(" ] ---------\n");
      if (withTimeCost) {
        route.setTotalTravelTime(data.getTotalTravelTime());
        tempString.append("Time: ").append(data.getTotalTravelTime()).append(" minutes\n");
      }
      route.setTraveledStations(data.getTravelStationCount());
      route.setListOfStations(data.getRoutes().stream().map(Station::getCode).collect(Collectors.toList()));
      route.setTravelSteps(getTrainRouteStepsInText(data.getRoutes()));
      routes.add(route);
      tempString.append("Stations travelled: ")
          .append(data.getTravelStationCount())
          .append("\n")
          .append("Route: ")
          .append(Arrays.toString(data.getRoutes().toArray()))
          .append("\n\n")
          .append(getTrainRouteStepsInText(data.getRoutes()))
          .append("\n");
      counter++;
    }

    return routes;
  }

  private List<String> getTrainRouteStepsInText(ArrayList<Station> stations) {
    StringBuilder sb = new StringBuilder("");
    List<String> steps = new ArrayList<>();
    for (int i = 0; i < stations.size() - 1; i++) {
      String strTake = "Take ";
      String strLineFrom = " line from ";
      if (stations.get(i).isInterchange()) { // is interchange
        if (i > 0 && !stations.get(i - 1).isSameTrainLine(stations.get(i + 1))) { // next station is not in the same line
          String changeStep =
              "Change from " + stations.get(i - 1).getLineCode() + " line to " + stations.get(i + 1).getLineCode() +
                  " line";
          sb.append(changeStep + "\n");
          steps.add(changeStep);
          String fromToStep =
              strTake + stations.get(i + 1).getLineCode() + strLineFrom + stations.get(i).getName() + " to " + stations.get(i + 1).getName();
          sb.append(fromToStep + "\n");
          steps.add(fromToStep);
        } else {
          String commonLine = getCommonLine(stations, i);
          String fromToStep =
              strTake + commonLine + strLineFrom + stations.get(i).getName() + " to " + stations.get(i + 1).getName();
          sb.append(fromToStep + "\n");
          steps.add(fromToStep);
        }
      } else {
        String fromToStep =
            strTake + stations.get(i).getLineCode() + strLineFrom + stations.get(i).getName() + " to " + stations.get(i + 1).getName();
        sb.append(fromToStep + "\n");
        steps.add(fromToStep);
      }
    }
    return steps;
  }

  private String getCommonLine(List<Station> stations, int index) {
    Station current = stations.get(index++);
    Station nextStation = stations.get(index++);

    List<String> currentLines = current.getLines();
    List<String> otherStLines = nextStation.getLines();

    List<String> intersect = currentLines.stream().filter(otherStLines::contains).collect(Collectors.toList());

    while (intersect.size() > 1 && stations.size() - 1 >= index) {
      Station next = stations.get(index++);
      List<String> nextStLines = next.getLines();

      intersect = intersect.stream()
          .filter(nextStLines::contains)
          .collect(Collectors.toList());
    }

    if (!intersect.isEmpty()) {
      return intersect.get(0);
    } else {
      return nextStation.getLineCode();
    }
  }

}
