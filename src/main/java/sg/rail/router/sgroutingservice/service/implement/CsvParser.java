package sg.rail.router.sgroutingservice.service.implement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import sg.rail.router.sgroutingservice.models.Station;
import sg.rail.router.sgroutingservice.service.FileParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@Scope("singleton")
public class CsvParser implements FileParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(CsvParser.class);

  @Override
  public List<Station> parseFile(String filePath, String columnSeparator) throws IOException {
    Resource resource = new ClassPathResource(filePath);
    List<Station> stationList = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
      final var firstLine = br.readLine();// this will read the header and skip it (it's the csv header)
      LOGGER.debug("CSV Headers: {}", firstLine);
      String line;
      while ((line = br.readLine()) != null) {
        String[] stationData = line.split(columnSeparator);
        LocalDate openingDate;
        openingDate = parseAndGetLocalDate(stationData);
        Station station = new Station(stationData[0], stationData[1], openingDate);
        stationList.add(station);
      }
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
      throw new IOException("Can't parse the csv file");
    }
    return stationList;
  }

  private LocalDate parseAndGetLocalDate(String[] stations) {
    final LocalDate openingDate;
    if (stations[2].matches("(\\d{1,2}\\s\\w+\\s\\d{4})")) { // for complete date
      openingDate = LocalDate.parse(stations[2], DateTimeFormatter.ofPattern("d MMMM yyyy"));
    } else if (stations[2].matches("(\\s?\\w+\\s\\d{4})")) { // those missing date considering as 1 day of month
      openingDate = LocalDate.parse("1 " + stations[2], DateTimeFormatter.ofPattern("d MMMM yyyy"));
    } else {
      throw new DateTimeParseException("Unable to parse opening date", stations[2], 0);
    }
    return openingDate;
  }

}


