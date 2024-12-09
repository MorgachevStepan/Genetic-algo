package com.stepanew;

import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
public class GraphColoringCalculator {

    private final static String GRAPH_PNG = "src/main/resources/graph";

    private final static String BEFORE_SUFFIX = "_before";

    private final static String AFTER_SUFFIX = "_after";

    private final static Short POPULATION_SIZE = 100;

    private final static Short GENERATION_SIZE = 500;

    private final static Double MUTATION_RATE = 0.05;

    private final static Byte RATE_LOG = 50;

    private EvaluationMatrix evaluationMatrix;

    private List<String> vertices;

    private List<Chromosome> population;

    private final Render render = new Render(GRAPH_PNG);

    public void calculateGraphColoring(final Graph<String, DefaultEdge> graph) {
        preparingPhase(graph);
        calculateGenerationAlgo(graph);
        lastPhase(graph);
    }

    private void lastPhase(Graph<String, DefaultEdge> graph) {
        Chromosome bestChromosome = population.stream()
                .min(Comparator.comparingDouble(Chromosome::getFitness))
                .orElse(null);

        logCurrentGlobalResult(bestChromosome);

        renderGraph(graph, bestChromosome.getVertexColors(), AFTER_SUFFIX);
    }

    private void renderGraph(Graph<String, DefaultEdge> graph, Map<String, Integer> vertexColors, String afterSuffix) {
        render.renderGraph(graph, vertexColors, AFTER_SUFFIX);
    }

    private void renderGraph(Graph<String, DefaultEdge> graph, String suffix) {
        render.renderGraph(graph, suffix);
    }

    private void preparingPhase(Graph<String, DefaultEdge> graph) {
        renderGraph(graph, BEFORE_SUFFIX);
        evaluationMatrix = new EvaluationMatrix(graph);
        vertices = new ArrayList<>(graph.vertexSet());
        population = new ArrayList<>();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            Chromosome chromosome = Chromosome.createRandomChromosome(vertices);
            chromosome.evaluateFitness(graph);
            population.add(chromosome);
        }
    }

    private void calculateGenerationAlgo(Graph<String, DefaultEdge> graph) {
        for (int generation = 0; generation < GENERATION_SIZE; generation++) {
            // Селекция
            population.sort(Comparator.comparingDouble(Chromosome::getFitness));

            List<Chromosome> newPopulation = new ArrayList<>();

            Random rand = new Random();

            // Кроссинговер и мутация
            while (newPopulation.size() < POPULATION_SIZE) {
                Chromosome parent1 = population.get(rand.nextInt(POPULATION_SIZE / 2));
                Chromosome parent2 = population.get(rand.nextInt(POPULATION_SIZE / 2));

                Chromosome offspring = Chromosome.crossover(parent1, parent2, evaluationMatrix);

                // Мутация
                if (rand.nextDouble() < MUTATION_RATE) {
                    offspring.mutate();
                }

                offspring.evaluateFitness(graph);
                newPopulation.add(offspring);
            }

            population = newPopulation;

            logCurrentGenResult(generation);
        }
    }

    /**
     * Логгирование результата алгоритма для текущего поколения
     */
    private void logCurrentGenResult(int generation) {
        if (generation % RATE_LOG == 0) {
            Chromosome bestChromosome = population
                    .stream()
                    .min(Comparator.comparingDouble(Chromosome::getFitness))
                    .orElse(null);

            if (bestChromosome != null) {
                log.info(
                        AnsiColors
                                .color(
                                        String.format(
                                                "Поколение %d: Лучшая приспособленность = %.2f, Количество цветов = %d",
                                                generation,
                                                bestChromosome.getFitness(),
                                                bestChromosome.getNumberOfColors()
                                        ),
                                        AnsiColors.GREEN
                                )
                );
            } else {
                log.warn(
                        AnsiColors.color(
                                String.format(
                                        "Поколение %d: Лучшая хромосома не найдена.",
                                        generation),
                                AnsiColors.RED
                        )
                );
            }
        }
    }

    private void logCurrentGlobalResult(Chromosome bestChromosome) {
        if (bestChromosome != null) {
            log.info(
                    AnsiColors
                            .color(
                                    String.format(
                                            "Лучшее решение: %s",
                                            bestChromosome
                                    ), AnsiColors.GREEN)
            );
            log.info(
                    AnsiColors
                            .color(
                                    String.format(
                                            "Количество использованных цветов: %d",
                                            bestChromosome.getNumberOfColors()
                                    ), AnsiColors.GREEN)
            );
        } else {
            log.warn(AnsiColors.color("Лучшее решение не найдено.", AnsiColors.RED));
        }
    }

}
