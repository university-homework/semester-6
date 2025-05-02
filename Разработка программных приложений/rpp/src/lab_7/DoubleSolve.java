package lab_7;

import java.util.Arrays;

public class DoubleSolve {
    private static final int V = 11;

    public static void main(String[] args) {
        int[] sizes = {7, 100, 500};

        for (int N : sizes) {
            System.out.println("\nРешение для N = " + N);

            double[][] A = generateMatrixA(N);
            double[] b = generateVectorB(N);

            if (N == 7) {
                System.out.println("\nИсходная матрица A:");
                printMatrix(A);
                System.out.println("\nВектор b:");
                printVector(b);
            }

            long startTime = System.currentTimeMillis();

            double[][] A_copy = copyMatrix(A);
            double[] b_copy = Arrays.copyOf(b, b.length);

            givensRotation(A_copy, b_copy);
            double[] x = backSubstitution(A_copy, b_copy);

            long endTime = System.currentTimeMillis();
            double runtime = (endTime - startTime) / 1000.0;

            double[] residual = calculateResidual(A, b, x);
            double residualNorm = vectorNorm(residual);

            double det = calculateDeterminant(A_copy);

            if (N == 7) {
                System.out.println("\nВерхняя треугольная матрица после преобразований:");
                printMatrix(A_copy);
                System.out.println("\nПреобразованный вектор правой части:");
                printVector(b_copy);
                System.out.println("\nВектор решения X:");
                printVector(x);
            } else {
                System.out.println("\nЭлементы решения X55..X63:");
                for (int i = 54; i < 63 && i < N; i++) {
                    System.out.printf("X%d = %.8f\n", i+1, x[i]);
                }
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
            A[i][i] += 0.25;
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

    private static void givensRotation(double[][] A, double[] b) {
        int N = A.length;

        for (int j = 0; j < N; j++) {
            for (int i = j + 1; i < N; i++) {
                if (A[i][j] != 0) {
                    double r = Math.hypot(A[j][j], A[i][j]);
                    double c = A[j][j] / r;
                    double s = A[i][j] / r;

                    // Применяем вращение к строкам j и i
                    for (int k = j; k < N; k++) {
                        double temp = c * A[j][k] + s * A[i][k];
                        A[i][k] = -s * A[j][k] + c * A[i][k];
                        A[j][k] = temp;
                    }

                    // Применяем вращение к вектору b
                    double temp = c * b[j] + s * b[i];
                    b[i] = -s * b[j] + c * b[i];
                    b[j] = temp;
                }
            }
        }
    }

    private static double[] backSubstitution(double[][] U, double[] b) {
        int N = U.length;
        double[] x = new double[N];

        for (int i = N - 1; i >= 0; i--) {
            x[i] = b[i];
            for (int j = i + 1; j < N; j++) {
                x[i] -= U[i][j] * x[j];
            }
            x[i] /= U[i][i];
        }

        return x;
    }

    private static double[] calculateResidual(double[][] A, double[] b, double[] x) {
        int N = A.length;
        double[] residual = new double[N];

        for (int i = 0; i < N; i++) {
            residual[i] = b[i];
            for (int j = 0; j < N; j++) {
                residual[i] -= A[i][j] * x[j];
            }
        }

        return residual;
    }

    private static double vectorNorm(double[] v) {
        double norm = 0.0;
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
