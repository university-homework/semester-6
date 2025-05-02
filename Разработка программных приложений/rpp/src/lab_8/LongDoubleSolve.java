package lab_8;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;

public class LongDoubleSolve {
    private static final int V = 11;
    private static final MathContext MC = new MathContext(18, RoundingMode.HALF_UP);
    private static final BigDecimal BD_V = new BigDecimal(V);
    private static final BigDecimal BD_1_2 = new BigDecimal("1.2");
    private static final BigDecimal BD_0_7 = new BigDecimal("0.7");
    private static final BigDecimal BD_0_001 = new BigDecimal("0.001");
    private static final BigDecimal BD_17 = new BigDecimal("17");
    private static final BigDecimal BD_2 = new BigDecimal("2");

    public static void main(String[] args) {
        int[] sizes = {6, 35};

        for (int N : sizes) {
            System.out.println("\nРешение для N = " + N);

            BigDecimal[][] A = generateMatrixA(N);
            BigDecimal[] b = generateVectorB(N);

            if (N == 6) {
                System.out.println("\nИсходная матрица A:");
                printMatrix(A);
                System.out.println("\nВектор b:");
                printVector(b);
            }

            long startTime = System.nanoTime();

            BigDecimal[][] A_copy = copyMatrix(A);
            BigDecimal[] b_copy = Arrays.copyOf(b, b.length);

            householderTransformation(A_copy, b_copy);
            BigDecimal[] x = backSubstitution(A_copy, b_copy);

            long endTime = System.nanoTime();
            double runtime = (endTime - startTime) / 1e9;

            BigDecimal[] residual = calculateResidual(A, b, x);
            BigDecimal residualNorm = vectorNorm(residual);

            BigDecimal det = calculateDeterminant(A_copy);

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
                    System.out.printf("X%d = %s\n", i+1, x[i].round(new MathContext(18)));
                }
            }

            System.out.printf("\nВремя работы: %.6f сек\n", runtime);
            System.out.println("Норма вектора невязки: " + residualNorm.round(new MathContext(18)));
            System.out.println("Определитель матрицы: " + det.round(new MathContext(18)));
        }
    }

    private static BigDecimal[][] generateMatrixA(int N) {
        BigDecimal[][] A = new BigDecimal[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                BigDecimal i_bd = new BigDecimal(i+1);
                BigDecimal j_bd = new BigDecimal(j+1);

                A[i][j] = BD_V.add(BD_1_2.multiply(i_bd, MC), MC)
                        .subtract(BD_0_7.multiply(j_bd, MC), MC);
            }
            A[i][i] = A[i][i].add(BD_0_001, MC);
        }
        return A;
    }

    private static BigDecimal[] generateVectorB(int N) {
        BigDecimal[] b = new BigDecimal[N];
        BigDecimal v_inv = BD_17.divide(BD_V, MC);

        for (int j = 0; j < N; j++) {
            BigDecimal j_bd = new BigDecimal(j+1);
            BigDecimal sinTerm = BigDecimal.valueOf(Math.sin(j_bd.multiply(BD_V, MC).doubleValue()));
            b[j] = v_inv.subtract(sinTerm, MC);
        }
        return b;
    }

    private static void householderTransformation(BigDecimal[][] A, BigDecimal[] b) {
        int n = A.length;

        for (int k = 0; k < n-1; k++) {
            // Вычисляем норму столбца
            BigDecimal normSq = BigDecimal.ZERO;
            for (int i = k; i < n; i++) {
                normSq = normSq.add(A[i][k].pow(2, MC), MC);
            }
            BigDecimal norm = sqrt(normSq, MC);

            BigDecimal alpha = A[k][k].compareTo(BigDecimal.ZERO) >= 0 ?
                    norm.negate() : norm;

            BigDecimal r = sqrt(alpha.pow(2, MC).subtract(A[k][k].multiply(alpha, MC), MC).divide(BD_2, MC), MC);

            BigDecimal[] v = new BigDecimal[n];
            for (int i = 0; i < n; i++) {
                if (i < k) {
                    v[i] = BigDecimal.ZERO;
                } else if (i == k) {
                    v[i] = A[k][k].subtract(alpha, MC).divide(BD_2.multiply(r, MC), MC);
                } else {
                    v[i] = A[i][k].divide(BD_2.multiply(r, MC), MC);
                }
            }

            // Применяем отражение к матрице A
            for (int j = k; j < n; j++) {
                BigDecimal dot = BigDecimal.ZERO;
                for (int i = k; i < n; i++) {
                    dot = dot.add(v[i].multiply(A[i][j], MC), MC);
                }
                for (int i = k; i < n; i++) {
                    A[i][j] = A[i][j].subtract(BD_2.multiply(v[i].multiply(dot, MC), MC), MC);
                }
            }

            // Применяем отражение к вектору b
            BigDecimal dot = BigDecimal.ZERO;
            for (int i = k; i < n; i++) {
                dot = dot.add(v[i].multiply(b[i], MC), MC);
            }
            for (int i = k; i < n; i++) {
                b[i] = b[i].subtract(BD_2.multiply(v[i].multiply(dot, MC), MC), MC);
            }
        }
    }

    private static BigDecimal sqrt(BigDecimal value, MathContext mc) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new ArithmeticException("Square root of negative number");
        }

        BigDecimal x = new BigDecimal(Math.sqrt(value.doubleValue()), mc);
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        // Итерации Ньютона для уточнения
        for (int i = 0; i < 10; i++) {
            x = x.add(value.divide(x, mc), mc).divide(BD_2, mc);
        }
        return x;
    }

    private static BigDecimal[] backSubstitution(BigDecimal[][] U, BigDecimal[] b) {
        int n = U.length;
        BigDecimal[] x = new BigDecimal[n];

        for (int i = n-1; i >= 0; i--) {
            x[i] = b[i];
            for (int j = i+1; j < n; j++) {
                x[i] = x[i].subtract(U[i][j].multiply(x[j], MC), MC);
            }
            x[i] = x[i].divide(U[i][i], MC);
        }

        return x;
    }

    private static BigDecimal[] calculateResidual(BigDecimal[][] A, BigDecimal[] b, BigDecimal[] x) {
        int n = A.length;
        BigDecimal[] residual = new BigDecimal[n];

        for (int i = 0; i < n; i++) {
            residual[i] = b[i];
            for (int j = 0; j < n; j++) {
                residual[i] = residual[i].subtract(A[i][j].multiply(x[j], MC), MC);
            }
        }

        return residual;
    }

    // Вычисление нормы вектора
    private static BigDecimal vectorNorm(BigDecimal[] v) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal val : v) {
            sum = sum.add(val.pow(2, MC), MC);
        }
        return sqrt(sum, MC);
    }

    private static BigDecimal calculateDeterminant(BigDecimal[][] U) {
        BigDecimal det = BigDecimal.ONE;
        for (int i = 0; i < U.length; i++) {
            det = det.multiply(U[i][i], MC);
        }
        return det;
    }

    private static BigDecimal[][] copyMatrix(BigDecimal[][] original) {
        BigDecimal[][] copy = new BigDecimal[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }

    private static void printMatrix(BigDecimal[][] A) {
        for (BigDecimal[] row : A) {
            for (BigDecimal val : row) {
                System.out.printf("%12.6f ", val);
            }
            System.out.println();
        }
    }

    private static void printVector(BigDecimal[] v) {
        for (BigDecimal val : v) {
            System.out.printf("%18.18f\n", val);
        }
    }
}
