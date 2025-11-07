package kolokviumski.kol1.zad10;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Triple<T extends Number>{
    T a;
    T b;
    T c;

    List<T> list;

    public Triple(T a, T b, T c) {
        this.a = a;
        this.b = b;
        this.c = c;
        list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);
    }

    double max(){
        return Math.max(a.doubleValue(), Math.max(b.doubleValue(), c.doubleValue()));
    }

    double avarage(){
        return (a.doubleValue() + b.doubleValue() + c.doubleValue()) / 3.0;
    }
    void sort(){
        list = list.stream().sorted(Comparator.comparingDouble(Number::doubleValue)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f", list.get(0).doubleValue(), list.get(1).doubleValue(), list.get(2).doubleValue());
    }
}
public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.avarage());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.avarage());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.avarage());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde
// class Triple



