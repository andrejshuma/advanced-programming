package kolokviumski.kol1.zad49;

import java.util.*;
import java.util.stream.Collectors;



class Names {
    private List<String> names;

    public Names() {
        this.names = new ArrayList<>();
    }

    public void addName(String name) {
        names.add(name);
    }

    public void printN(int n) {
        Map<String, Long> countByName = names.stream()
                .collect(Collectors.groupingBy(
                        name-> name,
                        Collectors.counting()
                ));

        TreeSet<String> sortedNames = new TreeSet<>(countByName.keySet());

        for (String name : sortedNames) {
            long count = countByName.get(name);
            if (count >= n) {
                long uniqueLetters = name.toLowerCase()
                        .chars()
                        .filter(Character::isLetter)
                        .distinct()
                        .count();

                System.out.printf("%s (%d) %d%n", name, count, uniqueLetters);
            }
        }
    }

    public String findName(int len, int x) {
        List<String> filtered = names.stream()
                .filter(n -> n.length() < len)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        if (filtered.isEmpty())
            throw new IllegalArgumentException("No names match the criteria.");

        int index = x % filtered.size();
        return filtered.get(index);
    }
}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }

        int minCount = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====%n", minCount);
        names.printN(minCount);

        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));

        scanner.close();
    }
}
