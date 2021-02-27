package sg.rail.router.sgroutingservice.service;

import org.springframework.stereotype.Service;
import sg.rail.router.sgroutingservice.models.Station;

import java.io.IOException;
import java.util.List;

@Service
public interface FileParser {
  List<Station> parseFile(String filePath, String columnSeparator) throws IOException;
}
