package aud.aud2.zad3;

import java.util.function.*;
public class FunctionalInterfacesDemo {
    public static void main(String[] args) {
        // Function to get the length of a string
        Function<String, Integer> stringLength = str -> str.length();
        System.out.println("Length of 'Hello': " + stringLength.apply("Hello"));

        // BiFunction to sum two integers
        BiFunction<Integer, Integer, Integer> sum = (a, b) -> a + b;
        System.out.println("Sum of 5 and 3: " + sum.apply(5, 3));

        // Predicate to check if a number is even
        Predicate<Integer> isEven = num -> num % 2 == 0;
        System.out.println("Is 4 even? " + isEven.test(4));
        System.out.println("Is 5 even? " + isEven.test(5));

        // Consumer to print a string
        Consumer<String> printString = str -> System.out.println("Printing: " + str);
        printString.accept("Hello, World!");

        // Supplier to get current time in milliseconds
        Supplier<Long> currentTimeMillis = () -> System.currentTimeMillis();
        System.out.println("Current time in milliseconds: " + currentTimeMillis.get());
    }
}
