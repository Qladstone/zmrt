package com.zendesk.zmrt.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StationCode {
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
