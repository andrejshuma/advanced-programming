package labs.lab5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;


class Book implements Comparable<Book>{
    private final String isbn;
    private final String title;
    private final String author;
    private final int year;
    private int copies = 0;
    private int totalBorrows = 0;
    private Set<Member> currBorrowers;
    private Queue<Member> waitingList;

    public Book(String isbn, String title, String author, int year) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
        waitingList = new LinkedList<>();
        currBorrowers = new TreeSet<>(Comparator.comparing(Member::getId));
    }

    public void addCopy(){copies++;}

    public void borrowCopy(Member member){
        if (copies == 0) waitingList.add(member);
        else {
            copies--;
            totalBorrows++;
            currBorrowers.add(member);
            member.addBook(this);
        }
    }

    public int getTotalBorrows() {
        return totalBorrows;
    }

    public void returnCopy(Member member) {
        member.removeBook(this);
        currBorrowers.remove(member);
        if (!waitingList.isEmpty()){
            Member m = waitingList.remove();
            m.addBook(this);
            currBorrowers.add(m);
            totalBorrows++;
        } else copies++;
    }

    @Override
    public String toString(){
        return String.format("%s - \"%s\" by %s (%d), available: %d, total borrows: %d",
                isbn, title, author, year, copies, totalBorrows);
    }

    public Set<Member> getCurrBorrowers(){
        return currBorrowers;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public Queue<Member> getWaitingList() {
        return waitingList;
    }

    @Override
    public int compareTo(Book o) {
        int diff = o.totalBorrows - this.totalBorrows;
        return diff != 0 ? diff : this.year - o.year;
    }
}

class Member implements Comparable<Member> {
    private String id;
    private String name;
    private int totalBorrows = 0;
    private Set<Book> books;

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
        books = new HashSet<>();
    }

    public void addBook(Book b){
        books.add(b);
        totalBorrows++;
    }

    public void removeBook(Book b){
        books.remove(b);
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString(){
        return String.format("%s (%s) - borrowed now: %d, total borrows: %d",
                name, id, books.size(), totalBorrows);
    }

    @Override
    public int compareTo(Member o) {
        int diff = o.books.size() - this.books.size();
        return diff != 0 ? diff : this.name.compareTo(o.name);
    }
}

class LibrarySystem {
    String name;
    Map<String, Member> members;
    Map<String, Book> books;

    public LibrarySystem(String name) {
        this.name = name;
        members = new HashMap<>();
        books = new HashMap<>();
    }

    void registerMember(String id, String fullName){
        members.put(id, new Member(id, fullName));
    }

    void addBook(String isbn, String title, String author, int year){
        books.putIfAbsent(isbn, new Book(isbn, title, author, year));
        Book book = books.get(isbn);
        book.addCopy();
    }

    void borrowBook(String memberId, String isbn){
        Book b = books.get(isbn);
        if (b == null) return;
        b.borrowCopy(members.get(memberId));
    }

    void returnBook(String memberId, String isbn){
        Book b = books.get(isbn);
        if (b == null) return;
        b.returnCopy(members.get(memberId));
    }

    void printMembers(){
        members.values().stream()
                .sorted()
                .forEach(System.out::println);
    }

    void printBooks(){
        books.values().stream()
                .sorted()
                .forEach(System.out::println);
    }

    void printBookCurrentBorrowers(String isbn){
        if (books.containsKey(isbn)) {
            String s = books.get(isbn).getCurrBorrowers()
                    .stream().map(Member::getId)
                    .sorted()
                    .collect(Collectors.toCollection(ArrayList::new))
                    .toString();
            System.out.println(s.substring(1, s.length() - 1));
        }
    }

    void printTopAuthors(){
        Map<String, Integer> collect = books.values().stream()
                .collect(Collectors.groupingBy(
                        Book::getAuthor,
                        Collectors.summingInt(Book::getTotalBorrows))
                );
        collect.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .map(e -> e.getKey() + " - " + e.getValue())
                .forEach(System.out::println);
    }

//    public Map<String, Book> getTopBookPerAuthor() {
//        return books.values().stream()
//                .collect(Collectors.groupingBy(
//                        Book::getAuthor,
//                        Collectors.collectingAndThen(
//                                Collectors.maxBy(Comparator.comparingInt(Book::getTotalBorrows)),
//                                optionalBook -> optionalBook.orElse(null)
//                        )
//                ));
//    }
//
//    public Map<String, Integer> getBooksWaitingListSize() {
//        return books.values().stream()
//                .collect(Collectors.toMap(
//                        Book::getIsbn,
//                        b -> b.getWaitingList().size()
//                ))
//                .entrySet().stream()
//                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
//                        .thenComparing(Map.Entry.comparingByKey()))
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (a, b) -> a,
//                        LinkedHashMap::new
//                ));
//    }
}

public class LibraryTester {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            String libraryName = br.readLine();
            //   System.out.println(libraryName); //test
            if (libraryName == null) return;

            libraryName = libraryName.trim();
            LibrarySystem lib = new LibrarySystem(libraryName);

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("END")) break;
                if (line.isEmpty()) continue;

                String[] parts = line.split(" ");

                switch (parts[0]) {

                    case "registerMember": {
                        lib.registerMember(parts[1], parts[2]);
                        break;
                    }

                    case "addBook": {
                        String isbn = parts[1];
                        String title = parts[2];
                        String author = parts[3];
                        int year = Integer.parseInt(parts[4]);
                        lib.addBook(isbn, title, author, year);
                        break;
                    }

                    case "borrowBook": {
                        lib.borrowBook(parts[1], parts[2]);
                        break;
                    }

                    case "returnBook": {
                        lib.returnBook(parts[1], parts[2]);
                        break;
                    }

                    case "printMembers": {
                        lib.printMembers();
                        break;
                    }

                    case "printBooks": {
                        lib.printBooks();
                        break;
                    }

                    case "printBookCurrentBorrowers": {
                        lib.printBookCurrentBorrowers(parts[1]);
                        break;
                    }

                    case "printTopAuthors": {
                        lib.printTopAuthors();
                        break;
                    }

                    default:
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}