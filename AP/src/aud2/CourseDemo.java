package aud2;

import java.util.Scanner;
import java.util.function.Predicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.Consumer;

class Student {
    private final String index;  // e.g., 221234
    private String name;
    private int grade;           // 5..10 (5 = fail)
    private int attendance;      // 0..100 (%)

    public Student(String index, String name, int grade, int attendance) {
        this.index = index;
        this.name = name;
        this.grade = grade;
        this.attendance = attendance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " (" + index + "), grade=" + grade + ", attendance=" + attendance + "%";
    }

    public int getGrade() {
        return grade;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setGrade(int grade) {
        if (grade > 10) {
            grade = 10;
        }
        if (grade < 5) {
            grade = 5;
        }
        this.grade = grade;
    }
}

class Course {
    private final String title;
    private final Student[] students;
    private int size = 0;

    public Course(String title, int capacity) {
        this.title = title;
        this.students = new Student[capacity];
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return students.length;
    }

    /**
     * Add a student provided by a Supplier. Demonstrates Supplier<T>.
     */
    public boolean enroll(Supplier<Student> supplier) {
        if (size >= students.length) {
            return false;
        }
        students[size++] = supplier.get();
        return true;
    }

    /**
     * Apply a Consumer to each student (side effects allowed, e.g., print or mutate).
     */
    public void forEach(Consumer<Student> action) {
        for (int i = 0; i < size; i++) {
            action.accept(students[i]);
        }
    }

    /**
     * Count students satisfying a Predicate.
     */
    public int count(Predicate<Student> predicate) {
        int c = 0;
        for (int i = 0; i < size; i++) {
            if (predicate.test(students[i])) {
                c++;
            }
        }
        return c;
    }

    /**
     * Find first student that matches; returns null if none.
     */
    public Student findFirst(Predicate<Student> predicate) {
        for (int i = 0; i < size; i++) {
            if (predicate.test(students[i])) {
                return students[i];
            }
        }
        return null;
    }

    /**
     * Filter students into a NEW array (still no collections).
     */
    public Student[] filter(Predicate<Student> predicate) {
        // 1st pass: count matches to size array exactly
        int matches = count(predicate);
        Student[] out = new Student[matches];
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (predicate.test(students[i])) {
                out[j++] = students[i];
            }
        }
        return out;
    }

    /**
     * Map students to Strings (labels) with a Function.
     * (We return String[] to avoid generics + array creation complexity.)
     */
    public String[] mapToLabels(Function<Student, String> mapper) {
        String[] out = new String[size];
        for (int i = 0; i < size; i++) {
            out[i] = mapper.apply(students[i]);
        }
        return out;
    }

    /**
     * In-place update using a Consumer (mutation allowed).
     * Example: curve grades +1, cap at 10.
     */
    public void mutate(Consumer<Student> mutator) {
        for (int i = 0; i < size; i++) {
            mutator.accept(students[i]);
        }
    }

    public void conditionalMutate(Predicate<Student> condition, Consumer<Student> mutator) {
        for (int i = 0; i < size; i++) {
            if (condition.test(students[i])) {
                mutator.accept(students[i]);
            }
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Course: " + title + " (" + size + "/" + students.length + " students)");
        for (Student student : students) {
            sb.append(student.toString()).append("\n");
        }
        return sb.toString();

    }
}


public class CourseDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Course se = new Course("Software Engineering", 10);

        int n = sc.nextInt();

        // Supplier that reads one student per line
        Supplier<Student> studentFromInput = () -> {
            System.out.print("Enter student (index name grade attendance): ");
            String index = sc.next();
            String name = sc.next();
            int grade = sc.nextInt();
            int attendance = sc.nextInt();
            sc.nextLine(); // consume leftover newline
            return new Student(index, name, grade, attendance);
        };

        // Enroll n students using the supplier
        for (int i = 0; i < n; i++) {
            se.enroll(studentFromInput);
        }

        sc.close(); // close scanner after done
        System.out.println("\nEnrolled students:");
        se.forEach(System.out::println);

        // --- Print all enrolled students using Consumer + forEach ---
        System.out.println("\n=== All Students ===");
        Consumer<Student> printer = System.out::println;
        se.forEach(printer);

        // --- Use Predicate to filter passing students ---
        Predicate<Student> isPassing = s -> s.getGrade() >= 6;
        Predicate<Student> goodAttendance = s -> s.getAttendance() >= 70;
        Predicate<Student> passingAndPresent = isPassing.and(goodAttendance);

        System.out.println("\n=== Students with passing grade and good attendance ===");
        Student[] passing = se.filter(passingAndPresent);
        for (Student s : passing) System.out.println(s);

        // --- Find first student with grade >= 9 ---
        System.out.println("\n=== First honor student (grade >= 9) ===");
        Student honor = se.findFirst(s -> s.getGrade() >= 9);
        System.out.println(honor != null ? honor : "None found");

        // --- Mutate: curve all grades by +1 (max 10) ---
        System.out.println("\n=== Curving all grades by +1 (max 10) ===");
        Consumer<Student> curve = s -> s.setGrade(s.getGrade() + 1);
        se.mutate(curve);
        se.forEach(printer);

        // --- Conditional mutation: if attendance is above 90%, award +1 grade to student;
        System.out.println("\n=== Curving high attendance students' grades by +1 ===");

        se.conditionalMutate(
                s -> s.getAttendance()>=90,
                s -> s.setGrade(s.getGrade()+1)
        );
    }
}