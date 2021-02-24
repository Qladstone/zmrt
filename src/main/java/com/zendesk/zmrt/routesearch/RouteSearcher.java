package com.zendesk.zmrt.routesearch;

import com.zendesk.zmrt.routesearch.StationLinesForDay.StationCode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class RouteSearcher {
    public List<Route> searchForRoutes(String origin, String destination, LocalDateTime startDateTime) {
        StationLinesForDay stationLines = constructStationLines(startDateTime);
        return searchForRoutesInLines(origin, destination, stationLines);
    }

    private StationLinesForDay constructStationLines(LocalDateTime startDateTime) {
        StationLinesForDay stationLines = new StationLinesForDay();
        if (!startDateTime.isBefore(LocalDateTime.of(1988, 3, 12, 0, 0))) {
            stationLines.addStation("Redhill", StationCode.of("EW18"));
            stationLines.addStation("Tiong Bahru", StationCode.of("EW17"));
        }
        if (!startDateTime.isBefore(LocalDateTime.of(1996, 2, 10, 0, 0))) {
            stationLines.addStation("Yew Tee", StationCode.of("NS5"));
            stationLines.addStation("Kranji", StationCode.of("NS7"));
        }
        return stationLines;
    }

    private List<Route> searchForRoutesInLines(String origin, String destination, StationLinesForDay stationLines) {
        List<Route> routesFound = new ArrayList<>();

        if (!stationLines.containsStationName(origin) || !stationLines.containsStationName(destination)) {
            return routesFound;
        }

        Set<String> commonPrefixes = commonPrefixesOf(origin, destination, stationLines);
        if (!commonPrefixes.isEmpty()) {
            String commonPrefix = commonPrefixes.stream().findAny().get();
            List<String> sequence = new ArrayList<>();
            sequence.add(stationLines
                    .getStationCodeOfStationNameWithPrefix(origin, commonPrefix)
                    .toStationCodeString());
            sequence.add(stationLines
                    .getStationCodeOfStationNameWithPrefix(destination, commonPrefix)
                    .toStationCodeString());
            routesFound.add(new Route(sequence));
        }
        return routesFound;
    }

    private Set<String> commonPrefixesOf(String origin, String destination, StationLinesForDay stationLines) {
        Set<String> prefixesOfOrigin = stationLines.getStationPrefixesOfStationName(origin);
        Set<String> prefixesOfDestination = stationLines.getStationPrefixesOfStationName(destination);
        Set<String> commonPrefixes = new HashSet<>(prefixesOfOrigin);
        commonPrefixes.retainAll(prefixesOfDestination);
        return commonPrefixes;
    }
}
