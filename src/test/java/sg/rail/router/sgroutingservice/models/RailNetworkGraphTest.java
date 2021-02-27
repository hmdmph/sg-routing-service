package sg.rail.router.sgroutingservice.models;

import static org.springframework.test.util.AssertionErrors.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import sg.rail.router.sgroutingservice.service.FileParser;
import sg.rail.router.sgroutingservice.service.NetworkBuilder;
import sg.rail.router.sgroutingservice.service.implement.CsvParser;
import sg.rail.router.sgroutingservice.service.implement.RailNetworkBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RailNetworkGraphTest {

  @InjectMocks
  FileParser csvParserService = new CsvParser();
  NetworkBuilder railNetworkBuilder = new RailNetworkBuilder();

  String fileName = "./stationmap.csv";

  @Test
  public void addStationAndEdgeTest() throws IOException {
    List<Station> stations = csvParserService.parseFile(fileName, ",");
    RailNetworkGraph railNetworkGraph = new RailNetworkGraph();

    Station jeNs = stations.get(0); // NS1,Jurong East,10 March 1990
    RailNetworkGraph graph1 = railNetworkGraph.addStation(jeNs);

    assertTrue("station count is wrong", graph1.getStations().size() == 1);
    assertTrue("station count is wrong", graph1.getStationsMap().size() == 1);
    assertTrue("station count is wrong", graph1.getStationsNameMap().size() == 1);

    Station jeEw = stations.get(50); // EW24,Jurong East,5 November 1988
    RailNetworkGraph graph2 = graph1.addStation(jeEw);

    assertTrue("station count is wrong", graph2.getStations().size() == 2);
    assertTrue("wrong station mapping", graph2.getStationsMap().size() == 2);
    assertTrue("wrong station mapping", graph2.getStationsNameMap().size() == 1);
    assertTrue("wrong station mapping", graph2.getStationsNameMap().get("Jurong East").size() == 2);
    assertTrue("wrong station mapping", graph2.getStationsNameMap().get("Jurong East").containsAll(Arrays.asList(jeEw, jeEw)));

    Station bukitBatok = stations.get(1); // NS2,Bukit Batok,10 March 1990
    RailNetworkGraph graph3 = graph2.addStation(bukitBatok);

    assertTrue("station count is wrong", graph3.getStations().size() == 3);
    assertTrue("station count is wrong", graph3.getStationsMap().size() == 3);
    assertTrue("station count is wrong", graph3.getStationsNameMap().size() == 2);
    assertTrue("bukit baton not in the list", graph3.getStationsMap().containsKey(bukitBatok));

    RailNetworkGraph graphWithAll = railNetworkBuilder.buildRailNetwork(stations);
    assertTrue("station count is wrong", graphWithAll.getStations().size() == 166);
    assertTrue("adjacent station count is wrong", graphWithAll.getStationsMap().get(jeEw).size() == 3);
    List<Station> adjacentStations = Arrays.asList(graphWithAll.getStationsNameMap().get("Chinese Garden").get(0),
        graphWithAll.getStationsNameMap().get("Clementi").get(0), graphWithAll.getStationsNameMap().get("Bukit Batok").get(0));
    assertTrue("station count is wrong", graphWithAll.getStationsMap().get(jeEw).containsAll(adjacentStations));

  }

}
