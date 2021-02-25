package com.zendesk.zmrt.raildata;

import com.zendesk.zmrt.common.StationCode;
import com.zendesk.zmrt.routesearch.StationLinesForDay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class StationsWithScheduleTest {

    private StationsWithSchedule stationsWithSchedule;

    @BeforeEach
    void setUp() {
        stationsWithSchedule = new StationsWithSchedule();
        stationsWithSchedule.addScheduledStation(
                LocalDate.of(1988, 3, 12), StationCode.of("EW18"), "Redhill");
        stationsWithSchedule.addScheduledStation(
                LocalDate.of(1988, 3, 12), StationCode.of("EW17"), "Tiong Bahru");
        stationsWithSchedule.addScheduledStation(
                LocalDate.of(1988, 3, 12), StationCode.of("EW24"), "Jurong East");
        stationsWithSchedule.addScheduledStation(
                LocalDate.of(1996, 2, 10), StationCode.of("NS1"), "Jurong East");
        stationsWithSchedule.addScheduledStation(
                LocalDate.of(1996, 2, 10), StationCode.of("NS5"), "Yew Tee");
        stationsWithSchedule.addScheduledStation(
                LocalDate.of(1996, 2, 10), StationCode.of("NS7"), "Kranji");
    }

    @Test
    void constructStationLinesFor() {
        StationLinesForDay stationLines = stationsWithSchedule.constructStationLinesFor(
                LocalDate.of(1989, 5, 10));
        assertThat(stationLines.containsStationName("Redhill")).isTrue();
        assertThat(stationLines.containsStationName("Tiong Bahru")).isTrue();
        assertThat(stationLines.containsStationName("Jurong East")).isTrue();
        assertThat(stationLines.containsStationName("Yew Tee")).isFalse();
        assertThat(stationLines.containsStationName("Kranji")).isFalse();
        assertThat(stationLines.getStationPrefixes()).hasSize(1);
        assertThat(stationLines.getStationPrefixes()).contains("EW");
        assertThat(stationLines.getStationsOfLine("EW")).hasSize(3);
    }
}