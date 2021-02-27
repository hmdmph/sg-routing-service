package sg.rail.router.sgroutingservice.service;

import sg.rail.router.sgroutingservice.models.Station;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public interface RoutingService {

  public ArrayList<ArrayList<Station>> getAllPossibleRoutes(Station source, Station destination,
                                                            Optional<LocalDateTime> journeyStartTime);
}
