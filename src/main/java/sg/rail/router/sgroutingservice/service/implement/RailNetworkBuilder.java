package sg.rail.router.sgroutingservice.service.implement;

import org.springframework.stereotype.Service;
import sg.rail.router.sgroutingservice.models.RailNetworkGraph;
import sg.rail.router.sgroutingservice.models.Station;
import sg.rail.router.sgroutingservice.service.NetworkBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class RailNetworkBuilder implements NetworkBuilder {

  @Override
  public RailNetworkGraph buildRailNetwork(List<Station> stations) {
    RailNetworkGraph graph = new RailNetworkGraph();

    // sorting all stations by line and station code
    stations.sort(Station::compareTo);

    // Adding same line connected stations
    addSameLineConnectedStations(stations, graph);

    // finding interchange stations in others lines and adding edges to graph
    addEdgesToGraph(graph);

    // updating all stations with all required details ( codes, names )
    populateStationsWithData(graph);

    return graph;
  }

  private void addSameLineConnectedStations(List<Station> stations, RailNetworkGraph graph) {

    IntStream.range(0, stations.size()).forEach(index -> {
      Station currentStation = stations.get(index);
      graph.addStation(currentStation);
      if (index > 0) {
        Station previousStation = stations.get(index - 1);
        if (previousStation.isSameTrainLine(currentStation)) {
          graph.addStationEdge(stations.get(index - 1), currentStation);
        }
      }
    });
  }

  private void addEdgesToGraph(RailNetworkGraph graph) {
    List<Station> stations = graph.getStations();

    stations.forEach(station -> {
      List<Station> connected = graph.getStationsNameMap().get(station.getName());
      if (connected.size() > 1) {
        IntStream.range(0, connected.size()).forEach(index -> {
          Station interchangeStation = connected.get(index);
          List<Station> adjacentStations = graph.getStationsMap().get(interchangeStation);
          updateAdjacentStations(graph, station, interchangeStation, adjacentStations);
        });
      }
    });
  }

  private void updateAdjacentStations(RailNetworkGraph graph, Station station, Station interchangeStation,
                                      List<Station> adjacentStations) {
    if (!station.getLineCode().equals(interchangeStation.getLineCode())) {
      adjacentStations.stream().filter(adjacentStation -> !station.getLineCode().equals(adjacentStation.getLineCode()))
          .forEach(filteredStation -> graph.addStationEdge(station, filteredStation));
    }
  }

  private void populateStationsWithData(RailNetworkGraph graph) {
    Map<Station, List<Station>> stationList = graph.getStationsMap();
    Map<String, List<Station>> stationNameMap = graph.getStationsNameMap();

    stationList.forEach((key, otherStations) -> {
      setStationCodes(stationNameMap, key);
      otherStations.forEach(otherStation -> setStationCodes(stationNameMap, otherStation));
    });
  }

  private void setStationCodes(Map<String, List<Station>> stationNameMap, Station station) {
    if (station.getLines().isEmpty()) {
      List<Station> sameStationList = stationNameMap.get(station.getName());
      List<String> lines = sameStationList.stream().map(Station::getLineCode).collect(Collectors.toList());
      List<String> codes = sameStationList.stream().map(Station::getCode).collect(Collectors.toList());
      station.setLines(lines);
      station.setCodes(codes);
    }
  }

}
