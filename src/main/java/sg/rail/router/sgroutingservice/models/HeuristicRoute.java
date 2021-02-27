package sg.rail.router.sgroutingservice.models;

import java.util.List;

/**
 * Class to present the output.
 * Contains best possible list of routes
 */
public class HeuristicRoute {

  private Integer routeId;
  private Integer totalTravelTime;
  private Integer traveledStations;
  private List<String> travelSteps;
  private List<String> listOfStations;

  public HeuristicRoute() {
    // empty constructor
  }

  public Integer getRouteId() {
    return routeId;
  }

  public void setRouteId(Integer routeId) {
    this.routeId = routeId;
  }

  public Integer getTotalTravelTime() {
    return totalTravelTime;
  }

  public void setTotalTravelTime(Integer totalTravelTime) {
    this.totalTravelTime = totalTravelTime;
  }

  public Integer getTraveledStations() {
    return traveledStations;
  }

  public void setTraveledStations(Integer traveledStations) {
    this.traveledStations = traveledStations;
  }

  public List<String> getListOfStations() {
    return listOfStations;
  }

  public void setListOfStations(List<String> listOfStations) {
    this.listOfStations = listOfStations;
  }

  public List<String> getTravelSteps() {
    return travelSteps;
  }

  public void setTravelSteps(List<String> travelSteps) {
    this.travelSteps = travelSteps;
  }
}
