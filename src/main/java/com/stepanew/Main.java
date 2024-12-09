package com.stepanew;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class Main {

    private final static String GRAPH_YAML = "graph.yaml";

    public static void main(String... args) {
        GraphLoader graphLoader = new GraphLoader(GRAPH_YAML);
        Graph<String, DefaultEdge> graph = graphLoader.loadGraph();

        GraphColoringCalculator graphColoringCalculator = new GraphColoringCalculator();
        graphColoringCalculator.calculateGraphColoring(graph);
    }
}


