package com.zendesk.zmrt.routesearch;

import com.zendesk.zmrt.routesearch.RouteSearchGraph.Solution;
import com.zendesk.zmrt.routesearch.StationLinesForDay.StationCode;
import com.zendesk.zmrt.routesearch.StationLinesForDay.StationInLine;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class RouteSearcher {

    private final RouteConstructor routeConstructor;

    @Inject
    public RouteSearcher(RouteConstructor routeConstructor) {
        this.routeConstructor = routeConstructor;
    }

    public List<Route> searchForRoutes(String origin, String destination, LocalDateTime startDateTime) {
        StationLinesForDay stationLines = constructStationLines(startDateTime);
        return searchForRoutesInLines(origin, destination, stationLines);
    }

    private StationLinesForDay constructStationLines(LocalDateTime startDateTime) {
        StationLinesForDay stationLines = new StationLinesForDay();
        if (!startDateTime.isBefore(LocalDateTime.of(1988, 3, 12, 0, 0))) {
            stationLines.addStation("Redhill", StationCode.of("EW18"));
            stationLines.addStation("Tiong Bahru", StationCode.of("EW17"));
            stationLines.addStation("Jurong East", StationCode.of("EW24"));
        }
        if (!startDateTime.isBefore(LocalDateTime.of(1996, 2, 10, 0, 0))) {
            stationLines.addStation("Yew Tee", StationCode.of("NS5"));
            stationLines.addStation("Kranji", StationCode.of("NS7"));
            stationLines.addStation("Jurong East", StationCode.of("NS1"));
        }
        return stationLines;
    }

    private List<Route> searchForRoutesInLines(String origin, String destination, StationLinesForDay stationLines) {
        if (!stationLines.containsStationName(origin) || !stationLines.containsStationName(destination)) {
            return Collections.emptyList();
        }
        RouteSearchGraph graph = constructGraph(stationLines);
        List<Solution> solutions = graph.goalSearch(origin, destination);
        return constructRoutesFromSolutions(solutions, stationLines);
    }

    private RouteSearchGraph constructGraph(StationLinesForDay stationLines) {
        RouteSearchGraph graph = new RouteSearchGraph();
        for (String stationPrefix : stationLines.getStationPrefixes()) {
            List<StationInLine> stations = stationLines.getStationsOfLine(stationPrefix);
            String sJ = stations.get(0).getStationName();
            graph.addVertex(sJ);
            for (int p = 1; p < stations.size(); p++) {
                String sI = sJ;
                sJ = stations.get(p).getStationName();
                graph.addVertex(sJ);
                graph.addEdge(sI, sJ);
            }
        }
        return graph;
    }

    private List<Route> constructRoutesFromSolutions(List<Solution> solutions, StationLinesForDay stationLines) {
        List<Route> routesFound = new ArrayList<>();
        for (Solution solution : solutions) {
            routesFound.add(routeConstructor.constructRouteFromSolution(solution, stationLines));
        }
        return routesFound;
    }
}
