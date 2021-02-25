package com.zendesk.zmrt.routesearch;

import com.zendesk.zmrt.common.StationCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static com.zendesk.zmrt.routesearch.RouteSearchGraph.Solution;

class RouteConstructorTest {

    private RouteConstructor routeConstructor;

    private StationLinesForDay stationLines;
    private Solution solution = Mockito.mock(Solution.class);

    @BeforeEach
    void setUp() {
        routeConstructor = new RouteConstructor();

        stationLines = new StationLinesForDay();
        stationLines.addStation("Tiong Bahru", StationCode.of("EW17"));
        stationLines.addStation("Redhill", StationCode.of("EW18"));
        stationLines.addStation("Jurong East", StationCode.of("EW24"));
        stationLines.addStation("Jurong East", StationCode.of("NS1"));
        stationLines.addStation("Yew Tee", StationCode.of("NS5"));
        stationLines.addStation("Kranji", StationCode.of("NS7"));
    }

    @Test
    void constructRouteFromSolution_simpleLine() {
        List<String> path = Arrays.asList("Redhill", "Tiong Bahru");
        Mockito.when(solution.getPath()).thenReturn(path);
        Route route = routeConstructor.constructRouteFromSolution(solution, stationLines);
        List<String> routeSequence = route.getSequence();
        Assertions.assertThat(routeSequence.size()).isEqualTo(2);
        Assertions.assertThat(routeSequence.get(0)).isEqualTo("EW18");
        Assertions.assertThat(routeSequence.get(1)).isEqualTo("EW17");
    }

    @Test
    void constructRouteFromSolution_startAtInterchangeNoTransfer() {
        List<String> path = Arrays.asList("Jurong East", "Redhill", "Tiong Bahru");
        Mockito.when(solution.getPath()).thenReturn(path);
        Route route = routeConstructor.constructRouteFromSolution(solution, stationLines);
        List<String> routeSequence = route.getSequence();
        Assertions.assertThat(routeSequence.size()).isEqualTo(3);
        Assertions.assertThat(routeSequence.get(0)).isEqualTo("EW24");
        Assertions.assertThat(routeSequence.get(1)).isEqualTo("EW18");
        Assertions.assertThat(routeSequence.get(2)).isEqualTo("EW17");
    }

    @Test
    void constructRouteFromSolution_endAtInterchangeNoTransfer() {
        List<String> path = Arrays.asList("Kranji", "Yew Tee", "Jurong East");
        Mockito.when(solution.getPath()).thenReturn(path);
        Route route = routeConstructor.constructRouteFromSolution(solution, stationLines);
        List<String> routeSequence = route.getSequence();
        Assertions.assertThat(routeSequence.size()).isEqualTo(3);
        Assertions.assertThat(routeSequence.get(0)).isEqualTo("NS7");
        Assertions.assertThat(routeSequence.get(1)).isEqualTo("NS5");
        Assertions.assertThat(routeSequence.get(2)).isEqualTo("NS1");
    }

    @Test
    void constructRouteFromSolution_withSingleTransfer() {
        List<String> path = Arrays.asList("Kranji", "Yew Tee", "Jurong East", "Redhill", "Tiong Bahru");
        Mockito.when(solution.getPath()).thenReturn(path);
        Route route = routeConstructor.constructRouteFromSolution(solution, stationLines);
        List<String> routeSequence = route.getSequence();
        Assertions.assertThat(routeSequence.size()).isEqualTo(6);
        Assertions.assertThat(routeSequence.get(0)).isEqualTo("NS7");
        Assertions.assertThat(routeSequence.get(1)).isEqualTo("NS5");
        Assertions.assertThat(routeSequence.get(2)).isEqualTo("NS1");
        Assertions.assertThat(routeSequence.get(3)).isEqualTo("EW24");
        Assertions.assertThat(routeSequence.get(4)).isEqualTo("EW18");
        Assertions.assertThat(routeSequence.get(5)).isEqualTo("EW17");
    }
}