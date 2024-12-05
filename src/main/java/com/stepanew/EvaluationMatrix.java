package com.stepanew;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class EvaluationMatrix {

    private final int[][] matrix;

    /**
     * Конструктор для вычисления оценочной матрицы.
     *
     * @param graph граф, для которого вычисляется оценочная матрица
     */
    public EvaluationMatrix(Graph<String, DefaultEdge> graph) {
        int size = graph.vertexSet().size();
        matrix = new int[size][size];
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

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    matrix[i][j] = 0;
                } else {
                    int stepI = graph.edgesOf(vertices[i]).size(); // Локальная степень вершины i
                    int stepJ = graph.edgesOf(vertices[j]).size(); // Локальная степень вершины j
                    int sv = graph.containsEdge(vertices[i], vertices[j]) ? 0 : (int) (0.3 * n);

                    matrix[i][j] = stepI + stepJ + sv;
                }
            }
        }
    }

    /**
     * Получить значение оценочной матрицы для двух вершин.
     *
     * @param i индекс первой вершины
     * @param j индекс второй вершины
     * @return значение оценочной матрицы для двух вершин
     */
    public int getValue(int i, int j) {
        return matrix[i][j];
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
