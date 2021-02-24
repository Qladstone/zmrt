package com.zendesk.zmrt.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zendesk.zmrt.routesearch.Route;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class RoutesResponseBody {
    @JsonProperty("routes")
    private final List<Route> routes;
}
