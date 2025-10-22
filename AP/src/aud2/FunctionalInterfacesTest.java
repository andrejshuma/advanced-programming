package aud2;

import java.util.function.*;

public class FunctionalInterfacesTest {
    public static void main(String[] args) {
        //1. Function
        Function<String, Integer> function = str -> str.length();
        System.out.println(function.apply("Stefan"));

        BiFunction<Integer,Integer,Integer> biFunction = (a,b) -> a+b;
        System.out.println(biFunction.apply(5,6));

        //2. Predicate (condition)
        Predicate<Integer> isEven = number -> number%2==0;
        System.out.println(isEven.test(6));
        System.out.println(isEven.test(5));

        //3. Supplier
        Supplier<Long> currentTimeInMs = () -> System.currentTimeMillis();

        //4. Consumer
        Consumer<String> printer = str -> System.out.println(str);
        printer.accept("Dimi");
    }
}
