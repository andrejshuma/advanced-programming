package kolokviumski.kol1.zad17;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class Article{
    private final String taxType;
    private final int price;
    private static final double DDV = 0.15;

    public Article(String taxType, int price) {
        this.taxType = taxType;
        this.price = price;
    }

    public String getTaxType() {
        return taxType;
    }

    public double taxReturn() {
        if (taxType.equalsIgnoreCase("A"))
            return DDV * 0.18 * price;
        else if (taxType.equalsIgnoreCase("B"))
            return DDV * 0.05 * price;
        else return 0;
    }

    public int getPrice() {
        return price;
    }
}
class User{
    long id;
    List<Article> articles;

    public User(long id,ArrayList<Article> articles) {
        this.id = id;
        this.articles = articles;
    }
    public Integer getSumPrices(){
        return  articles.stream().mapToInt(Article::getPrice).sum();
    }

    public Double getTaxReturn(){
        return articles.stream().mapToDouble(Article::taxReturn).sum() ;
    }

    @Override
    public String toString() {
        return String.format("%10d\t %9d\t%10.5f", id, getSumPrices(), getTaxReturn());
    }

    public static User createUser(String line) throws AmountNotAllowedException {
        String[] split = line.split("\\s+");
        Long id = Long.parseLong(split[0]);
        ArrayList<Article> newList = new ArrayList<>();
        for (int i = 1; i < split.length ; i+=2) {

            newList.add(new Article(split[i+1],Integer.parseInt(split[i])));
        }
        if (newList.stream().mapToInt(Article::getPrice).sum()>30000){
            throw new AmountNotAllowedException(newList.stream().mapToInt(Article::getPrice).sum());
        }
        return new User(id,newList);
    }
}
class MojDDV{
    List<User> users;

    public MojDDV() {
        this.users = new ArrayList<>();
    }

    void readRecords (InputStream inputStream){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        this.users = bufferedReader.lines()
                .map(line -> {
                    try {
                        return User.createUser(line);
                    } catch (AmountNotAllowedException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    void printTaxReturns (OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);
        users.forEach(user -> pw.println(user.toString()));
        pw.flush();
    }

    public Double findMin() {
        return users.stream()
                .mapToDouble(User::getTaxReturn)
                .min()
                .orElse(0.0);
    }

    public Double findMax() {
        return users.stream()
                .mapToDouble(User::getTaxReturn)
                .max()
                .orElse(0.0);
    }

    public Double findSum(){
        return users.stream().mapToDouble(user -> user.articles.stream().mapToDouble(Article::taxReturn).sum()).sum();
    }

    public Long findCount(){
        return users.stream().mapToDouble(user -> user.articles.stream().mapToDouble(Article::taxReturn).count()).count();
    }

    public Double findAverage(){
        return users.stream().mapToDouble(user -> user.articles.stream().mapToDouble(Article::taxReturn).sum()).average().orElse(0.0);
    }
    void printStatistics(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
            pw.println(String.format("min:\t%.3f", findMin()));
            pw.println(String.format("max:\t%.3f", findMax()));
            pw.println(String.format("sum:\t%.3f", findSum()));
            pw.println(String.format("count:\t%d", findCount()));
            pw.println(String.format("avg:\t%.3f", findAverage()));

        pw.flush();
    }
}

class AmountNotAllowedException extends RuntimeException{
    public AmountNotAllowedException(int sum) {
        super("Receipt with amount "+sum+" is not allowed to be scanned");
    }
}
public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}