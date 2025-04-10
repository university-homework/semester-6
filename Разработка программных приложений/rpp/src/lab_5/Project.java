package lab_5;

import java.util.Scanner;

public class Project {
    private static final int V = 11;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int N = scanner.nextInt();

        if (N == 8) {
            System.out.println("Решение для N = " + N + ":");
            solveAndPrint(N, "double");
            solveAndPrint(N, "float");
        }
        else {
            System.out.println("\nРешение для N = 3,000,000:");
            solveAndPrintLarge(N, "double");
            solveAndPrintLarge(N, "float");
        }
    }

    private static void solveAndPrint(int N, String type) {
        System.out.println("\nТип: " + type);
        if (type.equals("double")) {
            double[] A = new double[N - 1];
            double[] B = new double[N];
            double[] C = new double[N - 1];
            double[] D = new double[N];
            fillArrays(A, B, C, D, N);
            double[] X = solveTridiagonal(A, B, C, D, N);
            printArrays(A, B, C, D, X, N);
            double residual = computeResidualNorm(A, B, C, D, X, N);
            System.out.printf("Норма невязки (double): %.15e\n", residual / Math.pow(10, 4));
        } else {
            float[] A = new float[N - 1];
            float[] B = new float[N];
            float[] C = new float[N - 1];
            float[] D = new float[N];
            fillArraysFloat(A, B, C, D, N);
            float[] X = solveTridiagonalFloat(A, B, C, D, N);
            printArraysFloat(A, B, C, D, X, N);
            float residual = computeResidualNormFloat(A, B, C, D, X, N);
            System.out.printf("Норма невязки (float): %.10e\n", residual / Math.pow(10, 2));
        }
    }

    private static void solveAndPrintLarge(int N, String type) {
        System.out.println("\nТип: " + type);
        if (type.equals("double")) {
            double[] A = new double[N - 1];
            double[] B = new double[N];
            double[] C = new double[N - 1];
            double[] D = new double[N];
            fillArrays(A, B, C, D, N);
            double[] X = solveTridiagonal(A, B, C, D, N);
            double residual = computeResidualNorm(A, B, C, D, X, N);
            System.out.printf("Норма невязки (double): %.15e\n", residual / Math.pow(10, 4));
            printLargeX(X);
        } else {
            float[] A = new float[N - 1];
            float[] B = new float[N];
            float[] C = new float[N - 1];
            float[] D = new float[N];
            fillArraysFloat(A, B, C, D, N);
            float[] X = solveTridiagonalFloat(A, B, C, D, N);
            float residual = computeResidualNormFloat(A, B, C, D, X, N);
            System.out.printf("Норма невязки (float): %.10e\n", residual / Math.pow(10, 2));
            printLargeXFloat(X);
        }
    }

    private static void fillArrays(double[] A, double[] B, double[] C, double[] D, int N) {
        for (int k = 0; k < N - 1; k++) {
            int i = k + 2;
            A[k] = 0.3 * Math.sin(i) / V;
        }
        for (int k = 0; k < N; k++) {
            int i = k + 1;
            B[k] = 10 * V + (double) i / V;
        }
        for (int k = 0; k < N - 1; k++) {
            int i = k + 1;
            C[k] = 0.4 * Math.cos(i) / V;
        }
        for (int k = 0; k < N; k++) {
            int i = k + 1;
            D[k] = 1.3 + (double) i / V;
        }
    }

    private static void fillArraysFloat(float[] A, float[] B, float[] C, float[] D, int N) {
        for (int k = 0; k < N - 1; k++) {
            int i = k + 2;
            A[k] = (float) (0.3 * Math.sin(i) / V);
        }
        for (int k = 0; k < N; k++) {
            int i = k + 1;
            B[k] = (float) (10 * V + (double) i / V);
        }
        for (int k = 0; k < N - 1; k++) {
            int i = k + 1;
            C[k] = (float) (0.4 * Math.cos(i) / V);
        }
        for (int k = 0; k < N; k++) {
            int i = k + 1;
            D[k] = (float) (1.3 + (double) i / V);
        }
    }

    private static double[] solveTridiagonal(double[] A, double[] B, double[] C, double[] D, int N) {
        double[] alpha = new double[N - 1];
        double[] beta = new double[N];
        double[] X = new double[N];

        alpha[0] = -C[0] / B[0];
        beta[0] = D[0] / B[0];

        for (int i = 1; i < N; i++) {
            double a = (i > 1) ? A[i - 1] : 0;
            double denom = B[i] + a * alpha[i - 1];
            if (i < N - 1) {
                alpha[i] = -C[i] / denom;
            }
            beta[i] = (D[i] - a * beta[i - 1]) / denom;
        }

        X[N - 1] = beta[N - 1];
        for (int i = N - 2; i >= 0; i--) {
            X[i] = alpha[i] * X[i + 1] + beta[i];
        }

        return X;
    }

    private static float[] solveTridiagonalFloat(float[] A, float[] B, float[] C, float[] D, int N) {
        float[] alpha = new float[N - 1];
        float[] beta = new float[N];
        float[] X = new float[N];

        alpha[0] = -C[0] / B[0];
        beta[0] = D[0] / B[0];

        for (int i = 1; i < N; i++) {
            float a = (i > 1) ? A[i - 1] : 0;
            float denom = B[i] + a * alpha[i - 1];
            if (i < N - 1) {
                alpha[i] = -C[i] / denom;
            }
            beta[i] = (D[i] - a * beta[i - 1]) / denom;
        }

        X[N - 1] = beta[N - 1];
        for (int i = N - 2; i >= 0; i--) {
            X[i] = alpha[i] * X[i + 1] + beta[i];
        }

        return X;
    }

    private static double computeResidualNorm(double[] A, double[] B, double[] C, double[] D, double[] X, int N) {
        double maxResidual = 0.0;
        for (int i = 0; i < N; i++) {
            double res;
            if (i == 0) {
                res = B[i] * X[i] + C[i] * X[i + 1] - D[i];
            } else if (i == N - 1) {
                res = A[i - 1] * X[i - 1] + B[i] * X[i] - D[i];
            } else {
                res = A[i - 1] * X[i - 1] + B[i] * X[i] + C[i] * X[i + 1] - D[i];
            }
            maxResidual = Math.max(maxResidual, Math.abs(res));
        }
        return maxResidual;
    }

    private static float computeResidualNormFloat(float[] A, float[] B, float[] C, float[] D, float[] X, int N) {
        float maxResidual = 0.0f;
        for (int i = 0; i < N; i++) {
            float res;
            if (i == 0) {
                res = B[i] * X[i] + C[i] * X[i + 1] - D[i];
            } else if (i == N - 1) {
                res = A[i - 1] * X[i - 1] + B[i] * X[i] - D[i];
            } else {
                res = A[i - 1] * X[i - 1] + B[i] * X[i] + C[i] * X[i + 1] - D[i];
            }
            maxResidual = Math.max(maxResidual, Math.abs(res));
        }
        return maxResidual;
    }

    private static void printArrays(double[] A, double[] B, double[] C, double[] D, double[] X, int N) {
        System.out.println("Элементы Ai (i=2.." + N + "):");
        for (int k = 0; k < N - 1; k++) {
            System.out.printf("A%d = %.6f\n", k + 2, A[k]);
        }

        System.out.println("\nЭлементы Bi (i=1.." + N + "):");
        for (int k = 0; k < N; k++) {
            System.out.printf("B%d = %.6f\n", k + 1, B[k]);
        }

        System.out.println("\nЭлементы Ci (i=1.." + (N - 1) + "):");
        for (int k = 0; k < N - 1; k++) {
            System.out.printf("C%d = %.6f\n", k + 1, C[k]);
        }

        System.out.println("\nЭлементы Di (i=1.." + N + "):");
        for (int k = 0; k < N; k++) {
            System.out.printf("D%d = %.6f\n", k + 1, D[k]);
        }

        System.out.println("\nВектор решения Xi:");
        for (int k = 0; k < N; k++) {
            System.out.printf("X%d = %.15f\n", k + 1, X[k]);
        }
    }

    private static void printArraysFloat(float[] A, float[] B, float[] C, float[] D, float[] X, int N) {
        System.out.println("Элементы Ai (i=2.." + N + "):");
        for (int k = 0; k < N - 1; k++) {
            System.out.printf("A%d = %.6f\n", k + 2, A[k]);
        }

        System.out.println("\nЭлементы Bi (i=1.." + N + "):");
        for (int k = 0; k < N; k++) {
            System.out.printf("B%d = %.6f\n", k + 1, B[k]);
        }

        System.out.println("\nЭлементы Ci (i=1.." + (N - 1) + "):");
        for (int k = 0; k < N - 1; k++) {
            System.out.printf("C%d = %.6f\n", k + 1, C[k]);
        }

        System.out.println("\nЭлементы Di (i=1.." + N + "):");
        for (int k = 0; k < N; k++) {
            System.out.printf("D%d = %.6f\n", k + 1, D[k]);
        }

        System.out.println("\nВектор решения Xi:");
        for (int k = 0; k < N; k++) {
            System.out.printf("X%d = %.10f\n", k + 1, X[k]);
        }
    }

    private static void printLargeX(double[] X) {
        int[] indices = {300000, 300001, 300002, 300003};
        System.out.println("Значения вектора X для i=300001..300004:");
        for (int idx : indices) {
            System.out.printf("X%d = %.15e\n", idx + 1, X[idx]);
        }
    }

    private static void printLargeXFloat(float[] X) {
        int[] indices = {300000, 300001, 300002, 300003};
        System.out.println("Значения вектора X для i=300001..300004:");
        for (int idx : indices) {
            System.out.printf("X%d = %.10e\n", idx + 1, X[idx]);
        }
    }
}
