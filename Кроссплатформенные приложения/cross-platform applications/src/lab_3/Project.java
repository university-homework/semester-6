package lab_3;

import java.util.Scanner;

public class Project {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[][] adjacencyMatrix = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjacencyMatrix[i][j] = scanner.nextInt();
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(adjacencyMatrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();

        // Вычисление полустепеней захода и исхода
        for (int vertex = 0; vertex < n; vertex++) {
            int inDegree = 0;
            int outDegree = 0;

            // Полустепень захода - количество входящих рёбер (столбец)
            for (int i = 0; i < n; i++) {
                if (adjacencyMatrix[i][vertex] == 1) {
                    inDegree++;
                }
            }

            // Полустепень исхода - количество исходящих рёбер (строка)
            for (int j = 0; j < n; j++) {
                if (adjacencyMatrix[vertex][j] == 1) {
                    outDegree++;
                }
            }

            System.out.println(inDegree + " " + outDegree);
        }
        System.out.println();

        int[][] startMatrix = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    startMatrix[i][j] = 0;
                }
                else {
                    startMatrix[i][j] = 1;
                }
            }
        }

        for (int i = 1; i < n; i++) {
            int[][] adjacencyMatrixInPower = matrixPower(adjacencyMatrix, i);
            startMatrix = addMatrices(startMatrix, adjacencyMatrixInPower);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(startMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static int[][] addMatrices(int[][] a, int[][] b) {
        int rows = a.length;
        int cols = a[0].length;
        int[][] result = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }
        return result;
    }

    public static int[][] matrixPower(int[][] matrix, int power) {
        int n = matrix.length;
        int[][] result = new int[n][n];

        // Инициализация единичной матрицы
        for (int i = 0; i < n; i++) {
            result[i][i] = 1;
        }

        while (power > 0) {
            if (power % 2 == 1) {
                result = multiplyMatrices(result, matrix);
            }
            matrix = multiplyMatrices(matrix, matrix);
            power /= 2;
        }
        return result;
    }

    public static int[][] multiplyMatrices(int[][] a, int[][] b) {
        int aRows = a.length;
        int aCols = a[0].length;
        int bCols = b[0].length;
        int[][] result = new int[aRows][bCols];

        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bCols; j++) {
                for (int k = 0; k < aCols; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }
}
