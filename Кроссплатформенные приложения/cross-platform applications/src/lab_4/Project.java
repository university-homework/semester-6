package lab_4;

import java.util.Scanner;
import java.util.regex.*;

public class Project {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();

        System.out.println(input + ": " + (isValidChemicalFormula(input) ? "YES" : "NO"));
    }

    public static boolean isValidChemicalFormula(String formula) {
        String elementPattern = "([A-Z][a-z]?)";
        String countPattern = "([1-9]\\d*)";
        String formulaPattern = "^(" + elementPattern + countPattern + "?)+$";

        System.out.println(formulaPattern);

        return Pattern.matches(formulaPattern, formula);
    }
}
