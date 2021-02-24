package com.zendesk.zmrt.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Route {
    private final List<String> sequence;
}
