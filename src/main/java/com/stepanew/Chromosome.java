package com.stepanew;

import lombok.Data;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Data
public class Chromosome {

    private List<String> genes; // Список номеров вершин
    private double fitness;     // Значение целевой функции
    private int numberOfColors; // Количество использованных цветов
    private Map<String, Integer> vertexColors; // Карта соответствия вершины и цвета

    /**
     * Конструктор для создания хромосомы с заданными генами.
     *
     * @param genes список номеров вершин
     */
    public Chromosome(List<String> genes) {
        this.genes = new ArrayList<>(genes);
        this.fitness = 0.0;
    }

    /**
     * Создание случайной хромосомы на основе списка вершин графа.
     *
     * @param vertices список вершин графа
     * @return случайная хромосома
     */
    public static Chromosome createRandomChromosome(List<String> vertices) {
        List<String> genes = new ArrayList<>(vertices);
        Collections.shuffle(genes, new Random());
        return new Chromosome(genes);
    }

    /**
     * Оператор мутации: случайно переставляет два гена.
     */
    public void mutate() {
        Random rand = new Random();
        int index1 = rand.nextInt(genes.size());
        int index2 = rand.nextInt(genes.size());
        Collections.swap(genes, index1, index2);
    }

    /**
     * Оператор кроссинговера: создает потомка на основе двух родительских хромосом.
     *
     * @param parent1 первая родительская хромосома
     * @param parent2 вторая родительская хромосома
     * @param evalMatrix оценочная матрица
     * @return потомок
     */
    public static Chromosome crossover(Chromosome parent1, Chromosome parent2, EvaluationMatrix evalMatrix) {
        List<String> offspringGenes = new ArrayList<>();
        Random rand = new Random();
        int chromosomeLength = parent1.getGenes().size();
        int crossoverPoint = rand.nextInt(chromosomeLength - 1) + 1; // Точка скрещивания от 1 до length - 1

        // Копируем гены до точки кроссинговера из первого родителя
        for (int i = 0; i < crossoverPoint; i++) {
            offspringGenes.add(parent1.getGenes().get(i));
        }

        // Добавляем оставшиеся гены на основе оценочной матрицы
        String lastGene = offspringGenes.get(offspringGenes.size() - 1);
        for (int i = crossoverPoint; i < chromosomeLength; i++) {
            String gene1 = parent1.getGenes().get(i);
            String gene2 = parent2.getGenes().get(i);

            // Проверяем, были ли уже добавлены эти гены
            boolean gene1Added = offspringGenes.contains(gene1);
            boolean gene2Added = offspringGenes.contains(gene2);

            // Если оба гена уже добавлены, пропускаем итерацию
            if (gene1Added && gene2Added) {
                continue;
            }

            int value1 = gene1Added ? Integer.MIN_VALUE : evalMatrix.getValue(lastGene, gene1);
            int value2 = gene2Added ? Integer.MIN_VALUE : evalMatrix.getValue(lastGene, gene2);

            if (value1 > value2) {
                offspringGenes.add(gene1);
                lastGene = gene1;
            } else {
                offspringGenes.add(gene2);
                lastGene = gene2;
            }
        }

        // Добавляем оставшиеся гены, которые не были включены
        for (String gene : parent1.getGenes()) {
            if (!offspringGenes.contains(gene)) {
                offspringGenes.add(gene);
            }
        }

        return new Chromosome(offspringGenes);
    }

    /**
     * Раскрашивает граф на основе порядка вершин в хромосоме и вычисляет приспособленность.
     *
     * @param graph граф, который нужно раскрасить
     */
    public void evaluateFitness(Graph<String, DefaultEdge> graph) {
        vertexColors = new HashMap<>();
        int currentColor = 0;
        Set<String> coloredVertices = new HashSet<>();

        for (String gene : genes) {
            boolean assigned = false;

            // Попробуем присвоить один из уже использованных цветов
            for (int color = 0; color <= currentColor; color++) {
                boolean canUseColor = true;
                for (String vertex : coloredVertices) {
                    if (vertexColors.get(vertex) == color && graph.containsEdge(gene, vertex)) {
                        canUseColor = false;
                        break;
                    }
                }
                if (canUseColor) {
                    vertexColors.put(gene, color);
                    assigned = true;
                    break;
                }
            }

            // Если не удалось присвоить существующий цвет, используем новый
            if (!assigned) {
                currentColor++;
                vertexColors.put(gene, currentColor);
            }

            coloredVertices.add(gene);
        }

        // Количество использованных цветов
        numberOfColors = currentColor + 1;

        // Целевая функция: отношение количества цветов к количеству вершин
        this.fitness = (double) numberOfColors / genes.size();
    }

}
