package sg.rail.router.sgroutingservice.service.implement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static sg.rail.router.sgroutingservice.service.implement.MRTTravelTimeOperationService.LINE_CHANGE_DEFAULT_WAIT_DURATION;
import static sg.rail.router.sgroutingservice.service.implement.MRTTravelTimeOperationService.LINE_CHANGE_PEAK_WAIT_DURATION;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import sg.rail.router.sgroutingservice.service.TravelTimeOperationService;

import java.time.LocalDateTime;

@RunWith(MockitoJUnitRunner.class)
public class MRTTravelTimeOperationServiceTest {

  @InjectMocks
  TravelTimeOperationService travelTimeService = new MRTTravelTimeOperationService();

  // 2021-02-02T17:00 - Tuesday/non-peak
  LocalDateTime date1 = LocalDateTime.of(2021, 2, 2, 7, 15, 0);

  // 2021-02-03T22:50 - Wednesday/peak
  LocalDateTime date2 = LocalDateTime.of(2021, 2, 3, 22, 50, 0);

  // 2021-02-13T18:50 - Saturday/non-peak
  LocalDateTime date3 = LocalDateTime.of(2021, 2, 13, 18, 50, 0);

  // 2021-02-12T2:10 - Friday/non-peak
  LocalDateTime date4 = LocalDateTime.of(2021, 2, 12, 2, 10, 0);

  // 2021-02-07T5:50 - Sunday/non-peak
  LocalDateTime date5 = LocalDateTime.of(2021, 2, 7, 5, 50, 0);

  // 2021-02-15T5:50 - Monday/peak
  LocalDateTime date6 = LocalDateTime.of(2021, 2, 15, 6, 50, 0);

  // 2021-02-18T19:50 - Thursday/peak
  LocalDateTime date7 = LocalDateTime.of(2021, 2, 18, 19, 50, 0);

  @Test
  public void testisNightTime() {
    Assertions.assertFalse(travelTimeService.isNightTime(date1), "time is night-time");
    Assertions.assertTrue(travelTimeService.isNightTime(date2), "time is not night-time");
    Assertions.assertFalse(travelTimeService.isNightTime(date3), "time is night-time");
    Assertions.assertTrue(travelTimeService.isNightTime(date4), "time is not night-time");
  }

  @Test
  public void testInterchangeWaitingTime() {
    assertEquals(LINE_CHANGE_PEAK_WAIT_DURATION, travelTimeService.getInterchangeWaitingTime(date1), "wrong waiting time");
    assertEquals(LINE_CHANGE_DEFAULT_WAIT_DURATION, travelTimeService.getInterchangeWaitingTime(date2), "wrong waiting " +
        "time");
    assertEquals(LINE_CHANGE_DEFAULT_WAIT_DURATION, travelTimeService.getInterchangeWaitingTime(date3), "wrong waiting " +
        "time");

  }

  @Test
  public void testisPeakTime() {
    Assertions.assertTrue(travelTimeService.isPeakTime(date1), "time is peak-time");
    Assertions.assertFalse(travelTimeService.isPeakTime(date2), "time is peak-time");
    Assertions.assertFalse(travelTimeService.isPeakTime(date3), "time is peak-time");
    Assertions.assertFalse(travelTimeService.isPeakTime(date4), "time is peak-time");
    Assertions.assertTrue(travelTimeService.isPeakTime(date6), "time is not peak-time");
    Assertions.assertTrue(travelTimeService.isPeakTime(date7), "time is not peak-time");
  }

  @Test
  public void testisTrainOperational() {

    Assertions.assertTrue(travelTimeService.isLineOperational("EW", date1), "line is not operational");
    Assertions.assertTrue(travelTimeService.isLineOperational("DT", date1), "line is not operational");

    Assertions.assertTrue(travelTimeService.isLineOperational("NS", date2), "line is not operational");
    Assertions.assertFalse(travelTimeService.isLineOperational("CG", date2), "line is operational");

    Assertions.assertTrue(travelTimeService.isLineOperational("CG", date3), "line is not operational");
    Assertions.assertTrue(travelTimeService.isLineOperational("CE", date3), "line is not operational");

    Assertions.assertFalse(travelTimeService.isLineOperational("CE", date4), "line is operational");
    Assertions.assertTrue(travelTimeService.isLineOperational("EW", date5), "line is not operational");
  }

  @Test
  public void testTravelTimeBetweenStations() {
    // 2020-03-03T02:00 - Monday
    LocalDateTime date1 = LocalDateTime.of(2020, 3, 2, 7, 0, 0);

    // 2020-03-04T23:30 - Wednesday
    LocalDateTime date2 = LocalDateTime.of(2020, 3, 4, 23, 30, 0);

    // 2020-03-14T18:40 - Saturday
    LocalDateTime date3 = LocalDateTime.of(2020, 3, 14, 18, 40, 0);

    // 2020-03-20T2:50 - Friday
    LocalDateTime date4 = LocalDateTime.of(2020, 3, 20, 2, 50, 0);

    // 2020-03-21T10:50 - Sunday
    LocalDateTime date5 = LocalDateTime.of(2020, 3, 20, 10, 50, 0);

    assertEquals(10, (int) travelTimeService.getTravelTimeBetweenStations("EW", date1), "wrong travel time");
    assertEquals(12, (int) travelTimeService.getTravelTimeBetweenStations("NS", date1), "wrong travel time");
    Assertions.assertNotEquals(10, (int) travelTimeService.getTravelTimeBetweenStations("NE", date1), "wrong travel time");

    assertEquals(10, (int) travelTimeService.getTravelTimeBetweenStations("EW", date2), "wrong travel time");
    assertEquals(8, (int) travelTimeService.getTravelTimeBetweenStations("TE", date2), "wrong travel time");

    assertEquals(10, (int) travelTimeService.getTravelTimeBetweenStations("EW", date3), "wrong travel time");
    assertEquals(8, (int) travelTimeService.getTravelTimeBetweenStations("TE", date4), "wrong travel time");

    assertEquals(8, (int) travelTimeService.getTravelTimeBetweenStations("DT", date5), "wrong travel time");
    assertEquals(8, (int) travelTimeService.getTravelTimeBetweenStations("TE", date5), "wrong travel time");
    assertEquals(10, (int) travelTimeService.getTravelTimeBetweenStations("EW", date5), "wrong travel time");
  }

}
