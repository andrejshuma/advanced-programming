package kolokviumski.kol1.zad15;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

class Weather implements Comparable<Weather>{
     private double temperature;
    private double humidity;
    private double wind;
    private double visibility;
    private Date timestamp;

    public Weather(double temperature, double humidity, double wind, double visibility, Date timestamp) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.wind = wind;
        this.visibility = visibility;
        this.timestamp = timestamp;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    public double getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(Weather o) {
        return this.getTimestamp().compareTo(o.getTimestamp());
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s", temperature, wind, humidity, visibility
                , df.format(timestamp));
    }
}
class WeatherStation{
    int days;
    List<Weather> measurements;

    public WeatherStation(int days){
        this.days = days;
        this.measurements = new ArrayList<Weather>();
    }

    public void deleteDates(Date date) {
        long maxDays = days * 24L * 60 * 60 * 1000;
        for (int i = measurements.size() - 1; i >= 0; i--) {
            if (date.getTime() - measurements.get(i).getTimestamp().getTime() > maxDays)
                measurements.remove(i);
        }
    }

    public boolean isValidDate(Date date) {
        return measurements.stream()
                .noneMatch(cond -> date.getTime() - cond.getTimestamp().getTime() <= 2.5 * 60 * 1000);
    }

    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date){
        if(isValidDate(date)){
            deleteDates(date);
            Weather weather = new Weather(temperature, humidity, wind, visibility, date);
            measurements.add(weather);
        }
    }
    public int total(){
        return measurements.size();
    }

    public void status(Date from, Date to){
        List<Weather> filtered = measurements.stream()
                .filter(m -> m.getTimestamp().compareTo(from) >= 0 && m.getTimestamp().compareTo(to) <= 0)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        if(filtered.isEmpty()){
            throw new RuntimeException();
        }
        else{
            filtered.forEach(System.out::println);
            System.out.printf("Average temperature: %.2f",filtered.stream().mapToDouble(Weather::getTemperature).sum()/(double) filtered.size());
        }
    }
}
public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

// vashiot kod ovde