package sg.rail.router.sgroutingservice.models;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class StationTest {

  private Station ns01 = new Station("NS01", "Jurong East", LocalDateTime.of(1990, 3, 10, 0, 0, 0).toLocalDate());
  private Station ew24 = new Station("EW24", "Jurong East", LocalDateTime.of(1998, 11, 5, 0, 0, 0).toLocalDate());
  private Station ns15 = new Station("NS15", "Yio Chu Kang", LocalDateTime.of(1987, 11, 18, 0, 0, 0).toLocalDate());
  private Station cc12 = new Station("CC12", "Bartley", LocalDateTime.of(2009, 5, 28, 0, 0, 0).toLocalDate());
  private Station dt12 = new Station("DT02", "Cashew", LocalDateTime.of(2015, 12, 27, 0, 0, 0).toLocalDate());
  private Station ew21 = new Station("EW21", "Buona Vista", LocalDateTime.of(1998, 03, 12, 0, 0, 0).toLocalDate());
  private Station ew22 = new Station("EW22", "Dover", LocalDateTime.of(2001, 10, 18, 0, 0, 0).toLocalDate());
  private Station ew23 = new Station("EW23", "Clementi", LocalDateTime.of(1988, 03, 12, 0, 0, 0).toLocalDate());
  private Station ew25 = new Station("EW25", "Chinese Garden", LocalDateTime.of(1988, 11, 5, 0, 0, 0).toLocalDate());
  private Station te19 = new Station("TE19", "Shenton Way", LocalDateTime.of(2021, 12, 31, 0, 0, 0).toLocalDate());

  private List<Station> stationList = new ArrayList<>();

  @Test
  void setCodeTest() {
    assertEquals("NS", ns01.getLineCode(), "line is not matched");
    assertEquals("NS", ns15.getLineCode(), "line is not matched");
    assertEquals("CC", cc12.getLineCode(), "line is not matched");
    assertEquals("DT", dt12.getLineCode(), "line is not matched");
  }

  @Test
  void stationNumberTest() {
    assertEquals(01, ns01.getNumber(), "number is not matched");
    assertEquals(15, ns15.getNumber(), "number is not matched");
    assertEquals(12, cc12.getNumber(), "number is not matched");
    assertEquals(02, dt12.getNumber(), "number is not matched");
  }

  @Test
  void isSameTrainLineTest() {
    assertFalse(ns01.isSameTrainLine(ew24), "is same line");
    assertFalse(ns01.isSameTrainLine(cc12), "is same line");
    assertTrue(ns01.isSameTrainLine(ns15), "not the same line");
  }

  @Test
  void equalsStationsTest() {
    ns01.setInterchange(true);
    ew24.setInterchange(true);
    assertEquals(ns01, ew24, "not the same station");
    assertNotEquals(ns01, cc12, "same station");
    assertNotEquals(new NotStation(), ns01, "same type class");
    assertThat(ns01, Matchers.anyOf(Matchers.not(new NotStation()), Matchers.not((null))));
  }

  @Test
  void hashCodeTest() {
    assertNotEquals(ns01.hashCode(), ew24.hashCode(), "has same hash code");
    assertNotEquals(ns01.hashCode(), cc12.hashCode(), "has same hash code");
    assertEquals(ns01.hashCode(), ns01.hashCode(), "not same hash code");
  }

  @Test
  void compareToTest() {
    stationList = Arrays.asList(ns01, ns15, ew24, cc12, dt12, ew21, ew22, ew23, ew25);
    List<Station> sortedStationList = Arrays.asList(cc12, dt12, ew21, ew22, ew23, ew24, ew25, ns01, ns15);
    Collections.sort(stationList);
    assertEquals(sortedStationList, stationList, "list are not the same");
  }

  @Test
  void isOperableStationTest() {
    assertTrue(ns01.isOpenedStation(), "is not in operation");
    assertFalse(te19.isOpenedStation(), "is not in operation");
  }

  @Test
  void otherPropertyTest() {
    Station ns011 = new Station();
    ns011.setName("Jurong East");
    ns011.setNumber(1);
    ns011.setLineCode("EW");
    ns011.setCode("EW24");
    List<String> tempList = Collections.singletonList("NS01");
    ns011.setCodes(tempList);
    List<String> lines = Arrays.asList("EW", "NS");
    ns011.setLines(lines);
    ns011.setInterchange(true);
    LocalDate localDate = LocalDateTime.of(1990, 3, 10, 0, 0, 0).toLocalDate();
    ns011.setStartingDate(localDate);

    assertEquals("EW24", ns011.getCode(), "station code is not same");
    assertEquals("EW", ns011.getLineCode(), "station line code is not same");
    assertEquals(24, ns011.getNumber(), "station number is not same");
    assertEquals("Jurong East", ns011.getName(), "station name is not same");
    assertEquals(tempList, ns011.getCodes(), "station codes are different");
    assertEquals(lines, ns011.getLines(), "station lines are not same");
    assertEquals(localDate, ns011.getStartingDate(), "station starting date is not same");
    assertTrue(ns011.isInterchange(), "station is not interchange");

  }

  @Test
  void isSameStation(){
    assertTrue(ns01.isIntersectStation(ew24),"not the same station");
  }

  class NotStation {

  }


}
