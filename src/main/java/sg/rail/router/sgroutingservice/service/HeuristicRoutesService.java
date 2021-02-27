package sg.rail.router.sgroutingservice.service;

import org.springframework.stereotype.Service;
import sg.rail.router.sgroutingservice.models.HeuristicRoute;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface HeuristicRoutesService {
  List<HeuristicRoute> findRoutes(String startStationName, String destinationStationName, LocalDateTime dateTime);
}
