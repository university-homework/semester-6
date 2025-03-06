package lab_1;

import java.util.Arrays;
import java.util.Scanner;

public class Project {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        final int n = input.nextInt();
        GetSolution solution = new GetSolution(n);

        if (n == 120_000 || n == 240_000) {
            long startTime = System.currentTimeMillis();
            solution.getSolutionWithDouble();
            long endTime = System.currentTimeMillis();
            System.out.println("Время выполнения (double): " + ((endTime - startTime) / 1000) + " секунд(ы)");

            startTime = System.currentTimeMillis();
            solution.getSolutionWithFloat();
            endTime = System.currentTimeMillis();
            System.out.println("Время выполнения (float): " + ((endTime - startTime) / 1000) + " секунд(ы)");
        }
        else {
            solution.getSolutionWithDouble();
        }
    }
}

class GetSolution {
    int n;

    GetSolution(int n) {
        this.n = n;
    }

    public void getSolutionWithDouble() {
        double[] numbers = new double[n];
        double s1, s2, s3;

        SolutionWithDouble solution = new SolutionWithDouble(numbers);
        solution.fillArray();
        if (n == 12) {
            solution.printArray();
        }
        s1 = solution.getArraySum();
        solution.bubbleSort();
        if (n == 12) {
            solution.printArray();
        }
        s2 = solution.getArraySum();
        s3 = solution.getReverseArraySum();

        System.out.printf("S1 = %.15f, S2 = %.15f, S3 = %.15f\n", s1, s2, s3);
    }

    public void getSolutionWithFloat() {
        float[] numbers = new float[n];
        float s1, s2, s3;

        SolutionWithFloat solution = new SolutionWithFloat(numbers);
        solution.fillArray();
        if (n == 12) {
            solution.printArray();
        }
        s1 = solution.getArraySum();
        solution.bubbleSort();
        if (n == 12) {
            solution.printArray();
        }
        s2 = solution.getArraySum();
        s3 = solution.getReverseArraySum();

        System.out.printf("S1 = %.15f, S2 = %.15f, S3 = %.15f\n", s1, s2, s3);
    }
}

class SolutionWithDouble {
    final int v = 11;
    double[] numbers;

    SolutionWithDouble(double[] numbers) {
        this.numbers = numbers;
    }

    public void fillArray() {
        for (int i = 1; i <= numbers.length; i++) {
            if (i % 2 != 0) {
                numbers[i - 1] = (i + 1) * Math.sin(v + i);
            }
            else {
                numbers[i - 1] = (1.0 / (i + 1)) * Math.cos(v + i);
            }
        }
    }

    public void printArray() {
        System.out.print("Массив: ");
        System.out.println(Arrays.toString(numbers));
    }

    public double getArraySum() {
        double sum = 0;
        for (double number : numbers) {
            sum += number;
        }
        return sum;
    }

    public double getReverseArraySum() {
        double sum = 0;
        for (int i = numbers.length - 1; i >= 0; i--) {
            sum += numbers[i];
        }
        return sum;
    }

    public void bubbleSort() {
        boolean isSwapped;
        for (int i = 0; i < numbers.length - 1; i++) {
            isSwapped = false;
            for (int j = 0; j < numbers.length - i - 1; j++) {
                if (Math.abs(numbers[j]) > Math.abs(numbers[j + 1])) {
                    double temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                    isSwapped = true;
                }
            }
            if (!isSwapped) break;
        }
    }
}


class SolutionWithFloat {
    final int v = 11;
    float[] numbers;

    SolutionWithFloat(float[] numbers) {
        this.numbers = numbers;
    }

    public void fillArray() {
        for (int i = 1; i <= numbers.length; i++) {
            if (i % 2 != 0) {
                double res = (i + 1) * Math.sin(v + i);
                numbers[i - 1] = (float) res;
            }
            else {
                double res = (1.0 / (i + 1)) * Math.cos(v + i);
                numbers[i - 1] = (float) res;
            }
        }
    }

    public void printArray() {
        System.out.print("Массив: ");
        System.out.println(Arrays.toString(numbers));
    }

    public float getArraySum() {
        float sum = 0;
        for (float number : numbers) {
            sum += number;
        }
        return sum;
    }

    public float getReverseArraySum() {
        float sum = 0;
        for (int i = numbers.length - 1; i >= 0; i--) {
            sum += numbers[i];
        }
        return sum;
    }

    public void bubbleSort() {
        boolean isSwapped;
        for (int i = 0; i < numbers.length - 1; i++) {
            isSwapped = false;
            for (int j = 0; j < numbers.length - i - 1; j++) {
                if (Math.abs(numbers[j]) > Math.abs(numbers[j + 1])) {
                    float temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                    isSwapped = true;
                }
            }
            if (!isSwapped) break;
        }
    }
}