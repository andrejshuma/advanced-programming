package labs.lab4.zad2;



import java.sql.Struct;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


class AverageGradeComparator implements Comparator<Student>{

    @Override
    public int compare(Student o1, Student o2) {
        int rez=Double.compare(o2.getAverageGrade(),o1.getAverageGrade());
        if (rez==0){
            rez=Integer.compare(o2.getPassed(),o1.getPassed());
        }
        if (rez==0){
            rez=o2.id.compareTo(o1.id);
        }
        return rez;
    }
}

class PassedComparatorimplements implements Comparator<Student> {

    @Override
    public int compare(Student o1, Student o2) {
        int rez=Integer.compare(o2.getPassed(),o1.getPassed());
        if (rez==0){
            rez=Double.compare(o2.getAverageGrade(),o1.getAverageGrade());
        }
        if (rez==0){
            rez=o2.id.compareTo(o1.id);
        }
        return rez;
    }
}


class Student {
    String id;
    List<Integer>grades;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", grades=" + grades +
                '}';
    }

    public void addGrade(int grade){
        this.grades.add(grade);
    }

    public double getAverageGrade(){
        return grades.stream().mapToDouble(Integer::doubleValue).average().orElse(0);
    }

    public int getPassed(){
        return (int) grades.stream().filter(g->g>=6).count();
    }

}

class StudentExistsError extends RuntimeException{
    public StudentExistsError(String id){
        super(String.format("Student with ID %s already exists",id));
    }
}

class Faculty{
    HashMap<String,Student>students;

    public Faculty(){
        this.students=new HashMap<>();
    }

    public void addStudent(String id, List<Integer> grades){
        Student s=new Student(id,grades);
        if (students.get(id)!=null){
            throw new StudentExistsError(id);
        }
        students.put(id,s);
    }

    public void addGrade(String id,int grade){
        students.get(id).addGrade(grade);
    }

    public Set<Student>getStudentsSortedByAverageGrade(){
        return students.values().stream()
                .collect(Collectors.toCollection(()->new TreeSet<>(new AverageGradeComparator())));
    }

    public Set<Student>getStudentsSortedByCoursesPassed(){
        return students.values().stream()
                .collect(Collectors.toCollection(()->new TreeSet<>(new PassedComparatorimplements())));
    }


}



public class SetsTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Faculty faculty = new Faculty();

        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String[] tokens = input.split("\\s+");
            String command = tokens[0];

            switch (command) {
                case "addStudent":
                    String id = tokens[1];
                    List<Integer> grades = new ArrayList<>();
                    for (int i = 2; i < tokens.length; i++) {
                        grades.add(Integer.parseInt(tokens[i]));
                    }
                    try {
                        faculty.addStudent(id, grades);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "addGrade":
                    String studentId = tokens[1];
                    int grade = Integer.parseInt(tokens[2]);
                    faculty.addGrade(studentId, grade);
                    break;

                case "getStudentsSortedByAverageGrade":
                    System.out.println("Sorting students by average grade");
                    Set<Student> sortedByAverage = faculty.getStudentsSortedByAverageGrade();
                    for (Student student : sortedByAverage) {
                        System.out.println(student);
                    }
                    break;

                case "getStudentsSortedByCoursesPassed":
                    System.out.println("Sorting students by courses passed");
                    Set<Student> sortedByCourses = faculty.getStudentsSortedByCoursesPassed();
                    for (Student student : sortedByCourses) {
                        System.out.println(student);
                    }
                    break;

                default:
                    break;
            }
        }

        scanner.close();
    }
}
