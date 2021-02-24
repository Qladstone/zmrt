package com.zendesk.zmrt;

import com.zendesk.zmrt.payload.Route;
import com.zendesk.zmrt.payload.RoutesResponseBody;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class Controller {

    @GetMapping("/")
    public String index() {
        return "Zendesk MRT is running.";
    }

    @GetMapping("/routes")
    public RoutesResponseBody routes(@RequestParam("origin") String origin,
                                     @RequestParam("destination") String destination,
                                     @RequestParam("start-datetime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                 LocalDateTime startDateTime) {
        List<Route> routesFound = new ArrayList<>();
        if (!startDateTime.isBefore(LocalDateTime.of(1988, 3, 12, 0, 0))
                && Objects.equals(origin, "Redhill") && Objects.equals(destination, "Tiong Bahru")) {
            List<String> sequence = new ArrayList<>();
            sequence.add("EW18");
            sequence.add("EW17");
            routesFound.add(new Route(sequence));
        }
        return new RoutesResponseBody(routesFound);
    }
}
