package labs.lab5;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        List<String> words = br.lines().collect(Collectors.toCollection(ArrayList::new));
        Map<String, List<String>> anagrams = new LinkedHashMap<>();
        for (String word : words) {
            char[] charArray = word.toCharArray();
            Arrays.sort(charArray);
            String key = new String(charArray);
            anagrams.putIfAbsent(key, new ArrayList<>());
            anagrams.get(key).add(word);
        }
        anagrams.values().stream()
                .filter(v -> v.size() > 4)
                .forEach(v -> System.out.println(String.join(" ", v)));
    }
}