package sg.rail.router.sgroutingservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import sg.rail.router.sgroutingservice.models.RailNetworkGraph;
import sg.rail.router.sgroutingservice.models.Station;
import sg.rail.router.sgroutingservice.service.implement.CsvParser;
import sg.rail.router.sgroutingservice.service.implement.RailNetworkBuilder;

import java.io.IOException;
import java.util.List;

@Configuration
@ComponentScan(value = "sg.rail.router")
public class RailRoutingConfig {

  @Value("${stationmap.file.path}")
  private String stationMapFilePath;

  private final CsvParser csvParserService;

  private final RailNetworkBuilder railNetworkBuilderService;

  @Autowired
  public RailRoutingConfig(CsvParser csvParserService, RailNetworkBuilder railNetworkBuilderService) {
    this.csvParserService = csvParserService;
    this.railNetworkBuilderService = railNetworkBuilderService;
  }

  @Bean
  public RailNetworkGraph graph() throws IOException {
    String fileName = stationMapFilePath;
    List<Station> stations = csvParserService.parseFile(fileName, ",");
    return railNetworkBuilderService.buildRailNetwork(stations);
  }
}
