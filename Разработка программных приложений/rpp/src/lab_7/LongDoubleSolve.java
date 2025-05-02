package lab_7;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;

public class LongDoubleSolve {
    private static final int V = 11;
    private static final MathContext MC = new MathContext(18, RoundingMode.HALF_UP);
    private static final BigDecimal BD_V = BigDecimal.valueOf(V);
    private static final BigDecimal BD_1_1 = new BigDecimal("1.1");
    private static final BigDecimal BD_0_25 = new BigDecimal("0.25");

    public static void main(String[] args) {
        int[] sizes = {7, 100, 500};

        for (int N : sizes) {
            System.out.println("\nРешение для N = " + N);

            BigDecimal[][] A = generateMatrixA(N);
            BigDecimal[] b = generateVectorB(N);

            if (N == 7) {
                System.out.println("\nИсходная матрица A:");
                printMatrix(A);
                System.out.println("\nВектор b:");
                printVector(b);
            }

            long startTime = System.currentTimeMillis();

            BigDecimal[][] A_copy = copyMatrix(A);
            BigDecimal[] b_copy = Arrays.copyOf(b, b.length);

            givensRotation(A_copy, b_copy);
            BigDecimal[] x = backSubstitution(A_copy, b_copy);

            long endTime = System.currentTimeMillis();
            double runtime = (endTime - startTime) / 1000.0;

            BigDecimal[] residual = calculateResidual(A, b, x);
            BigDecimal residualNorm = vectorNorm(residual);

            BigDecimal det = calculateDeterminant(A_copy);

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
                    System.out.printf("X%d = %s\n", i+1, x[i].round(new MathContext(16, RoundingMode.HALF_UP)));
                }
            }

            System.out.println("\nВремя работы: " + runtime + " сек");
            System.out.println("Норма вектора невязки: " + residualNorm.round(new MathContext(16)));
            System.out.println("Определитель матрицы: " + det.round(new MathContext(16)));
        }
    }

    private static BigDecimal[][] generateMatrixA(int N) {
        BigDecimal[][] A = new BigDecimal[N][N];
        BigDecimal v10 = BD_V.divide(BigDecimal.TEN, MC);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                BigDecimal i_bd = BigDecimal.valueOf(i);
                BigDecimal j_bd = BigDecimal.valueOf(j);

                BigDecimal term1 = v10;
                BigDecimal term2 = BD_1_1.multiply(j_bd.subtract(i_bd), MC);
                BigDecimal term3 = BigDecimal.valueOf(Math.cos(i - j)).negate();

                A[i][j] = term1.add(term2).add(term3, MC);
            }
            A[i][i] = A[i][i].add(BD_0_25, MC);
        }
        return A;
    }

    private static BigDecimal[] generateVectorB(int N) {
        BigDecimal[] b = new BigDecimal[N];
        BigDecimal v_inv = BigDecimal.ONE.divide(BD_V, MC);

        for (int j = 0; j < N; j++) {
            BigDecimal j_bd = BigDecimal.valueOf(j + 1);
            BigDecimal term = BigDecimal.valueOf(Math.sin(j_bd.multiply(BD_V).doubleValue()));
            b[j] = v_inv.subtract(term, MC);
        }
        return b;
    }

    private static void givensRotation(BigDecimal[][] A, BigDecimal[] b) {
        int N = A.length;

        for (int j = 0; j < N; j++) {
            for (int i = j + 1; i < N; i++) {
                if (!A[i][j].equals(BigDecimal.ZERO)) {
                    // Вычисляем коэффициенты вращения
                    BigDecimal r = sqrt(A[j][j].pow(2, MC).add(A[i][j].pow(2, MC), MC), MC);
                    BigDecimal c = A[j][j].divide(r, MC);
                    BigDecimal s = A[i][j].divide(r, MC);

                    // Применяем вращение к строкам j и i
                    for (int k = j; k < N; k++) {
                        BigDecimal temp = c.multiply(A[j][k], MC).add(s.multiply(A[i][k], MC), MC);
                        A[i][k] = s.negate().multiply(A[j][k], MC).add(c.multiply(A[i][k], MC), MC);
                        A[j][k] = temp;
                    }

                    // Применяем вращение к вектору b
                    BigDecimal temp = c.multiply(b[j], MC).add(s.multiply(b[i], MC), MC);
                    b[i] = s.negate().multiply(b[j], MC).add(c.multiply(b[i], MC), MC);
                    b[j] = temp;
                }
            }
        }
    }

    private static BigDecimal sqrt(BigDecimal value, MathContext mc) {
        BigDecimal x = new BigDecimal(Math.sqrt(value.doubleValue()), mc);
        return x.add(value.subtract(x.multiply(x, mc), mc).divide(x.multiply(new BigDecimal("2"), mc), mc), mc);
    }

    private static BigDecimal[] backSubstitution(BigDecimal[][] U, BigDecimal[] b) {
        int N = U.length;
        BigDecimal[] x = new BigDecimal[N];

        for (int i = N - 1; i >= 0; i--) {
            x[i] = b[i];
            for (int j = i + 1; j < N; j++) {
                x[i] = x[i].subtract(U[i][j].multiply(x[j], MC), MC);
            }
            x[i] = x[i].divide(U[i][i], MC);
        }

        return x;
    }

    private static BigDecimal[] calculateResidual(BigDecimal[][] A, BigDecimal[] b, BigDecimal[] x) {
        int N = A.length;
        BigDecimal[] residual = new BigDecimal[N];

        for (int i = 0; i < N; i++) {
            residual[i] = b[i];
            for (int j = 0; j < N; j++) {
                residual[i] = residual[i].subtract(A[i][j].multiply(x[j], MC), MC);
            }
        }

        return residual;
    }

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
                System.out.printf("%12.6f", val.doubleValue());
            }
            System.out.println();
        }
    }

    private static void printVector(BigDecimal[] v) {
        for (BigDecimal val : v) {
            System.out.printf("%12.18f\n", val.doubleValue());
        }
    }
}
