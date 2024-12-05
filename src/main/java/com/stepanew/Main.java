package com.stepanew;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Main {

    private final static String GRAPH_PNG = "src/main/resources/graph.png";

    public static void main(String... args) {
        Graph<String, DefaultEdge> graph = getStringDefaultEdgeGraph();

        System.out.println("Вершины: " + graph.vertexSet());
        System.out.println("Рёбра: " + graph.edgeSet());

        // Создание оценочной матрицы
        EvaluationMatrix evalMatrix = new EvaluationMatrix(graph);

        // Создание начальной популяции
        List<String> vertices = new ArrayList<>(graph.vertexSet());
        List<Chromosome> population = new ArrayList<>();

        int populationSize = 50;
        int generations = 100;

        for (int i = 0; i < populationSize; i++) {
            Chromosome chromosome = Chromosome.createRandomChromosome(vertices);
            chromosome.evaluateFitness(graph);
            population.add(chromosome);
        }

        // Генетический алгоритм
        for (int generation = 0; generation < generations; generation++) {
            // Селекция (например, турнирная)
            population.sort(Comparator.comparingDouble(Chromosome::getFitness));

            List<Chromosome> newPopulation = new ArrayList<>();

            // Кроссинговер и мутация
            while (newPopulation.size() < populationSize) {
                Chromosome parent1 = population.get(new Random().nextInt(populationSize / 2));
                Chromosome parent2 = population.get(new Random().nextInt(populationSize / 2));

                Chromosome offspring = Chromosome.crossover(parent1, parent2, evalMatrix);

                // Мутация с определенной вероятностью
                if (new Random().nextDouble() < 0.1) {
                    offspring.mutate();
                }

                offspring.evaluateFitness(graph);
                newPopulation.add(offspring);
            }

            population = newPopulation;

            // Выводим лучшую хромосому каждого поколения
            Chromosome bestChromosome = population.stream()
                    .min(Comparator.comparingDouble(Chromosome::getFitness))
                    .orElse(null);

            System.out.println("Поколение " + generation + ": Лучшая приспособленность = " + bestChromosome.getFitness());
        }

        // Выводим результат
        Chromosome bestChromosome = population.stream()
                .min(Comparator.comparingDouble(Chromosome::getFitness))
                .orElse(null);

        System.out.println("Лучшее решение: " + bestChromosome);

        System.out.println("Количество использованных цветов: " + bestChromosome.getNumberOfColors());
    }

    private static Graph<String, DefaultEdge> getStringDefaultEdgeGraph() {
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        Render render = new Render(GRAPH_PNG);

        GraphBuilder<String, DefaultEdge> builder = g -> {
            g.addVertex("A");
            g.addVertex("B");
            g.addVertex("C");
            g.addVertex("D");
            g.addVertex("E");
            g.addVertex("F");
            // Добавьте больше вершин и рёбер по необходимости
            g.addEdge("A", "B");
            g.addEdge("A", "C");
            g.addEdge("B", "C");
            g.addEdge("B", "D");
            g.addEdge("C", "E");
            g.addEdge("D", "E");
            g.addEdge("E", "F");
            g.addEdge("D", "F");
        };

        builder.buildGraph(graph);
        render.renderGraph(graph);
        return graph;
    }

}

