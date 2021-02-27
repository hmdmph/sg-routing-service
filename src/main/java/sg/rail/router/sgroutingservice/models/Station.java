package sg.rail.router.sgroutingservice.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Station implements Comparable<Station> {

  private String code;
  private String name;
  private Integer number;
  private String lineCode;
  private boolean isInterchange;
  private LocalDate startingDate;
  private List<String> codes;
  private List<String> lines;

  public Station(String code, String name, LocalDate startingDate) {
    this.code = code;
    this.name = name;
    this.startingDate = startingDate;
    this.codes = new ArrayList<>();
    this.lines = new ArrayList<>();
    setCode(code);
  }

  @Override
  public int compareTo(Station o) {
    // if same line then order it by station number or else order by line
    if (lineCode.compareTo(o.lineCode) == 0) {
      return number.compareTo(o.number); // same rail line in order 1,2,3...
    } else {
      return lineCode.compareTo(o.lineCode); // different rail lines
    }
  }

  @Override
  public boolean equals(Object o) {

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Station otherStation = (Station) o;

    if (otherStation.isInterchange || isInterchange) {
      return name.equals(otherStation.name);
    }
    return code.equals(otherStation.code);

  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + code.hashCode();
    return result;
  }

  public boolean isSameTrainLine(Station otherStation) {
    return getLineCode().equals(otherStation.getLineCode());
  }

  public boolean isOpenedStation() {
    return startingDate.isBefore(LocalDate.now());
  }

  public boolean isIntersectStation(Station anotherStation) {
    return name.equals(anotherStation.name);
  }

  public void setCode(String code) {
    this.code = code; // EW01
    this.lineCode = code.substring(0, 2); // EW
    this.number = Integer.valueOf(code.substring(2)); // 01, 1
  }


}
