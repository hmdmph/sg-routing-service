package sg.rail.router.sgroutingservice.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface TravelTimeOperationService {

  Integer getTravelTimeBetweenStations(String lineCode, LocalDateTime localDateTime);

  Integer getInterchangeWaitingTime(LocalDateTime localDateTime);

  boolean isLineOperational(String lineCode, LocalDateTime localDateTime);

  boolean isPeakTime(LocalDateTime localDateTime);

  boolean isNightTime(LocalDateTime localDateTime);

}
