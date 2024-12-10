package com.stepanew.graphs;

import org.jgrapht.Graph;

@FunctionalInterface
public interface GraphBuilder<V, E> {

    /**
     * Строит граф, добавляя вершины и рёбра.
     *
     * @param graph граф, который нужно построить
     */
    void buildGraph(Graph<V, E> graph);

}
