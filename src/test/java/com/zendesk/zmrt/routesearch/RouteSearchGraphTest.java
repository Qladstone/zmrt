package com.zendesk.zmrt.routesearch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.zendesk.zmrt.routesearch.RouteSearchGraph.Solution;
import static org.assertj.core.api.Assertions.assertThat;

class RouteSearchGraphTest {

    private RouteSearchGraph routeSearchGraph;

    @BeforeEach
    void setUp() {
        /*
        Build the following graph:
        A - B - C - D - E
        |       |       |
        F - G - H - I - J

        K (isolated vertex)
         */
        routeSearchGraph = new RouteSearchGraph();

        List<String> vertexNames = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K");
        for (String name : vertexNames) {
            routeSearchGraph.addVertex(name);
        }

        List<SimpleImmutableEntry<String, String>> edgeNames = Arrays.asList(
                new SimpleImmutableEntry<>("A", "B"),
                new SimpleImmutableEntry<>("B", "C"),
                new SimpleImmutableEntry<>("C", "D"),
                new SimpleImmutableEntry<>("D", "E"),

                new SimpleImmutableEntry<>("A", "F"),
                new SimpleImmutableEntry<>("C", "H"),
                new SimpleImmutableEntry<>("E", "J"),

                new SimpleImmutableEntry<>("F", "G"),
                new SimpleImmutableEntry<>("G", "H"),
                new SimpleImmutableEntry<>("H", "I"),
                new SimpleImmutableEntry<>("I", "J")
        );
        for (SimpleImmutableEntry<String, String> edgeName : edgeNames) {
            routeSearchGraph.addEdge(edgeName.getKey(), edgeName.getValue());
        }
    }

    @Test
    void breadthFirstGoalSearch_AF() {
        Optional<Solution> solution = routeSearchGraph.breadthFirstGoalSearch("A", "F");
        assertThat(solution.isPresent()).isTrue();
        List<String> path = solution.get().getPath();
        assertThat(path.size()).isEqualTo(2);
        assertThat(path.get(0)).isEqualTo("A");
        assertThat(path.get(1)).isEqualTo("F");
    }

    @Test
    void breadthFirstGoalSearch_GK() {
        Optional<Solution> solution = routeSearchGraph.breadthFirstGoalSearch("G", "K");
        assertThat(solution.isPresent()).isFalse();
    }

    @Test
    void breadthFirstGoalSearch_JB() {
        Optional<Solution> solution = routeSearchGraph.breadthFirstGoalSearch("J", "B");
        assertThat(solution.isPresent()).isTrue();
        List<String> path = solution.get().getPath();
        assertThat(path.size()).isEqualTo(5);
        assertThat(path.get(0)).isEqualTo("J");
        assertThat(path.get(1)).isIn("E", "I");
        assertThat(path.get(3)).isEqualTo("C");
        assertThat(path.get(4)).isEqualTo("B");
    }
}