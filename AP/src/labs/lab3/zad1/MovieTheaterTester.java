package labs.lab3.zad1;

import java.io.*;
import java.util.*;

class Movie{
    String title;
    String genre;
    int year;

    List<Integer> ratings;
    double avgRating;

    public Movie(String title, String genre, int year, String ratings) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.ratings = new ArrayList<>();
        for (String rating : ratings.split(" ")) {
            this.ratings.add(Integer.parseInt(rating));
        }
        this.avgRating = this.ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %d, %.2f", title, genre, year, avgRating);
    }
}

class MovieTheater{
    List<Movie> movies;

    public MovieTheater() {
        this.movies = new ArrayList<>();
    }

    public void readMovies(InputStream in) throws IOException {
        Scanner scanner = new Scanner(in);
        int n = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i <n ; i++) {
            String title = scanner.nextLine();
            String genre = scanner.nextLine();
            int year = Integer.parseInt(scanner.nextLine());
            String ratings = scanner.nextLine();
            movies.add(new Movie(title, genre, year, ratings));
        }
    }
    public void printList(List<Movie> sorted){
        for (Movie movie : sorted) {
            System.out.println(movie.toString());
        }
    }
    public void printByRatingAndTitle() {
        List<Movie> sorted = new ArrayList<>(movies);
        sorted.sort(new MovieRatingAndTitleComparable());
        Collections.reverse(sorted);
        printList(sorted);
    }

    public void printByGenreAndTitle() {
        List<Movie> sorted = new ArrayList<>(movies);
        sorted.sort(Comparator.comparing((Movie m) -> m.genre)
                .thenComparing(m -> m.title));
        printList(sorted);
    }

    public void printByYearAndTitle() {
        List<Movie> sorted = new ArrayList<>(movies);
        sorted.sort(Comparator.comparingInt((Movie m) -> m.year).thenComparing(m -> m.title));
        printList(sorted);
    }
}

class MovieRatingAndTitleComparable implements Comparator<Movie>{

    @Override
    public int compare(Movie o1, Movie o2) {
        if(Double.compare(o1.avgRating,o2.avgRating)==0){
            return o2.title.compareTo(o1.title);
        }
        else return Double.compare(o1.avgRating,o2.avgRating);
    }
}
public class MovieTheaterTester {
    public static void main(String[] args) {
        MovieTheater mt = new MovieTheater();
        try {
            mt.readMovies(System.in);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("SORTING BY RATING");
        mt.printByRatingAndTitle();
        System.out.println("\nSORTING BY GENRE");
        mt.printByGenreAndTitle();
        System.out.println("\nSORTING BY YEAR");
        mt.printByYearAndTitle();
    }
}