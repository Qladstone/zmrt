package com.zendesk.zmrt;

import com.zendesk.zmrt.payload.RoutesResponseBody;
import com.zendesk.zmrt.routesearch.Route;
import com.zendesk.zmrt.routesearch.RouteSearcher;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class Controller {

    private final RouteSearcher routeSearcher;

    @Inject
    public Controller(RouteSearcher routeSearcher) {
        this.routeSearcher = routeSearcher;
    }

    @GetMapping("/")
    public String index() {
        return "Zendesk MRT is running.";
    }

    @GetMapping("/routes")
    public RoutesResponseBody routes(@RequestParam("origin") String origin,
                                     @RequestParam("destination") String destination,
                                     @RequestParam("start-datetime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                 LocalDateTime startDateTime) {
        List<Route> routesFound = routeSearcher.searchForRoutes(origin, destination, startDateTime);
        return new RoutesResponseBody(routesFound);
    }
}
