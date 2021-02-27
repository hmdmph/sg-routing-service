package sg.rail.router.sgroutingservice.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RailNetworkGraph {

  private final Map<Station, List<Station>> stationsMap;
  private final Map<String, List<Station>> stationsNameMap;

  public RailNetworkGraph() {
    stationsMap = new LinkedHashMap<>();
    stationsNameMap = new LinkedHashMap<>();
  }

  public List<Station> getStations() {
    return new ArrayList<>(stationsMap.keySet());
  }

  public Map<Station, List<Station>> getStationsMap() {
    return stationsMap;
  }

  public Map<String, List<Station>> getStationsNameMap() {
    return stationsNameMap;
  }

  /**
   * Adding station to name map.
   *
   * @param station station
   * @return Rail network graph
   */
  public RailNetworkGraph addStation(Station station) {
    stationsMap.put(station, new ArrayList<>());
    if (!stationsNameMap.containsKey(station.getName())) {
      stationsNameMap.put(station.getName(), new ArrayList<>());
    }
    List<Station> existingStations = stationsNameMap.get(station.getName());
    if (isInterchange(existingStations, station)) {
      existingStations.forEach(statn -> statn.setInterchange(true));
      station.setInterchange(true);
    } else {
      station.setInterchange(false);
    }
    existingStations.add(station);
    return this;
  }

  /**
   * Adding stations edges in the graph ( <-- bi-directional --> )
   *
   * @param station station
   * @param another another station
   * @return Rail network graph
   */
  public RailNetworkGraph addStationEdge(Station station, Station another) {
    if (!stationsMap.get(station).contains(another)) {
      // adding bi-directional path
      stationsMap.get(station).add(another);
      stationsMap.get(another).add(station);
    }
    return this;
  }

  private boolean isInterchange(List<Station> stations, Station current) {
    return stations.stream().anyMatch(station -> station.isIntersectStation(current));
  }

}
