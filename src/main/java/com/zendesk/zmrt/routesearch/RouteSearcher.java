package com.zendesk.zmrt.routesearch;

import com.zendesk.zmrt.routesearch.RouteSearchGraph.Solution;
import com.zendesk.zmrt.routesearch.StationLinesForDay.StationCode;
import com.zendesk.zmrt.routesearch.StationLinesForDay.StationInLine;
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
            routesFound.add(constructRouteFromSolution(solution, stationLines));
        }
        return routesFound;
    }

    private Route constructRouteFromSolution(Solution solution, StationLinesForDay stationLines) {
        List<String> sequence = new ArrayList<>();
        List<String> path = solution.getPath();
        int p = 0;
        while (p < path.size()) {
            List<String> stationNamesForSubsequence = new ArrayList<>();
            String stationName = path.get(p);
            Set<String> commonPrefixes = stationLines.getStationPrefixesOfStationName(stationName);
            stationNamesForSubsequence.add(stationName);
            p++;
            while (p < path.size()) {
                stationName = path.get(p);
                Set<String> newCommonPrefixes = commonPrefixesOf(commonPrefixes, stationName, stationLines);
                if (newCommonPrefixes.isEmpty()) {
                    if (stationNamesForSubsequence.size() == 1) {
                        throw new IllegalStateException(String.format(
                                "For a valid solution, two stations in a route must share a line. " +
                                        "Stations violating property: (%s,%s)",
                                stationNamesForSubsequence.get(0), stationName));
                    }
                    break;
                }
                commonPrefixes = newCommonPrefixes;
                stationNamesForSubsequence.add(stationName);
                p++;
            }
            String prefix = commonPrefixes.stream().findAny().get();
            for (String name : stationNamesForSubsequence) {
                StationCode stationCode = stationLines.getStationCodeOfStationNameWithPrefix(name, prefix);
                sequence.add(stationCode.toStationCodeString());
            }
        }
        return new Route(sequence);
    }

    private Set<String> commonPrefixesOf(Set<String> prefixes, String stationName, StationLinesForDay stationLines) {
        Set<String> stationPrefixes = stationLines.getStationPrefixesOfStationName(stationName);
        Set<String> commonPrefixes = new HashSet<>(prefixes);
        commonPrefixes.retainAll(stationPrefixes);
        return commonPrefixes;
    }
}
