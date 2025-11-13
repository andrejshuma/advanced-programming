package kolokviumski.kol1.zad23;

import java.util.*;

class Applicant implements Comparable<Applicant> {
    private final String code;
    private final String name;
    private final int age;

    public String getName() { return name; }

    public Applicant(String code, String name, int age) {
        this.code = code;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }

    @Override
    public int compareTo(Applicant o) {
        int cmp = this.name.compareTo(o.name);
        if (cmp != 0) return cmp;
        return Integer.compare(this.age, o.age);
    }
}

class Audition {
    private final Map<String, Map<String, Applicant>> applicantsByCity;

    public Audition() {
        this.applicantsByCity = new TreeMap<>();
    }

    void addParticipant(String city, String code, String name, int age) {
        applicantsByCity.computeIfAbsent(city,k -> new HashMap<>()).putIfAbsent(code, new Applicant(code, name, age));
    }

    void listByCity(String city) {
        Map<String, Applicant> cityApplicants = applicantsByCity.get(city);
        if (cityApplicants == null) return;

        cityApplicants.values().stream()
                .sorted()
                .forEach(System.out::println);
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticipant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++%n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}
