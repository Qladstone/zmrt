package com.zendesk.zmrt.raildata;

import com.zendesk.zmrt.common.StationCode;
import com.zendesk.zmrt.routesearch.StationLinesForDay;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StationsWithSchedule {

    private final List<ScheduledStation> scheduledStations = new ArrayList<>();
    public void addScheduledStation(LocalDate openingDate, StationCode stationCode, String stationName) {
        scheduledStations.add(new ScheduledStation(openingDate, stationCode, stationName));
    }

    public StationLinesForDay constructStationLinesFor(LocalDate localDate) {
        StationLinesForDay stationLines = new StationLinesForDay();
        for (ScheduledStation station : scheduledStations) {
            if (!station.openingDate.isAfter(localDate)) {
                stationLines.addStation(station.stationName, station.stationCode);
            }
        }
        return stationLines;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ScheduledStation {
        private final LocalDate openingDate;
        private final StationCode stationCode;
        private final String stationName;
    }

    public static StationsWithSchedule getInstance() {
        return INSTANCE;
    }

    private static final StationsWithSchedule INSTANCE =
            new DataLoader().loadData(
                    "StationMap.csv", true, DateTimeFormatter.ofPattern("d MMMM yyyy"));
}
