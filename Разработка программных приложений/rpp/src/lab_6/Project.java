package lab_6;

import java.util.Arrays;

public class Project {
    private static final int V = 11;

    public static void main(String[] args) {
        int[] sizes = {6, 1500};

        for (int N : sizes) {
            System.out.println("\nРешение для N = " + N);

            double[][] A = generateMatrixA(N);
            double[] b = generateVectorB(N);

            if (N == 6) {
                System.out.println("\nИсходная матрица A:");
                printMatrix(A);
                System.out.println("\nВектор b:");
                printVector(b);
            }

            long startTime = System.currentTimeMillis();

            double[][] A_copy = copyMatrix(A);
            double[] b_copy = Arrays.copyOf(b, b.length);

            int[] pivotHistory = new int[N];
            double det = gaussianEliminationWithPivoting(A_copy, b_copy, pivotHistory);

            double[] x = backSubstitution(A_copy, b_copy);

            long endTime = System.currentTimeMillis();
            double runtime = (endTime - startTime) / 1000.0;

            // Вычисление невязки
            double[] residual = calculateResidual(A, b, x);
            double residualNorm = vectorNorm(residual);

            if (N == 6) {
                System.out.println("\nВерхняя треугольная матрица A':");
                printMatrix(A_copy);
                System.out.println("\nПреобразованный вектор правой части:");
                printVector(b_copy);
                System.out.println("\nВектор решения X:");
                printVector(x);
            } else {
                System.out.println("X901 = " + x[900]);
                System.out.println("X902 = " + x[901]);
                System.out.println("X903 = " + x[902]);
            }

            System.out.println("\nВремя работы: " + runtime + " сек");
            System.out.println("Норма вектора невязки: " + residualNorm);
            System.out.println("Определитель матрицы: " + det);
        }
    }

    private static double[][] generateMatrixA(int N) {
        double[][] A = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                A[i][j] = V/10.0 + 1.1 * (j - i) - Math.cos(i - j);
            }
            A[i][i] += 1;
        }
        return A;
    }

    private static double[] generateVectorB(int N) {
        double[] b = new double[N];
        for (int j = 0; j < N; j++) {
            b[j] = 1.0/V - Math.sin((j+1) * V);
        }
        return b;
    }

    private static double gaussianEliminationWithPivoting(double[][] A, double[] b, int[] pivotHistory) {
        int N = A.length;
        double det = 1.0;

        for (int k = 0; k < N; k++) {
            // Поиск ведущего элемента в столбце k
            int maxRow = k;
            double maxVal = Math.abs(A[k][k]);

            for (int i = k + 1; i < N; i++) {
                if (Math.abs(A[i][k]) > maxVal) {
                    maxVal = Math.abs(A[i][k]);
                    maxRow = i;
                }
            }

            pivotHistory[k] = maxRow;

            // Перестановка строк, если необходимо
            if (maxRow != k) {
                swapRows(A, k, maxRow);
                swapElements(b, k, maxRow);
                det *= -1; // При перестановке строк определитель меняет знак
            }

            // Проверка на вырожденность
            if (Math.abs(A[k][k]) < 1e-10) {
                throw new RuntimeException("Матрица вырождена");
            }

            // Прямой ход метода Гаусса
            for (int i = k + 1; i < N; i++) {
                double factor = A[i][k] / A[k][k];
                b[i] -= factor * b[k];

                for (int j = k; j < N; j++) {
                    A[i][j] -= factor * A[k][j];
                }
            }

            det *= A[k][k]; // Определитель равен произведению диагональных элементов
        }

        return det;
    }

    // Обратная подстановка
    private static double[] backSubstitution(double[][] A, double[] b) {
        int N = A.length;
        double[] x = new double[N];

        for (int i = N - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < N; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
        }

        return x;
    }

    // Вычисление невязки
    private static double[] calculateResidual(double[][] A, double[] b, double[] x) {
        int N = A.length;
        double[] residual = new double[N];

        for (int i = 0; i < N; i++) {
            double sum = 0.0;
            for (int j = 0; j < N; j++) {
                sum += A[i][j] * x[j];
            }
            residual[i] = b[i] - sum;
        }

        return residual;
    }

    // Норма вектора (евклидова)
    private static double vectorNorm(double[] v) {
        double sum = 0.0;
        for (double val : v) {
            sum += val * val;
        }
        return Math.sqrt(sum);
    }

    // Вспомогательные методы
    private static double[][] copyMatrix(double[][] original) {
        double[][] copy = new double[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }

    private static void swapRows(double[][] A, int i, int j) {
        double[] temp = A[i];
        A[i] = A[j];
        A[j] = temp;
    }

    private static void swapElements(double[] b, int i, int j) {
        double temp = b[i];
        b[i] = b[j];
        b[j] = temp;
    }

    private static void printMatrix(double[][] A) {
        for (double[] row : A) {
            for (double val : row) {
                System.out.printf("%10.6f ", val);
            }
            System.out.println();
        }
    }

    private static void printVector(double[] v) {
        for (double val : v) {
            System.out.printf("%10.16f\n", val);
        }
    }
}
