package lab_2;

import java.util.Scanner;

public class Project {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int N = input.nextInt();

        Matrix matrix = new Matrix(N);

        matrix.generateMatrix();

        if (N < 10) {
            matrix.printMatrix();
        }

        long startTime = System.currentTimeMillis();
        double S1 = matrix.getSumByRows();
        long endTime = System.currentTimeMillis();
        long t1 = endTime - startTime;

        startTime = System.currentTimeMillis();
        double S2 = matrix.getSumByCols();
        endTime = System.currentTimeMillis();
        long t2 = endTime - startTime;

        System.out.println("A11 = " + matrix.A[0][0]);
        System.out.println("Ann = " + matrix.A[N - 1][N - 1]);
        System.out.println("S1 = " + S1);
        System.out.println("t1 = " + t1 + "мс");
        System.out.println("S2 = " + S2);
        System.out.println("t2 = " + t2 + "мс");
    }
}

class Matrix {
    double[][] A;
    int V = 11;

    Matrix(int N) {
        this.A = new double[N][N];
    }

    public void generateMatrix() {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                A[i][j] = (Math.sin(0.2 * (i + 1)) - Math.cos(0.3 * (j + 1))) / V;
            }
        }
    }

    public void printMatrix() {
        for (double[] row : A) {
            for (double col : row) {
                System.out.print(col + " ");
            }
            System.out.println();
        }
    }

    public double getSumByRows() {
        double sum = 0;

        for (double[] row : A) {
            for (double col : row) {
                sum += col;
            }
        }

        return sum;
    }

    public double getSumByCols() {
        double sum = 0;

        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                sum += A[j][i];
            }
        }

        return sum;
    }
}
