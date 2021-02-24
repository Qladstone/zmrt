package com.zendesk.zmrt.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class RoutesResponseBody {
    private final List<Route> routes;
}
