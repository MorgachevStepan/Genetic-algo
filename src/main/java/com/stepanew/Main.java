package com.stepanew;

import com.stepanew.algorithm.GraphColoringCalculator;
import com.stepanew.graphs.GraphLoader;
import com.stepanew.utils.Config;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class Main {

    private final static String CONFIG_FILE = "application.yaml";

    public static void main(String... args) {
        Config config = Config.load(CONFIG_FILE);

        String graphYaml = config.getGraph().getYaml();
        GraphLoader graphLoader = new GraphLoader(graphYaml);
        Graph<String, DefaultEdge> graph = graphLoader.loadGraph();

        GraphColoringCalculator graphColoringCalculator = new GraphColoringCalculator(config);
        graphColoringCalculator.calculateGraphColoring(graph);
    }

}
