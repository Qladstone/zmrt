package com.zendesk.zmrt.routesearch;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RouteSearchGraph {

    private final Map<String, Vertex> vertices = new HashMap<>();

    public boolean contains(String vertexName) {
        return vertices.containsKey(vertexName);
    }

    public void addVertex(String vertexName) {
        if (!vertices.containsKey(vertexName)) {
            Vertex vertex = new Vertex(vertexName);
            vertices.put(vertexName, vertex);
        }
    }

    public void addEdge(String vertexNameI, String vertexNameJ) {
        if (!vertices.containsKey(vertexNameI)) {
            throw new IllegalArgumentException(
                    "The route search graph does not contain vertex with name=" + vertexNameI);
        } else if (!vertices.containsKey(vertexNameJ)) {
            throw new IllegalArgumentException(
                    "The route search graph does not contain vertex with name=" + vertexNameJ);
        } else if (vertexNameI.equals(vertexNameJ)) {
            throw new IllegalArgumentException(
                    "Cannot add an edge between a vertex and itself; vertex name=" + vertexNameJ);
        }
        Vertex vertexI = vertices.get(vertexNameI);
        Vertex vertexJ = vertices.get(vertexNameJ);
        vertexI.neighbours.add(vertexJ);
        vertexJ.neighbours.add(vertexI);
    }

    private static class Vertex {
        private final String name; // vertex/station name
        private final Set<Vertex> neighbours = new HashSet<>();

        private Vertex(String name) {
            this.name = name;
        }
    }
}
