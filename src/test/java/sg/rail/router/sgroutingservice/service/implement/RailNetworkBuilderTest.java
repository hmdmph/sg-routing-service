package sg.rail.router.sgroutingservice.service.implement;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import sg.rail.router.sgroutingservice.models.RailNetworkGraph;
import sg.rail.router.sgroutingservice.models.Station;
import sg.rail.router.sgroutingservice.service.NetworkBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RailNetworkBuilderTest {

  @InjectMocks
  NetworkBuilder networkBuilder = new RailNetworkBuilder();

  @Test
  public void buildRailNetworkTest() {
    RailNetworkGraph graphWithAll = networkBuilder.buildRailNetwork(getListOfStations());

    Station jeEw = new Station("EW24", "Jurong East", LocalDate.of(1988, 11, 5)); // EW24,Jurong East,5 November 1988
    assertEquals("station count is wrong", 9, graphWithAll.getStations().size());
    assertEquals("adjacent station count is wrong", 3, graphWithAll.getStationsMap().get(jeEw).size());

    List<Station> adjacentStations = Arrays.asList(graphWithAll.getStationsNameMap().get("Chinese Garden").get(0),
        graphWithAll.getStationsNameMap().get("Clementi").get(0), graphWithAll.getStationsNameMap().get("Bukit Batok").get(0));

    assertTrue("station count is wrong", graphWithAll.getStationsMap().get(jeEw).containsAll(adjacentStations));
  }

  private List<Station> getListOfStations() {
    return Arrays.asList(
        new Station("EW22", "Dover", LocalDate.of(2001, 10, 18)),
        new Station("EW23", "Clementi", LocalDate.of(1988, 3, 12)),
        new Station("EW24", "Jurong East", LocalDate.of(1988, 11, 5)),
        new Station("EW25", "Chinese Garden", LocalDate.of(1988, 11, 5)),
        new Station("EW26", "Lakeside", LocalDate.of(1988, 11, 5)),
        new Station("EW27", "Boon Lay", LocalDate.of(1988, 11, 5)),
        new Station("NS1", "Jurong East", LocalDate.of(1990, 3, 10)),
        new Station("NS2", "Bukit Batok", LocalDate.of(1990, 3, 10)),
        new Station("NS3", "Bukit Gombak", LocalDate.of(1990, 3, 10))
    );
  }


}
