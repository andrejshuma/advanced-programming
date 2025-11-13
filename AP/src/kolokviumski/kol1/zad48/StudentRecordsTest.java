package kolokviumski.kol1.zad48;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * January 2016 Exam problem 1
 */
class Record implements Comparable<Record>{
    String code;
    List<Integer> grades;
    String group;

    public double getAverageGrade(){
        return grades.stream().mapToDouble(a -> a).average().orElse(5.0);
    }
    public Record(String code, String group,List<Integer> grades) {
        this.code = code;
        this.grades = grades;
        this.group = group;
    }

    @Override
    public int compareTo(Record o) {
        int cmpAvg = Double.compare(o.getAverageGrade(),getAverageGrade());
        if(cmpAvg==0){
            return this.code.compareTo(o.code);
        }
        return cmpAvg;
    }

    @Override
    public String toString() {
        return String.format("%s %.2f",code,getAverageGrade());
    }
}

class StudentRecords{
    private Map<String, Set<Record>> studentRecordsByGroup;

    private static class GroupDistribution implements Comparable<GroupDistribution> {
        String groupName;
        Map<Integer, Integer> gradeCounts;

        public GroupDistribution(String groupName, Set<Record> records) {
            this.groupName = groupName;
            this.gradeCounts = new HashMap<>();

            for (int i = 6; i <= 10; i++) {
                gradeCounts.put(i, 0);
            }

            for (Record record : records) {
                for (int grade : record.grades) {
                    if (grade >= 6 && grade <= 10) {
                        gradeCounts.merge(grade, 1, Integer::sum);
                    }
                }
            }
        }

        @Override
        public int compareTo(GroupDistribution other) {
            int count10 = gradeCounts.getOrDefault(10, 0);
            int otherCount10 = other.gradeCounts.getOrDefault(10, 0);

            return Integer.compare(otherCount10, count10);
        }
    }

    int readRecords(InputStream inputStream){
        Scanner scanner = new Scanner(inputStream);
        int counter =0;
        while (scanner.hasNextLine()){
            counter++;
            String[] parts = scanner.nextLine().split("\\s+");
            String code = parts[0];
            String group = parts[1];
            List<Integer> grades = new ArrayList<>();
            for (int i = 2; i < parts.length; i++) {
                grades.add(Integer.parseInt(parts[i]));
            }

            Record newRecord = new Record(code,group,grades);

            studentRecordsByGroup
                    .computeIfAbsent(group, k -> new TreeSet<>())
                    .add(newRecord);
        }
        return counter;
    }

    void writeTable(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream, true);

        for (String key : studentRecordsByGroup.keySet()) {
            pw.println(key);

            for (Record record : studentRecordsByGroup.get(key)) {
                pw.println(record);
            }
        }
        pw.flush();
    }

    void writeDistribution(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream, true);
        List<GroupDistribution> distributions = new ArrayList<>();

        for (Map.Entry<String, Set<Record>> entry : studentRecordsByGroup.entrySet()) {
            distributions.add(new GroupDistribution(entry.getKey(), entry.getValue()));
        }

        Collections.sort(distributions);

        for (GroupDistribution distribution : distributions) {
            pw.println(distribution.groupName);

            for (int grade = 6; grade <= 10; grade++) {
                int count = distribution.gradeCounts.getOrDefault(grade, 0);

                int asterisksCount = (int) Math.ceil((double) count / 10);
                String asterisks = "*".repeat(asterisksCount);

                pw.printf("%2d | %s(%d)\n", grade, asterisks, count);
            }
        }
        pw.flush();
    }

    public StudentRecords() {
        this.studentRecordsByGroup = new TreeMap<>();
    }
}
public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}