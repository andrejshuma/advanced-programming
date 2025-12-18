package labs.lab7;



import java.util.*;
import java.util.concurrent.*;

public class TextCounter {

    public static class Counter {
        public final int textId;
        public final int lines;
        public final int words;
        public final int chars;

        public Counter(int textId, int lines, int words, int chars) {
            this.textId = textId;
            this.lines = lines;
            this.words = words;
            this.chars = chars;
        }

        @Override
        public String toString() {
            return "Counter{" +
                    "textId=" + textId +
                    ", lines=" + lines +
                    ", words=" + words +
                    ", chars=" + chars +
                    '}';
        }
    }

    public static Callable<Counter> getTextCounter(int textId, String text) {
        return () -> {
            int lines = text.isEmpty() ? 0 : text.split("\n", -1).length;

            int words = text.trim().isEmpty()
                    ? 0
                    : text.trim().split("\\s+").length;

            int chars = text.length();

            return new Counter(textId, lines, words, chars);
        };
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        sc.nextLine();

        List<Callable<Counter>> tasks = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int textId = sc.nextInt();
            sc.nextLine();

            int lines = sc.nextInt();
            sc.nextLine();

            StringBuilder text = new StringBuilder();
            for (int j = 0; j < lines; j++) {
                text.append(sc.nextLine());
                if (j < lines - 1) {
                    text.append("\n");
                }
            }

            tasks.add(getTextCounter(textId, text.toString()));
        }

        ExecutorService executor =
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future<Counter>> futures = executor.invokeAll(tasks);

        List<Counter> results = new ArrayList<>();

        for (Future<Counter> future : futures) {
            results.add(future.get());
        }

        executor.shutdown();

        results.sort(Comparator.comparingInt(c -> c.textId));

        for (Counter c : results) {
            System.out.printf(
                    "%d %d %d %d%n",
                    c.textId, c.lines, c.words, c.chars
            );
        }
    }
}
