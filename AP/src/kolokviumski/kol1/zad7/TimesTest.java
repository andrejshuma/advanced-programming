package kolokviumski.kol1.zad7;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class TimeTable {
    List<Time> times;

    public TimeTable() {
        this.times = new ArrayList<>();
    }

    void readTimes(InputStream inputStream) throws UnsupportedFormatException,InvalidTimeException{
        Scanner scanner = new Scanner(inputStream);

        while(scanner.hasNext()){
            String line = scanner.next();

                if(!line.contains(".") && !line.contains(":")){
                    throw new UnsupportedFormatException(line);
                }

            String[] split;
            if(line.contains(".")){
                split = line.split("\\.");
            }
            else{
                split = line.split(":");
            }
            Integer hours = Integer.parseInt(split[0]);
            Integer minutes = Integer.parseInt(split[1]);


                if(hours < 0 || hours > 23 || minutes<0 || minutes>59){
                    throw new InvalidTimeException(line);
                }

            times.add(new Time(hours,minutes));
        }
    }


    public void writeTimes(PrintStream out, TimeFormat timeFormat) {
        times = times.stream().sorted().collect(Collectors.toList());

        if (timeFormat == TimeFormat.FORMAT_24) {
            times.forEach(Time::print24Hours);
        } else if (timeFormat == TimeFormat.FORMAT_AMPM) {
            times.forEach(Time::printAMPM);
        }
    }
}
class UnsupportedFormatException extends Exception {
    public UnsupportedFormatException(String line) {
        super(line);
    }
}

class InvalidTimeException extends Exception{
    public InvalidTimeException(String line) {
        super(line);
    }
}
class Time implements Comparable<Time>{
    int hours;
    int minutes;

    public Time(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getInMinutes(){
        return hours*60 + minutes;
    }

    @Override
    public int compareTo(Time o) {
        return Integer.compare(this.getInMinutes(),o.getInMinutes());
    }

    public void print24Hours() {
        System.out.printf("%2d:%02d%n", hours, minutes);
    }

    public void printAMPM() {
        String period = hours < 12 ? "AM" : "PM";
        int displayHours = (hours == 0) ? 12 : (hours > 12 ? hours - 12 : hours);
        System.out.printf("%2d:%02d %s%n", displayHours, minutes, period);
    }
}
public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }


}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}
