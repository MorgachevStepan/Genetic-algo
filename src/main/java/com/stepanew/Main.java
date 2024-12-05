package com.stepanew;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public class Main {

    private final static String GRAPH_PNG = "src/main/resources/graph";
    private final static String GRAPH_YAML = "graph.yaml";

    public static void main(String... args) {
        // Загружаем граф из YAML-файла
        GraphLoader graphLoader = new GraphLoader(GRAPH_YAML);
        Graph<String, DefaultEdge> graph = graphLoader.loadGraph();

        // Создаём объект Render
        Render render = new Render(GRAPH_PNG);

        // Визуализируем исходный граф
        render.renderGraph(graph, "_before");

        // Далее ваш код генетического алгоритма
        // Создание оценочной матрицы
        EvaluationMatrix evalMatrix = new EvaluationMatrix(graph);

        // Создание начальной популяции
        List<String> vertices = new ArrayList<>(graph.vertexSet());
        List<Chromosome> population = new ArrayList<>();

        int populationSize = 100;
        int generations = 500;

        for (int i = 0; i < populationSize; i++) {
            Chromosome chromosome = Chromosome.createRandomChromosome(vertices);
            chromosome.evaluateFitness(graph);
            population.add(chromosome);
        }

        // Генетический алгоритм
        for (int generation = 0; generation < generations; generation++) {
            // Селекция
            population.sort(Comparator.comparingDouble(Chromosome::getFitness));

            List<Chromosome> newPopulation = new ArrayList<>();

            Random rand = new Random();

            // Кроссинговер и мутация
            while (newPopulation.size() < populationSize) {
                Chromosome parent1 = population.get(rand.nextInt(populationSize / 2));
                Chromosome parent2 = population.get(rand.nextInt(populationSize / 2));

                Chromosome offspring = Chromosome.crossover(parent1, parent2, evalMatrix);

                // Мутация с вероятностью 5%
                if (rand.nextDouble() < 0.05) {
                    offspring.mutate();
                }

                offspring.evaluateFitness(graph);
                newPopulation.add(offspring);
            }

            population = newPopulation;

            // Выводим лучшую хромосому каждого 50-го поколения
            if (generation % 50 == 0) {
                Chromosome bestChromosome = population.stream()
                        .min(Comparator.comparingDouble(Chromosome::getFitness))
                        .orElse(null);

                System.out.println("Поколение " + generation + ": Лучшая приспособленность = " + bestChromosome.getFitness()
                        + ", Количество цветов = " + bestChromosome.getNumberOfColors());
            }
        }

        // Выводим результат
        Chromosome bestChromosome = population.stream()
                .min(Comparator.comparingDouble(Chromosome::getFitness))
                .orElse(null);

        System.out.println("Лучшее решение: " + bestChromosome);
        System.out.println("Количество использованных цветов: " + bestChromosome.getNumberOfColors());

        // Визуализация графа с раскраской
        render.renderGraph(graph, bestChromosome.getVertexColors(), "_after");
    }
}


