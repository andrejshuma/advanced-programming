package kolokviumski.kol1.zad5;

import java.util.Scanner;

class MinMax<T extends Comparable<T>> {

    private int counter;
    private int minCounter;
    private int maxCounter;

    private T min;
    private T max;

    public MinMax() {
        minCounter = 0;
        maxCounter = 0;
        counter = 0;
        min = null;
        max = null;
    }

    public void update(T element) {
        counter++;
        if (min==null || element.compareTo(min) < 0) {
            min = element;
            minCounter = 1;
        } else if (element.compareTo(min) == 0) {
            minCounter++;
        }

        if (max==null || element.compareTo(max) > 0) {
            max = element;
            maxCounter = 1;
        } else if (element.compareTo(max) == 0) {
            maxCounter++;
        }
    }

    public T max() {
        return max;
    }

    public T min() {
        return min;
    }

    @Override
    public String toString() {
        return String.format(
                "%s %s %s%n",
                min(), max(),
                counter - minCounter - maxCounter
        );
    }
}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for (int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}