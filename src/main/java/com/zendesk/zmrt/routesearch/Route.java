package com.zendesk.zmrt.routesearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Route {
    @JsonProperty("sequence")
    private final List<String> sequence;
}
