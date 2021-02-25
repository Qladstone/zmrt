package com.zendesk.zmrt.routesearch;

import com.zendesk.zmrt.raildata.DataLoader;
import com.zendesk.zmrt.raildata.StationsWithSchedule;
import com.zendesk.zmrt.routesearch.RouteSearchGraph.Solution;
import com.zendesk.zmrt.routesearch.StationLinesForDay.StationInLine;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class RouteSearcher {

    private final DataLoader dataLoader;
    private final RouteConstructor routeConstructor;

    @Inject
    public RouteSearcher(DataLoader dataLoader, RouteConstructor routeConstructor) {
        this.dataLoader = dataLoader;
        this.routeConstructor = routeConstructor;
    }

    public List<Route> searchForRoutes(String origin, String destination, LocalDateTime startDateTime) {
        StationLinesForDay stationLines = constructStationLines(startDateTime);
        return searchForRoutesInLines(origin, destination, stationLines);
    }

    private StationLinesForDay constructStationLines(LocalDateTime startDateTime) {

        StationsWithSchedule stationsWithSchedule;
        try {
            stationsWithSchedule = dataLoader.loadData("StationMap.csv", true, DateTimeFormatter.ofPattern("d MMMM yyyy"));
        } catch (IOException e) {
            System.out.println("Failed to read data file: " + e);
            return new StationLinesForDay();
        }
        return stationsWithSchedule.constructStationLinesFor(startDateTime.toLocalDate());
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
