package lab_3;

import java.util.Scanner;

public class Project {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();

        long startTime = System.currentTimeMillis();
        DoubleMatrix matrix1 = new DoubleMatrix(N);
        double[][] C1 = matrix1.multiply();
        double sum1 = matrix1.getSum(C1);
        long endTime = System.currentTimeMillis();

        System.out.println("Cумма (double) = " + sum1);
        System.out.println("Время работы программы (double) = " + (endTime - startTime));

        if (N == 6) {
            matrix1.printMatrix(matrix1.A);
            System.out.println();
            matrix1.printMatrix(matrix1.B);
            System.out.println();
            matrix1.printMatrix(C1);
            System.out.println();
        }

        startTime = System.currentTimeMillis();
        FloatMatrix matrix2 = new FloatMatrix(N);
        float[][] C2 = matrix2.multiply();
        float sum2 = matrix2.getSum(C2);
        endTime = System.currentTimeMillis();

        System.out.println("Cумма (float) = " + sum2);
        System.out.println("Время работы программы (float) = " + (endTime - startTime));

        if (N == 6) {
            matrix2.printMatrix(matrix2.A);
            System.out.println();
            matrix2.printMatrix(matrix2.B);
            System.out.println();
            matrix2.printMatrix(C2);
            System.out.println();
        }
    }
}

class DoubleMatrix {
    double[][] A;
    double[][] B;

    int V = 11;

    DoubleMatrix(int N) {
        this.A = new double[N][N];
        this.B = new double[N][N];

        this.generateMatrixA();
        this.generateMatrixB();
    }

    private void generateMatrixA() {
        for (int i = 0; i < this.A.length; i++) {
            for (int j = 0; j < this.A[i].length; j++) {
                this.A[i][j] = (Math.cos(0.2 * (i + 1)) - Math.sin(0.3 * (j + 1))) / V;
            }
        }
    }

    private void generateMatrixB() {
        for (int i = 0; i < this.B.length; i++) {
            for (int j = 0; j < this.B[i].length; j++) {
                this.B[i][j] = (Math.sin(0.7 * (j + 1)) - Math.cos(0.4 * (i + 1))) / V;
            }
        }
    }

    public double[][] multiply() {
        int N = this.A.length;
        double[][] result = new double[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                double[] column = new double[N];
                for (int k = 0; k < N; k++) {
                    column[k] = this.B[k][j];
                }
                result[i][j] = this.multiplyRowByColumn(A[i], column);
            }
        }

        return result;
    }

    private double multiplyRowByColumn(double[] row, double[] column) {
        double result = 0;

        for (int i = 0; i < row.length; i++) {
            result += row[i] * column[i];
        }

        return result;
    }

    public double getSum(double[][] C) {
        double sum = 0;

        for (double[] row : C) {
            for (double item : row) {
                sum += item;
            }
        }

        return sum;
    }

    public void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            for (double item : row) {
                System.out.print(item + " ");
            }
            System.out.println();
        }
    }
}

class FloatMatrix {
    float[][] A;
    float[][] B;

    int V = 11;

    FloatMatrix(int N) {
        this.A = new float[N][N];
        this.B = new float[N][N];

        this.generateMatrixA();
        this.generateMatrixB();
    }

    private void generateMatrixA() {
        for (int i = 0; i < this.A.length; i++) {
            for (int j = 0; j < this.A[i].length; j++) {
                this.A[i][j] = (float) (Math.cos(0.2 * (i + 1)) - Math.sin(0.3 * (j + 1))) / V;
            }
        }
    }

    private void generateMatrixB() {
        for (int i = 0; i < this.B.length; i++) {
            for (int j = 0; j < this.B[i].length; j++) {
                this.B[i][j] = (float) (Math.sin(0.7 * (j + 1)) - Math.cos(0.4 * (i + 1))) / V;
            }
        }
    }

    public float[][] multiply() {
        int N = this.A.length;
        float[][] result = new float[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                float[] column = new float[N];
                for (int k = 0; k < N; k++) {
                    column[k] = this.B[k][j];
                }
                result[i][j] = this.multiplyRowByColumn(A[i], column);
            }
        }

        return result;
    }

    private float multiplyRowByColumn(float[] row, float[] column) {
        float result = 0;

        for (int i = 0; i < row.length; i++) {
            result += row[i] * column[i];
        }

        return result;
    }

    public float getSum(float[][] C) {
        float sum = 0;

        for (float[] row : C) {
            for (float item : row) {
                sum += item;
            }
        }

        return sum;
    }

    public void printMatrix(float[][] matrix) {
        for (float[] row : matrix) {
            for (float item : row) {
                System.out.print(item + " ");
            }
            System.out.println();
        }
    }
}
