package lab_1;

import java.util.Scanner;

public class Project {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите целое число: ");
        int n = scanner.nextInt();

        Fibonacci num = new Fibonacci(n);

        System.out.printf("Введенное n: %d\n", num.getN());
        System.out.printf("Число Фибоначчи: %d\n", num.getAmount());
        System.out.printf("Последняя цифра числа: %d\n", num.getLastDigit());
    }
}

class Fibonacci {
    protected final int n;
    protected int amount;

    Fibonacci(int n) {
        this.n = n;
        amount = countAmount();
    }

    protected int countAmount() {
        int sum = 0, start = 1;

        for (int i = 0; i < n; i++) {
            int previous_sum = sum;
            sum += start;
            start = previous_sum;
        }

        return sum;
    }

    public int getN() {return n;}
    public int getAmount() {return amount;}
    public int getLastDigit() {return amount % 10;}
}
