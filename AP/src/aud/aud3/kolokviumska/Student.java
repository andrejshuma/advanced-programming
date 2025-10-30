package aud.aud3.kolokviumska;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Student implements Comparable<Student> {
    String id;
    List<Integer> grades;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    public double average() {
        return grades.stream().mapToDouble(i -> i).average().getAsDouble();
    }

    public int getYear() {
        return (24 - Integer.parseInt(id.substring(0, 2)));
    }

    public int totalCourses() {
        return Math.min(getYear() * 10, 40);
    }

    public double labAssistantPoints() {
        return average() * ((double) grades.size() / totalCourses()) * (0.8 + ((getYear() - 1) * 0.2) / 3.0);
    }

    //TODO: implement function
    public static List<Integer> mapGrades(List<Integer> grades) {
        return grades.stream().map(i -> 11 - i).collect(Collectors.toList());
    }

    @Override
    public int compareTo(Student o) {
        return Comparator.comparing(Student::labAssistantPoints)
                .thenComparing(Student::average)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return String.format(
                "Student %s (%d year) - %d/%d passed exam, average grade %.2f.\nLab assistant points: %.2f",
                id,
                getYear(),
                grades.size(),
                totalCourses(),
                average(),
                labAssistantPoints()
        );
    }
}
