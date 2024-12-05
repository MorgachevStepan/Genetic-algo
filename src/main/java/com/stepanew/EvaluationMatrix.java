package com.stepanew;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.HashMap;
import java.util.Map;

public class EvaluationMatrix {

    private final int[][] matrix;
    private final Map<String, Integer> vertexIndexMap;

    /**
     * Конструктор для вычисления оценочной матрицы.
     *
     * @param graph граф, для которого вычисляется оценочная матрица
     */
    public EvaluationMatrix(Graph<String, DefaultEdge> graph) {
        int size = graph.vertexSet().size();
        matrix = new int[size][size];
        vertexIndexMap = new HashMap<>();
        calculateMatrix(graph);
    }

    /**
     * Метод для вычисления оценочной матрицы.
     *
     * @param graph граф, для которого строится матрица
     */
    private void calculateMatrix(Graph<String, DefaultEdge> graph) {
        String[] vertices = graph.vertexSet().toArray(new String[0]);
        int n = vertices.length;

        // Создаем отображение идентификаторов вершин в их индексы
        for (int i = 0; i < n; i++) {
            vertexIndexMap.put(vertices[i], i);
        }

        for (int i = 0; i < n; i++) {
            String vertexI = vertices[i];
            for (int j = 0; j < n; j++) {
                String vertexJ = vertices[j];
                if (i == j) {
                    matrix[i][j] = 0;
                } else {
                    int stepI = graph.edgesOf(vertexI).size(); // Локальная степень вершины i
                    int stepJ = graph.edgesOf(vertexJ).size(); // Локальная степень вершины j
                    int sv = graph.containsEdge(vertexI, vertexJ) || graph.containsEdge(vertexJ, vertexI) ? 0 : (int) (0.3 * n);

                    matrix[i][j] = stepI + stepJ + sv;
                }
            }
        }
    }

    /**
     * Получить значение оценочной матрицы для двух вершин по их индексам.
     *
     * @param i индекс первой вершины
     * @param j индекс второй вершины
     * @return значение оценочной матрицы для двух вершин
     */
    public int getValue(int i, int j) {
        return matrix[i][j];
    }

    /**
     * Получить значение оценочной матрицы для двух вершин по их идентификаторам.
     *
     * @param vertex1 идентификатор первой вершины
     * @param vertex2 идентификатор второй вершины
     * @return значение оценочной матрицы для двух вершин
     */
    public int getValue(String vertex1, String vertex2) {
        Integer index1 = vertexIndexMap.get(vertex1);
        Integer index2 = vertexIndexMap.get(vertex2);

        if (index1 == null || index2 == null) {
            throw new IllegalArgumentException("Invalid vertex identifier");
        }

        return matrix[index1][index2];
    }

    /**
     * Вывести матрицу на консоль.
     */
    public void printMatrix() {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.printf("%5d", value);
            }
            System.out.println();
        }
    }

}
