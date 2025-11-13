package kolokviumski.kol1.zad22;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class F1Race {
    List<Driver> drivers;

    public F1Race() {
        this.drivers = new ArrayList<>();
    }

    void readResults(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split("\\s+");
            String name = parts[0];
            List<Duration> laps = new ArrayList<>();
            for (int i = 1; i < parts.length; i++) {
                String[] t = parts[i].split(":");
                int minutes = Integer.parseInt(t[0]);
                int seconds = Integer.parseInt(t[1]);
                int millis = Integer.parseInt(t[2]);
                laps.add(Duration.ofMinutes(minutes)
                        .plusSeconds(seconds)
                        .plusMillis(millis));
            }
            drivers.add(new Driver(name, laps));
        }
    }

    void printSorted(OutputStream outputStream) {
        drivers.sort(Driver::compareTo);
        for (int i = 0; i < drivers.size(); i++) {
            Driver driver = drivers.get(i);
            Duration best = driver.getBestLap();
            long minutes = best.toMinutes();
            long seconds = best.minusMinutes(minutes).getSeconds();
            long millis = best.minusMinutes(minutes).minusSeconds(seconds).toMillis();

            String formattedTime = String.format("%d:%02d:%03d", minutes, seconds, millis);
            System.out.printf("%d. %-10s%10s%n", i+1, driver.getName(), formattedTime);
        }
    }
}

class Driver implements Comparable<Driver> {
    private String name;
    private List<Duration> laps;

    public Driver(String name, List<Duration> laps) {
        this.name = name;
        this.laps = laps;
    }

    public String getName() {
        return name;
    }

    public Duration getBestLap() {
        return laps.stream().min(Duration::compareTo).orElse(Duration.ZERO);
    }

    @Override
    public int compareTo(Driver o) {
        return getBestLap().compareTo(o.getBestLap());
    }
}

public class F1Test {
    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }
}
