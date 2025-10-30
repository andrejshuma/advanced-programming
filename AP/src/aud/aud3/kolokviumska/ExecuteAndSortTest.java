package aud.aud3.kolokviumska;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ExecuteAndSortTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = Integer.parseInt(sc.nextLine());
        int n = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { // students
            int studentScenario = Integer.parseInt(sc.nextLine());
            List<Student> students = new ArrayList<>();
            while (n > 0) {
                String line = sc.nextLine();
                String[] parts = line.split("\\s+");
                String id = parts[0];
                List<Integer> grades = Arrays.stream(parts).skip(1).map(Integer::parseInt).collect(Collectors.toList());
                students.add(new Student(id, grades));
                --n;
            }

            if (studentScenario == 1) {
                //TODO: transform all students such that their id is converted into a new format adding the suffix
                // "_FCSE" after each id number

                students = ExecuteAndSort.execute(
                        students, (a) -> {
                            a.id = a.id + "_FCSE";
                            return a;
                        }
                );

                System.out.println(students);

            } else {
                //TODO: transform all students such that their grades are mapped into a new system as follows:
                // 10 -> 1
                // 9 -> 2
                // 8 -> 3
                // 7 -> 4
                // 6 -> 5

                students = ExecuteAndSort.execute(
                        students, (a) -> {
                            a.grades = Student.mapGrades(a.grades);
                            return a;
                        }
                );

                System.out.println(students);

            }
        } else { //integers
            List<Integer> integers = new ArrayList<>();
            while (n > 0) {
                integers.add(Integer.parseInt(sc.nextLine()));
                --n;
            }

            //TODO: transform all integers to be 10 times greater than their original value if their original value
            // was less than 100 or 2 times greater otherwise

            integers = ExecuteAndSort.execute(integers, (integer) ->{
               if(integer<100){
                   integer = integer*100;
               }
               else integer = integer*2;
               return integer;
            });
            System.out.println(integers);
        }
    }
}
