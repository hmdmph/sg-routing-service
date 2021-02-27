package sg.rail.router.sgroutingservice.models;

import java.util.ArrayList;
import java.util.Map;

public class RouteData {
  private String source;
  private String destination;
  private Integer travelStationCount;
  private Integer totalTravelTime;
  private Map<String, Long> countByTrainLine;
  private ArrayList<Station> routes;

  public RouteData(String source, String destination) {
    this.source = source;
    this.destination = destination;
  }

  public RouteData() {
    // empty constructor
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public Integer getTravelStationCount() {
    return travelStationCount;
  }

  public void setTravelStationCount(Integer travelStationCount) {
    this.travelStationCount = travelStationCount;
  }

  public Map<String, Long> getCountByTrainLine() {
    return countByTrainLine;
  }

  public void setCountByTrainLine(Map<String, Long> countByTrainLine) {
    this.countByTrainLine = countByTrainLine;
  }

  public ArrayList<Station> getRoutes() {
    return routes;
  }

  public void setRoutes(ArrayList<Station> routes) {
    this.routes = routes;
  }

  public Integer getTotalTravelTime() {
    return totalTravelTime;
  }

  public void setTotalTravelTime(Integer totalTravelTime) {
    this.totalTravelTime = totalTravelTime;
  }

  @Override
  public String toString() {

    return "RouteData{" +
        "source='" + source + '\'' +
        ", destination='" + destination + '\'' +
        ", stationCount=" + travelStationCount +
        ", countByTrainLine=" + countByTrainLine +
        ", routes=" + routes +
        '}';
  }
}
