package com.stepanew;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class GraphLoader {

    private String yamlFile;

    public GraphLoader(String yamlFile) {
        this.yamlFile = yamlFile;
    }

    /**
     * Загружает граф из YAML-файла.
     *
     * @return граф
     */
    public Graph<String, DefaultEdge> loadGraph() {
        Yaml yaml = new Yaml();
        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream(yamlFile);

        if (inputStream == null) {
            throw new RuntimeException("Не удалось найти файл " + yamlFile);
        }

        Map<String, Object> obj = yaml.load(inputStream);
        Map<String, Object> graphData = (Map<String, Object>) obj.get("graph");
        List<String> vertices = (List<String>) graphData.get("vertices");
        List<List<String>> edges = (List<List<String>>) graphData.get("edges");

        Graph<String, DefaultEdge> graph = getStringDefaultEdgeGraph(vertices, edges);

        return graph;
    }

    private static Graph<String, DefaultEdge> getStringDefaultEdgeGraph(List<String> vertices, List<List<String>> edges) {
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        for (String vertex : vertices) {
            graph.addVertex(vertex);
        }
        
        for (List<String> edge : edges) {
            if (edge.size() != 2) {
                throw new RuntimeException("Неверный формат ребра: " + edge);
            }
            String source = edge.get(0);
            String target = edge.get(1);
            graph.addEdge(source, target);
        }
        return graph;
    }
}
