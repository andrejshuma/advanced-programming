package kolokviumski.kol1.zad41;

import java.util.*;

public class MapSortingTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        List<String> l = readMapPairs(scanner);
        if(n==1){
            Map<String, Integer> map = new HashMap<>();
            fillStringIntegerMap(l, map);
            SortedSet<Map.Entry<String, Integer>> s = entriesSortedByValues(map);
            System.out.println(s);
        } else {
            Map<Integer, String> map2 = new HashMap<>();
            fillIntegerStringMap(l, map2);
            SortedSet<Map.Entry<Integer, String>> s = entriesSortedByValues(map2);
            System.out.println(s);
        }

    }

    private static List<String> readMapPairs(Scanner scanner) {
        String line = scanner.nextLine();
        String[] entries = line.split("\\s+");
        return Arrays.asList(entries);
    }

    static void fillStringIntegerMap(List<String> l, Map<String,Integer> map) {
        l.stream()
                .forEach(s -> map.put(s.substring(0, s.indexOf(':')), Integer.parseInt(s.substring(s.indexOf(':') + 1))));
    }

    static void fillIntegerStringMap(List<String> l, Map<Integer, String> map) {
        l.stream()
                .forEach(s -> map.put(Integer.parseInt(s.substring(0, s.indexOf(':'))), s.substring(s.indexOf(':') + 1)));
    }

    public static <T,U extends Comparable<U>> SortedSet<Map.Entry<T,U >> entriesSortedByValues(Map<T, U> map){
        System.out.println(map);
        SortedSet<Map.Entry<T,U >> sortedSet= new TreeSet<>((e1,e2) -> {
            int cmp = e2.getValue().compareTo(e1.getValue());
            if(cmp!=0){
                return cmp;
            }
            return 1;
        });
        sortedSet.addAll(map.entrySet());
        return sortedSet;
    }

}