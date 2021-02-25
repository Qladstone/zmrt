package com.zendesk.zmrt.routesearch;

import lombok.Getter;

import java.util.*;

public class RouteSearchGraph {

    private final Map<String, Vertex> vertices = new HashMap<>();
    private final Vertex sentinel = new Vertex(null);

    public List<Solution> goalSearch(String startVertexName, String goalVertexName) {
        List<Solution> solutions = new ArrayList<>();
        if (!vertices.containsKey(startVertexName) || !vertices.containsKey(goalVertexName)) {
            return solutions;
        }
        breadthFirstGoalSearch(startVertexName, goalVertexName).ifPresent(solutions::add);
        return solutions;
    }

    public Optional<Solution> breadthFirstGoalSearch(String startVertexName, String goalVertexName) {
        if (!vertices.containsKey(startVertexName) || !vertices.containsKey(goalVertexName)) {
            return Optional.empty();
        }
        Deque<Vertex> queue = new ArrayDeque<>();
        Vertex start = vertices.get(startVertexName);
        Vertex goal = vertices.get(goalVertexName);
        start.pred = sentinel;
        queue.add(start);
        while (!queue.isEmpty()) {
            Vertex current = queue.remove();
            if (current == goal) {
                return Optional.of(buildSolutionFromGoalVertex(goal));
            }
            for (Vertex neighbour : current.neighbours) {
                if (neighbour.pred != null) continue; // skip vertices that have already been visited
                neighbour.pred = current;
                queue.add(neighbour);
            }
        }
        return Optional.empty();
    }

    private Solution buildSolutionFromGoalVertex(Vertex goal) {
        Solution solution = new Solution();
        Deque<String> stack = new ArrayDeque<>(); // for reversing the order
        Vertex current = goal;
        while (current != sentinel) {
            stack.push(current.name);
            current = current.pred;
        }
        solution.path.addAll(stack);
        return solution;
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

        private Vertex pred = null; // predecessor on the path

        private Vertex(String name) {
            this.name = name;
        }
    }
}
