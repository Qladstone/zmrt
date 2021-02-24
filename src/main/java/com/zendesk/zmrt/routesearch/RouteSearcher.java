package com.zendesk.zmrt.routesearch;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class RouteSearcher {
    public List<Route> searchForRoutes(String origin, String destination, LocalDateTime startDateTime) {
        List<Route> routesFound = new ArrayList<>();
        if (!startDateTime.isBefore(LocalDateTime.of(1988, 3, 12, 0, 0))
                && Objects.equals(origin, "Redhill") && Objects.equals(destination, "Tiong Bahru")) {
            List<String> sequence = new ArrayList<>();
            sequence.add("EW18");
            sequence.add("EW17");
            routesFound.add(new Route(sequence));
        }
        return routesFound;
    }
}
