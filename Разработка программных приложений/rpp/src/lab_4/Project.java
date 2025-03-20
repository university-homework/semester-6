package lab_4;

import java.util.Scanner;

public class Project {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();

        DoubleMatrix matrix1 = new DoubleMatrix(N);

        long startTime = System.currentTimeMillis();
        double[][] Bt1 = matrix1.transpose(matrix1.B);
        double[][] C1 = matrix1.multiply(matrix1.A, Bt1);
        double sum1 = matrix1.getSum(C1);
        long endTime = System.currentTimeMillis();

        if (N == 6) {
            matrix1.printMatrix(matrix1.A);
            System.out.println();
            matrix1.printMatrix(matrix1.B);
            System.out.println();
            matrix1.printMatrix(Bt1);
            System.out.println();
            matrix1.printMatrix(C1);
            System.out.println();
        }

        System.out.println("Cумма (double) = " + sum1);
        System.out.println("Время работы программы (double) = " + (endTime - startTime));

        FloatMatrix matrix2 = new FloatMatrix(N);

        startTime = System.currentTimeMillis();
        float[][] Bt2 = matrix2.transpose(matrix2.B);
        float[][] C2 = matrix2.multiply(matrix2.A, Bt2);
        float sum2 = matrix2.getSum(C2);
        endTime = System.currentTimeMillis();

        if (N == 6) {
            matrix2.printMatrix(matrix2.A);
            System.out.println();
            matrix2.printMatrix(matrix2.B);
            System.out.println();
            matrix2.printMatrix(Bt2);
            System.out.println();
            matrix2.printMatrix(C2);
            System.out.println();
        }

        System.out.println("Cумма (float) = " + sum2);
        System.out.println("Время работы программы (float) = " + (endTime - startTime));
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

    public float[][] transpose(float[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        float[][] transposedMatrix = new float[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }

        return transposedMatrix;
    }

    public float[][] multiply(float[][] matrix1, float[][] matrix2) {
        int rows = matrix1.length;
        int cols = matrix1[0].length;

        float[][] result = new float[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                float sum = 0;
                for (int k = 0; k < cols; k++) {
                    sum += A[i][k] * matrix2[j][k];
                }
                result[i][j] = sum;
            }
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

    public double[][] transpose(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        double[][] transposedMatrix = new double[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }

        return transposedMatrix;
    }

    public double[][] multiply(double[][] matrix1, double[][] matrix2) {
        int rows = matrix1.length;
        int cols = matrix1[0].length;

        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                double sum = 0;
                for (int k = 0; k < cols; k++) {
                    sum += A[i][k] * matrix2[j][k];
                }
                result[i][j] = sum;
            }
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
