package sg.rail.router.sgroutingservice.service.implement;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import sg.rail.router.sgroutingservice.service.TravelTimeOperationService;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Scope("singleton")
public class MRTTravelTimeOperationService implements TravelTimeOperationService {

  public static final Integer PEAK_TRAVEL_TIME_WITH_SPECIFIC_DURATION = 12;
  public static final Integer PEAK_TRAVEL_TIME_WITH_DEFAULT_DURATION = 10;
  public static final Integer NIGHT_TRAVEL_TIME_WITH_SPECIFIC_DURATION = 8;
  public static final Integer NIGHT_TRAVEL_TIME_WITH_DEFAULT_DURATION = 10;
  public static final Integer NON_PEAK_TRAVEL_TIME_WITH_SPECIFIC_DURATION = 8;
  public static final Integer NON_PEAK_TRAVEL_TIME_WITH_DEFAULT_DURATION = 10;
  public static final Integer LINE_CHANGE_PEAK_WAIT_DURATION = 15;
  public static final Integer LINE_CHANGE_DEFAULT_WAIT_DURATION = 10;
  public static final List<String> NIGHT_TRAVEL_STATIONS_WITH_SPECIFIC_DURATION = Collections.singletonList("TE");
  protected static final List<String> NON_OPERATION_STATIONS_AT_NIGHTTIME = Arrays.asList("DT", "CG", "CE");
  private static final List<String> PEAK_TRAVEL_STATIONS_WITH_SPECIFIC_DURATION = Arrays.asList("NS", "NE");
  protected static final List<String> NON_PEAK_TRAVEL_STATIONS_WITH_SPECIFIC_DURATION = Arrays.asList("DT", "TE");

  @Override
  public Integer getTravelTimeBetweenStations(String lineCode, LocalDateTime localDateTime) {
    if (isPeakTime(localDateTime)) { // peak time specific
      if (PEAK_TRAVEL_STATIONS_WITH_SPECIFIC_DURATION.contains(lineCode.toUpperCase())) {
        return PEAK_TRAVEL_TIME_WITH_SPECIFIC_DURATION;
      } else {
        return PEAK_TRAVEL_TIME_WITH_DEFAULT_DURATION;
      }
    } else if (isNightTime(localDateTime)) { // night time specific
      if (NIGHT_TRAVEL_STATIONS_WITH_SPECIFIC_DURATION.contains(lineCode.toUpperCase())) {
        return NIGHT_TRAVEL_TIME_WITH_SPECIFIC_DURATION;
      } else {
        return NIGHT_TRAVEL_TIME_WITH_DEFAULT_DURATION;
      }
    } else { // non-peak ( all other times)
      if (NON_PEAK_TRAVEL_STATIONS_WITH_SPECIFIC_DURATION.contains(lineCode.toUpperCase())) {
        return NON_PEAK_TRAVEL_TIME_WITH_SPECIFIC_DURATION;
      } else {
        return NON_PEAK_TRAVEL_TIME_WITH_DEFAULT_DURATION;
      }
    }
  }

  @Override
  public Integer getInterchangeWaitingTime(LocalDateTime localDateTime) {
    if (isPeakTime(localDateTime)) {
      return LINE_CHANGE_PEAK_WAIT_DURATION;
    } else {
      return LINE_CHANGE_DEFAULT_WAIT_DURATION;
    }
  }

  @Override
  public boolean isLineOperational(String lineCode, LocalDateTime localDateTime) {
    return !isNightTime(localDateTime) || !NON_OPERATION_STATIONS_AT_NIGHTTIME.contains(lineCode.toUpperCase());
  }

  @Override
  public boolean isPeakTime(LocalDateTime localDateTime) {
    // week days with specific hours

    boolean isWeekdays =
        localDateTime.getDayOfWeek().equals(DayOfWeek.MONDAY) || localDateTime.getDayOfWeek().equals(DayOfWeek.TUESDAY)
            || localDateTime.getDayOfWeek().equals(DayOfWeek.WEDNESDAY) || localDateTime.getDayOfWeek().equals(DayOfWeek.THURSDAY)
            || localDateTime.getDayOfWeek().equals(DayOfWeek.FRIDAY);

    boolean isPeakHours = (localDateTime.toLocalTime().isAfter(LocalTime.of(6, 0))
        && localDateTime.toLocalTime().isBefore(LocalTime.of(9, 0)))
        || (localDateTime.toLocalTime().isAfter(LocalTime.of(18, 0))
        && localDateTime.toLocalTime().isBefore(LocalTime.of(21, 0)));

    return isWeekdays && isPeakHours;
  }

  @Override
  public boolean isNightTime(LocalDateTime localDateTime) {
    // whole week with specific hours

    return (localDateTime.toLocalTime().isAfter(LocalTime.of(22, 0))
        && localDateTime.toLocalTime().isBefore(LocalTime.of(23, 59)))
        || (localDateTime.toLocalTime().isAfter(LocalTime.of(0, 0))
        && localDateTime.toLocalTime().isBefore(LocalTime.of(6, 0)));
  }
}
