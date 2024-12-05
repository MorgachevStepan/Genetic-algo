package com.stepanew;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Main {

    private final static String GRAPH_PNG = "src/main/resources/graph.png";

    public static void main(String... args) {
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        Render render = new Render(GRAPH_PNG);

        GraphBuilder<String, DefaultEdge> builder = g -> {
            g.addVertex("A");
            g.addVertex("B");
            g.addVertex("C");
            g.addEdge("A", "B");
            g.addEdge("B", "C");
        };

        builder.buildGraph(graph);
        render.renderGraph(graph);

        System.out.println("Вершины: " + graph.vertexSet());
        System.out.println("Рёбра: " + graph.edgeSet());

        EvaluationMatrix evaluationMatrix = new EvaluationMatrix(graph);
        System.out.println("Оценочная матрица:");
        evaluationMatrix.printMatrix();
    }

}
