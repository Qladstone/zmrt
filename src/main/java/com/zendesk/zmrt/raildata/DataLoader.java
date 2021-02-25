package com.zendesk.zmrt.raildata;

import com.zendesk.zmrt.common.StationCode;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class DataLoader {
    public StationsWithSchedule loadData(String filePath, boolean withHeader, DateTimeFormatter dateTimeFormatter)
            throws IOException {
        StationsWithSchedule stations = new StationsWithSchedule();
        try (BufferedReader reader = new BufferedReader(new FileReader((filePath)))) {
            if (withHeader) reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineElements = line.split(",");
                StationCode stationCode = StationCode.of(lineElements[0]);
                String stationName = lineElements[1];
                LocalDate openingDate;
                try {
                    openingDate = LocalDate.parse(lineElements[2], dateTimeFormatter);
                } catch (DateTimeParseException e) {
                    System.out.println("Failed to parse opening date " + lineElements[2] + ". Skipping entry.");
                    continue;
                }
                stations.addScheduledStation(openingDate, stationCode, stationName);
            }
        }
        return stations;
    }

    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        System.out.println(LocalDate.parse("12 January 2008", formatter));
    }
}
