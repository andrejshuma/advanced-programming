package kolokviumski.kol1.zad50;

import java.util.*;
import java.util.stream.Collectors;

class OperationNotAllowedException extends Exception {
    public OperationNotAllowedException(String message) {
        super(message);
    }
}

class Student {
    String id;
    int yearsOfStudies;
    Map<Integer, Map<String, Integer>> gradesByTerm;

    public Student(String id, int yearsOfStudies) {
        this.id = id;
        this.yearsOfStudies = yearsOfStudies;
        this.gradesByTerm = new TreeMap<>();
    }

    public void addGrade(int term, String courseName, int grade) throws OperationNotAllowedException {
        int maxTerm = yearsOfStudies * 2;
        if (term > maxTerm) {
            throw new OperationNotAllowedException(
                    String.format("Term %d is not possible for student with ID %s", term, id)
            );
        }

        gradesByTerm.putIfAbsent(term, new TreeMap<>());

        Map<String, Integer> termGrades = gradesByTerm.get(term);
        if (termGrades.size() == 3) {
            throw new OperationNotAllowedException(
                    String.format("Student %s already has 3 grades in term %d", id, term)
            );
        }

        termGrades.put(courseName, grade);
    }

    public boolean hasGraduated() {
        int maxTerm = yearsOfStudies * 2;
        if (gradesByTerm.size() < maxTerm) return false;
        for (int i = 1; i <= maxTerm; i++) {
            Map<String, Integer> termGrades = gradesByTerm.get(i);
            if (termGrades == null || termGrades.size() < 3) return false;
        }
        return true;
    }

    public double averageGrade() {
        return gradesByTerm.values()
                .stream()
                .flatMap(m -> m.values().stream())
                .mapToInt(Integer::intValue)
                .average()
                .orElse(5.0);
    }

    public int totalCourses() {
        return gradesByTerm.values().stream().mapToInt(m -> m.size()).sum();
    }

    // 🩵 NEW DETAILED REPORT FORMAT
    public String detailedReport() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Student: %s%n", id));

        int maxTerm = yearsOfStudies * 2;

        for (int term = 1; term <= maxTerm; term++) {
            Map<String, Integer> termCourses = gradesByTerm.get(term);

            if (termCourses == null || termCourses.isEmpty()) {
                sb.append(String.format("Term %d%n", term));
                sb.append("Courses: 0%n");
                sb.append("Average grade for term: 0.00%n");
            } else {
                double avg = termCourses.values().stream().mapToInt(Integer::intValue).average().orElse(0);
                sb.append(String.format("Term %d%n", term));
                sb.append(String.format("Courses: %d%n", termCourses.size()));
                sb.append(String.format("Average grade for term: %.2f%n", avg));
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("Student: %s Courses passed: %d Average grade: %.2f",
                id, totalCourses(), averageGrade());
    }
}

class Faculty {
    Map<String, Student> students;
    List<String> logs;

    public Faculty() {
        this.students = new LinkedHashMap<>();
        this.logs = new ArrayList<>();
    }

    void addStudent(String id, int yearsOfStudies) {
        students.put(id, new Student(id, yearsOfStudies));
    }

    void addGradeToStudent(String studentId, int term, String courseName, int grade)
            throws OperationNotAllowedException {
        Student s = students.get(studentId);
        if (s == null) return;

        s.addGrade(term, courseName, grade);

        if (s.hasGraduated()) {
            logs.add(String.format("Student with ID %s graduated with average grade %.2f in %d years.",
                    s.id, s.averageGrade(),s.yearsOfStudies));
            students.remove(studentId); // 🩵 Required by spec: remove graduated students
        }
    }

    String getFacultyLogs() {
        return String.join("\n", logs);
    }

    String getDetailedReportForStudent(String id) {
        return students.get(id).detailedReport();
    }

    // 🩵 REVERSED ORDER for top students
    void printFirstNStudents(int n) {
        TreeSet<Student> sorted = new TreeSet<>(new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                // Reversed order: weaker students first
                if (s1.totalCourses() != s2.totalCourses())
                    return Integer.compare(s1.totalCourses(), s2.totalCourses()); // ascending
                if (Double.compare(s1.averageGrade(), s2.averageGrade()) != 0)
                    return Double.compare(s1.averageGrade(), s2.averageGrade()); // ascending
                return s1.id.compareTo(s2.id);
            }
        });

        sorted.addAll(students.values());
        int counter = 0;
        for (Student s : sorted) {
            if (counter == n) break;
            System.out.println(s);
            counter++;
        }
    }

    // 🩵 REVERSED ORDER for courses
    void printCourses() {
        Map<String, List<Integer>> courseGrades = new HashMap<>();

        for (Student s : students.values()) {
            for (Map<String, Integer> termMap : s.gradesByTerm.values()) {
                for (Map.Entry<String, Integer> e : termMap.entrySet()) {
                    courseGrades.computeIfAbsent(e.getKey(), k -> new ArrayList<>()).add(e.getValue());
                }
            }
        }

        TreeSet<Map.Entry<String, List<Integer>>> sortedCourses = new TreeSet<>(
                new Comparator<Map.Entry<String, List<Integer>>>() {
                    @Override
                    public int compare(Map.Entry<String, List<Integer>> e1, Map.Entry<String, List<Integer>> e2) {
                        int count1 = e1.getValue().size();
                        int count2 = e2.getValue().size();
                        if (count1 != count2)
                            return Integer.compare(count2, count1);
                        double avg1 = e1.getValue().stream().mapToInt(Integer::intValue).average().orElse(0);
                        double avg2 = e2.getValue().stream().mapToInt(Integer::intValue).average().orElse(0);
                        if (Double.compare(avg1, avg2) != 0)
                            return Double.compare(avg2, avg1);
                        return e1.getKey().compareTo(e2.getKey());
                    }
                });

        sortedCourses.addAll(courseGrades.entrySet());
        List<Map.Entry<String, List<Integer>>> list = new ArrayList<>(sortedCourses);
        Collections.reverse(list); // 🩵 Reverse order now

        for (Map.Entry<String, List<Integer>> entry : list) {
            double avg = entry.getValue().stream().mapToInt(Integer::intValue).average().orElse(0);
            System.out.printf("%s %d %.2f%n", entry.getKey(), entry.getValue().size(), avg);
        }
    }
}

public class FacultyTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();

        if (testCase == 1) {
            System.out.println("TESTING addStudent AND printFirstNStudents");
            Faculty faculty = new Faculty();
            for (int i = 0; i < 10; i++) {
                faculty.addStudent("student" + i, (i % 2 == 0) ? 3 : 4);
            }
            faculty.printFirstNStudents(10);

        } else if (testCase == 2) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            try {
                faculty.addGradeToStudent("123", 7, "NP", 10);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
            try {
                faculty.addGradeToStudent("1234", 9, "NP", 8);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        } else if (testCase == 3) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("123", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("1234", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (testCase == 4) {
            System.out.println("Testing addGrade for graduation");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            int counter = 1;
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("123", i, "course" + counter, (i % 2 == 0) ? 7 : 8);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            counter = 1;
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("1234", i, "course" + counter, (j % 2 == 0) ? 7 : 10);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("PRINT STUDENTS (there shouldn't be anything after this line!");
            faculty.printFirstNStudents(2);
        } else if (testCase == 5 || testCase == 6 || testCase == 7) {
            System.out.println("Testing addGrade and printFirstNStudents (not graduated student)");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), i % 5 + 6);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            if (testCase == 5)
                faculty.printFirstNStudents(10);
            else if (testCase == 6)
                faculty.printFirstNStudents(3);
            else
                faculty.printFirstNStudents(20);
        } else if (testCase == 8 || testCase == 9) {
            System.out.println("TESTING DETAILED REPORT");
            Faculty faculty = new Faculty();
            faculty.addStudent("student1", ((testCase == 8) ? 3 : 4));
            int grade = 6;
            int counterCounter = 1;
            for (int i = 1; i < ((testCase == 8) ? 6 : 8); i++) {
                for (int j = 1; j < 3; j++) {
                    try {
                        faculty.addGradeToStudent("student1", i, "course" + counterCounter, grade);
                    } catch (OperationNotAllowedException e) {
                        e.printStackTrace();
                    }
                    grade++;
                    if (grade == 10)
                        grade = 5;
                    ++counterCounter;
                }
            }
            System.out.println(faculty.getDetailedReportForStudent("student1"));
        } else if (testCase==10) {
            System.out.println("TESTING PRINT COURSES");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            faculty.printCourses();
        } else if (testCase==11) {
            System.out.println("INTEGRATION TEST");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 2 : 3); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }

            }

            for (int i=11;i<15;i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= 3; k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("DETAILED REPORT FOR STUDENT");
            System.out.println(faculty.getDetailedReportForStudent("student2"));
            try {
                System.out.println(faculty.getDetailedReportForStudent("student11"));
                System.out.println("The graduated students should be deleted!!!");
            } catch (NullPointerException e) {
                System.out.println("The graduated students are really deleted");
            }
            System.out.println("FIRST N STUDENTS");
            faculty.printFirstNStudents(10);
            System.out.println("COURSES");
            faculty.printCourses();
        }
    }
}
