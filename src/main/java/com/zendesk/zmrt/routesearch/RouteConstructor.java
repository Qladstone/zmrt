package com.zendesk.zmrt.routesearch;

import com.zendesk.zmrt.routesearch.StationLinesForDay.StationCode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RouteConstructor {
    public Route constructRouteFromSolution(RouteSearchGraph.Solution solution, StationLinesForDay stationLines) {
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
                    p--;
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
