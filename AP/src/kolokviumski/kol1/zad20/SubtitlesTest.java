package kolokviumski.kol1.zad20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Subtitles{
    private List<Line> lines;

    public Subtitles() {
        this.lines = new ArrayList<>();
    }

    public int loadSubtitles(InputStream in) {
        Scanner sc = new Scanner(in);
        List<String> readSubtitle = new ArrayList<>();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.isEmpty()) {
                readSubtitle.add(line);
            } else {
                Line s = Line.createSubtitle(readSubtitle);
                lines.add(s);
                readSubtitle = new ArrayList<>();
            }
        }

        if (!readSubtitle.isEmpty()) {
            Line s = Line.createSubtitle(readSubtitle);
            lines.add(s);
        }

        return lines.size();
    }


    public void print(){
        lines.forEach(System.out::println);
    }
    public void shift(int ms){
        for (Line line : lines) {
            line.setTimes(line.getStart().plusNanos(ms* 1000000L),line.getEnd().plusNanos(ms* 1000000L));
        }
    }
}

class Line implements Comparable<Line>{
    private int pos;
    private LocalTime start;
    private LocalTime end;
    private String content;
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
    public Line(int id, LocalTime start, LocalTime end, String text) {
        this.pos = id;
        this.start = start;
        this.end = end;
        this.content = text;
    }

    public static Line createSubtitle(List<String> input) {
        int id = Integer.parseInt(input.get(0));
        String[] times = input.get(1).split("\\s+-->\\s+");
        LocalTime start = LocalTime.parse(times[0], dtf);
        LocalTime end = LocalTime.parse(times[1], dtf);

        String text = input.stream()
                .skip(2)
                .collect(Collectors.joining("\n"));

        return new Line(id, start, end, text);
    }
    public void setTimes (LocalTime start, LocalTime end){
        this.start=start;
        this.end=end;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    @Override
    public String toString() {
        String startTime = dtf.format(start);
        String endTime = dtf.format(end);
        return String.format("%d\n%s --> %s\n%s\n", pos, startTime, endTime, content);
    }


    @Override
    public int compareTo(Line o) {
        return Integer.compare(this.pos,o.pos);
    }
}
public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

// Вашиот код овде

