package com.zendesk.zmrt.routesearch;

import lombok.Getter;

import java.util.*;

public class RouteSearchGraph {

    private final Map<String, Vertex> vertices = new HashMap<>();

    public List<Solution> goalSearch(String startVertexName, String goalVertexName) {
        List<Solution> solutions = new ArrayList<>();
        if (!vertices.containsKey(startVertexName) || !vertices.containsKey(goalVertexName)) {
            return solutions;
        }
        Vertex start = vertices.get(startVertexName);
        Vertex goal = vertices.get(goalVertexName);
        // dummy algorithm
        if (start.neighbours.contains(goal)) {
            Solution solution = new Solution();
            solution.path.add(startVertexName);
            solution.path.add(goalVertexName);
            solutions.add(solution);
        }
        return solutions;
    }

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

    @Getter
    public static class Solution {
        private final List<String> path = new ArrayList<>();
    }

    private static class Vertex {
        private final String name; // vertex/station name
        private final Set<Vertex> neighbours = new HashSet<>();

        private Vertex(String name) {
            this.name = name;
        }
    }
}
