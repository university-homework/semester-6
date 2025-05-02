package lab_8;

import java.util.Arrays;


public class DoubleSolve {
    private static final int V = 11;

    public static void main(String[] args) {
        int[] sizes = {6, 35};

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

            long startTime = System.nanoTime();

            double[][] A_copy = copyMatrix(A);
            double[] b_copy = Arrays.copyOf(b, b.length);

            householderTransformation(A_copy, b_copy);
            double[] x = backSubstitution(A_copy, b_copy);

            long endTime = System.nanoTime();
            double runtime = (endTime - startTime) / 1e9;

            double[] residual = calculateResidual(A, b, x);
            double residualNorm = vectorNorm(residual);

            double det = calculateDeterminant(A_copy);

            if (N == 6) {
                System.out.println("\nВерхняя треугольная матрица после преобразований:");
                printMatrix(A_copy);
                System.out.println("\nПреобразованный вектор правой части:");
                printVector(b_copy);
                System.out.println("\nВектор решения X:");
                printVector(x);
            } else {
                System.out.println("\nЭлементы X1..X5:");
                for (int i = 0; i < 5 && i < N; i++) {
                    System.out.printf("X%d = %.15f\n", i+1, x[i]);
                }
            }

            System.out.printf("\nВремя работы: %.6f сек\n", runtime);
            System.out.printf("Норма вектора невязки: %.15e\n", residualNorm);
            System.out.printf("Определитель матрицы: %.15e\n", det);
        }
    }

    private static double[][] generateMatrixA(int N) {
        double[][] A = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                A[i][j] = V + 1.2 * (i+1) - 0.7 * (j+1);
            }
            A[i][i] += 0.001;
        }
        return A;
    }

    private static double[] generateVectorB(int N) {
        double[] b = new double[N];
        for (int j = 0; j < N; j++) {
            b[j] = 17.0/V - Math.sin((j+1) * V);
        }
        return b;
    }

    private static void householderTransformation(double[][] A, double[] b) {
        int n = A.length;

        for (int k = 0; k < n-1; k++) {
            // Вычисляем вектор v для отражения
            double norm = 0;
            for (int i = k; i < n; i++) {
                norm += A[i][k] * A[i][k];
            }
            norm = Math.sqrt(norm);

            double alpha = -Math.signum(A[k][k]) * norm;
            double r = Math.sqrt(0.5 * (alpha * alpha - A[k][k] * alpha));

            double[] v = new double[n];
            for (int i = k; i < n; i++) {
                v[i] = (i == k) ? (A[k][k] - alpha) / (2 * r) : A[i][k] / (2 * r);
            }

            // Применяем отражение к матрице A
            for (int j = k; j < n; j++) {
                double dot = 0;
                for (int i = k; i < n; i++) {
                    dot += v[i] * A[i][j];
                }
                for (int i = k; i < n; i++) {
                    A[i][j] -= 2 * v[i] * dot;
                }
            }

            // Применяем отражение к вектору b
            double dot = 0;
            for (int i = k; i < n; i++) {
                dot += v[i] * b[i];
            }
            for (int i = k; i < n; i++) {
                b[i] -= 2 * v[i] * dot;
            }
        }
    }

    private static double[] backSubstitution(double[][] U, double[] b) {
        int n = U.length;
        double[] x = new double[n];

        for (int i = n-1; i >= 0; i--) {
            x[i] = b[i];
            for (int j = i+1; j < n; j++) {
                x[i] -= U[i][j] * x[j];
            }
            x[i] /= U[i][i];
        }

        return x;
    }

    private static double[] calculateResidual(double[][] A, double[] b, double[] x) {
        int n = A.length;
        double[] residual = new double[n];

        for (int i = 0; i < n; i++) {
            residual[i] = b[i];
            for (int j = 0; j < n; j++) {
                residual[i] -= A[i][j] * x[j];
            }
        }

        return residual;
    }

    private static double vectorNorm(double[] v) {
        double norm = 0;
        for (double val : v) {
            norm += val * val;
        }
        return Math.sqrt(norm);
    }

    private static double calculateDeterminant(double[][] U) {
        double det = 1.0;
        for (int i = 0; i < U.length; i++) {
            det *= U[i][i];
        }
        return det;
    }

    private static double[][] copyMatrix(double[][] original) {
        double[][] copy = new double[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }

    private static void printMatrix(double[][] A) {
        for (double[] row : A) {
            for (double val : row) {
                System.out.printf("%12.6f", val);
            }
            System.out.println();
        }
    }

    private static void printVector(double[] v) {
        for (double val : v) {
            System.out.printf("%12.16f\n", val);
        }
    }
}