package aud.aud2.zad2;

import java.util.Scanner;

public class CalculatorTest {
    static final char RESULT = 'r';
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while(true){
            Calculator calculator = new Calculator();
            System.out.println(calculator.init());

            while (true){
                String line = scanner.nextLine();
                char choice = getCharLower(line);

                if (choice == RESULT) {
                    System.out.printf("final result = %f%n", calculator.getResult());
                    break;
                }

                String[] parts = line.split("\\s+");
                if (parts.length < 2) {
                    System.out.println("Please enter: <operator> <number>");
                    continue;
                }
                char operator = parts[0].charAt(0);
                double value = Double.parseDouble(parts[1]);
                try {
                    String result = calculator.execute(operator, value);
                    System.out.println(result);
                    System.out.println(calculator);
                } catch (UnknownOperatorException e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("(Y/N)");
            String again = scanner.nextLine();
            char choice2 = getCharLower(again);
            if (choice2 == 'n') {
                break;
            }
        }
    }

    private static char getCharLower(String line) {
        if (!line.trim().isEmpty()) {
            return Character.toLowerCase(line.charAt(0));
        }
        return '?';
    }
}
