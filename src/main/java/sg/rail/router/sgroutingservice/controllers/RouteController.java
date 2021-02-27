package sg.rail.router.sgroutingservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sg.rail.router.sgroutingservice.models.HeuristicRoute;
import sg.rail.router.sgroutingservice.service.HeuristicRoutesService;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/sg/rail")
@RestController
public class RouteController {

  private final HeuristicRoutesService sgRailRouterService;

  @Autowired
  public RouteController(HeuristicRoutesService sgRailRouterService) {
    this.sgRailRouterService = sgRailRouterService;
  }

  @GetMapping("/routes")
  public ResponseEntity<Object> findById(@RequestParam String source, @RequestParam String destination, @RequestParam("time")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time) {
    Map<String, Object> responseMap = new LinkedHashMap<>();
    responseMap.put("title", "Travel from " + source + " to " + destination);
    try {
      if (source == null || source.isEmpty() || destination == null || destination.isEmpty()) {
        return new ResponseEntity<>("Query Params of source and destination are mandatory!", HttpStatus.BAD_REQUEST);
      } else {
        List<HeuristicRoute> result = sgRailRouterService.findRoutes(source, destination, time);
        if (result.isEmpty()) {
          responseMap.put("message", "No available routes for the given stations and time");
          return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
        } else {
          responseMap.put("routes", result);
          return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }
      }
    } catch (Exception e) {
      responseMap.put("message", e.getMessage());
      return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


}
