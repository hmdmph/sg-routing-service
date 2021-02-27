package sg.rail.router.sgroutingservice.service.implement;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import sg.rail.router.sgroutingservice.models.Station;
import sg.rail.router.sgroutingservice.service.FileParser;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CSVParserServiceTest {

  @InjectMocks
  FileParser csvParserService = new CsvParser();

  private String fileName = "stationmap.csv";
  private String fileWithWrongInputName1 = "test/stationmap_with_wrong_input.csv";
  private String fileWithNoDate = "test/stationmap_with_nodate.csv";
  private String fileWithWrongFormat = "test/stationmap_wrong_fomat.csv";


  @Test
  public void testParseCsv() throws IOException {
    List<Station> stations = csvParserService.parseFile(fileName, ",");
    assertTrue("station count does not match", stations.size() == 166);

    assertTrue("Wrong line code", stations.get(0).getLineCode().equals("NS"));
    assertTrue("Wrong station code", stations.get(0).getCode().equals("NS1"));
    assertTrue("Wrong station name", stations.get(0).getName().equals("Jurong East"));
    assertTrue("Wrong start date", stations.get(0).getStartingDate().equals(LocalDate.of(1990, 03, 10)));
    assertTrue("Station is not operational", stations.get(0).isOpenedStation());
  }


  @Test
  public void testExceptions() throws IOException {
    Exception exception1 = assertThrows(DateTimeParseException.class, () -> {
      List<Station> stations2 = csvParserService.parseFile(fileWithWrongInputName1, ",");
    });

    assertTrue("wrong exception", "Text '10 12 1990' could not be parsed at index 3".contains(exception1.getMessage()));

    List<Station> stations3 = csvParserService.parseFile(fileWithNoDate, ",");
    assertFalse(stations3.isEmpty(), "stations are not empty");

    Exception exception4 = assertThrows(IOException.class, () -> {
      List<Station> stations4 = csvParserService.parseFile("some_wrongFile.csv", ",");
    });

    assertTrue("wrong exception", "Can't parse the csv file".contains(exception4.getMessage()));

    Exception exception5 = assertThrows(DateTimeParseException.class, () -> {
      List<Station> stations5 = csvParserService.parseFile(fileWithWrongFormat, ",");
    });

    assertTrue("wrong exception", "Unable to parse opening date".contains(exception5.getMessage()));

  }


}
