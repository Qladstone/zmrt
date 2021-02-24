package com.zendesk.zmrt.routesearch;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

public class StationLinesForDay {
    private final Map<String, List<StationInLine>> stationLines;
    private final Map<String, Set<StationCode>> stationCodesOfStationNames;
    private boolean isSorted = true;

    public StationLinesForDay() {
        this.stationLines = new HashMap<>();
        this.stationCodesOfStationNames = new HashMap<>();
    }

    public void addStation(String stationName, StationCode stationCode) {
        if (!stationLines.containsKey(stationCode.prefix)) {
            stationLines.put(stationCode.prefix, new ArrayList<>());
        }
        stationLines.get(stationCode.prefix).add(new StationInLine(stationName, stationCode.suffix));
        isSorted = false;
        if (!stationCodesOfStationNames.containsKey(stationName)) {
            stationCodesOfStationNames.put(stationName, new HashSet<>());
        }
        stationCodesOfStationNames.get(stationName).add(stationCode);
    }

    public Set<String> getStationPrefixes() {
        return Collections.unmodifiableSet(stationLines.keySet());
    }

    public List<StationInLine> getStationsOfLine(String stationPrefix) {
        sortIfNotSorted();
        return Collections.unmodifiableList(stationLines.get(stationPrefix));
    }

    public boolean containsStationName(String stationName) {
        return stationCodesOfStationNames.get(stationName) != null;
    }

    public Set<StationCode> getStationCodesOfStationName(String stationName) {
        return Collections.unmodifiableSet(stationCodesOfStationNames.get(stationName));
    }

    public Set<String> getStationPrefixesOfStationName(String stationName) {
        return stationCodesOfStationNames.get(stationName).stream().map(StationCode::getPrefix)
                .collect(Collectors.toSet());
    }

    public StationCode getStationCodeOfStationNameWithPrefix(String stationName, String prefix) {
        for (StationCode stationCode : stationCodesOfStationNames.get(stationName)) {
            if (stationCode.prefix.equals(prefix)) return stationCode;
        }
        throw new IllegalArgumentException(stationName + " does not have a station code with prefix " + prefix);
    }

    private void sortIfNotSorted() {
        if (!isSorted) {
            for (List<StationInLine> stationLine : stationLines.values()) {
                stationLine.sort(Comparator.comparingInt(s -> s.stationCodeSuffix));
            }
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class StationInLine {
        private final String stationName;
        private final int stationCodeSuffix;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class StationCode {
        private final String prefix;
        private final int suffix;

        public String toStationCodeString() {
            return prefix + suffix;
        }

        public static StationCode of(String stationCodeString) {
            String prefix = stationCodeString.substring(0, 2);
            int suffix = Integer.parseInt(stationCodeString.substring(2, stationCodeString.length()));
            return new StationCode(prefix, suffix);
        }
    }
}
