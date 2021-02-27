package sg.rail.router.sgroutingservice.service;

import org.springframework.stereotype.Service;
import sg.rail.router.sgroutingservice.models.RailNetworkGraph;
import sg.rail.router.sgroutingservice.models.Station;

import java.util.List;

@Service
public interface NetworkBuilder {
  RailNetworkGraph buildRailNetwork(List<Station> stations);
}
