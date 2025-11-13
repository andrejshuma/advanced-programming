package labs.lab3.zad2;



import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Ad implements Comparable<Ad>{
    String id;
    String category;
    Double bidValue;
    Double ctr;
    String content;
    Double totalScore;

    public Ad(String id, String category, Double bidValue, Double ctr, String content) {
        this.id = id;
        this.category = category;
        this.bidValue = bidValue;
        this.ctr = ctr;
        this.content = content;
    }

    public void setTotalScore(Double totalScore){
        this.totalScore = totalScore;
    }

    public Double getTotalScore(){
        return totalScore;
    }

    @Override
    public String toString() {
        // Формат: ID CATEGORY (bid=..., ctr=...%) CONTENT
        return String.format("%s %s (bid=%.2f, ctr=%.2f%%) %s" ,id,category,bidValue,ctr * 100,content);
    }

    public String getCategory() {
        return category;
    }
    public String getContent() {
        return content;
    }
    public Double getBidValue() {
        return bidValue;
    }

    @Override
    public int compareTo(Ad o) {
        int bidCmp = o.bidValue.compareTo(this.bidValue);
        if (bidCmp != 0) {
            return bidCmp;
        }
        return this.id.compareTo(o.id);
    }
}

class AdRequest{
    String id;
    String category;
    Double floorBid;
    String keywords;

    public AdRequest(String id, String category, Double floorBid, String keywords) {
        this.id = id;
        this.category = category;
        this.floorBid = floorBid;
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return String.format("%s [%s] (floor=%.2f): %s", id, category,floorBid,keywords);
    }

    public String getCategory() {
        return category;
    }

    public Double getFloorBid() {
        return floorBid;
    }

    public String getKeywords() {
        return keywords;
    }
}

class AdNetwork {
    ArrayList<Ad> ads;

    private static final double X = 5.0;
    private static final double Y = 100.0;

    public AdNetwork() {
        this.ads = new ArrayList<>();
    }

    void readAds(BufferedReader in){
        try {
            String line;
            while ((line = in.readLine()) != null && !line.trim().isEmpty()) {
                String[] parts = line.split("\\s+");
                if (parts.length < 5) continue;

                String id = parts[0];
                String category = parts[1];
                Double bidValue = Double.parseDouble(parts[2]);
                Double ctr = Double.parseDouble(parts[3]);

                StringBuilder content = new StringBuilder();
                for (int i = 4; i < parts.length ; i++) {
                    content.append(parts[i]);
                    if(i != parts.length - 1){
                        content.append(" ");
                    }
                }
                Ad ad = new Ad(id, category, bidValue, ctr, content.toString());
                ads.add(ad);
            }
        } catch (IOException | NumberFormatException e) {

            // e.printStackTrace();
        }
    }

    List<Ad> placeAds(BufferedReader inputStream, int k, PrintWriter pw) throws IOException {
        String line = inputStream.readLine();
        String[] parts = line.split("\\s+");

        String id = parts[0];
        String category = parts[1];
        Double floorBid = Double.parseDouble(parts[2]);

        StringBuilder keywords = new StringBuilder();
        for (int i = 3; i < parts.length; i++) {
            keywords.append(parts[i]);
            if (i != parts.length - 1) {
                keywords.append(" ");
            }
        }
        AdRequest adRequest = new AdRequest(id, category, floorBid, keywords.toString());

        List<Ad> filtered = ads.stream()
                .filter(a -> a.getBidValue() >= adRequest.getFloorBid())
                .collect(Collectors.toList());

        filtered.forEach(a -> a.setTotalScore(calculateTotalScore(a, adRequest)));

        List<Ad> topK = filtered.stream()
                .sorted(Comparator.comparing(Ad::getTotalScore).reversed())
                .limit(k)
                .collect(Collectors.toList());

        topK.sort(Comparator.naturalOrder());

        pw.println("Top ads for request " + adRequest.id + ":");
        for (Ad ad : topK) {
            pw.println(ad.toString());
        }

        return topK;
    }

    private Double calculateTotalScore(Ad ad, AdRequest request) {
        return (double) relevanceScore(ad, request) + X * ad.bidValue + Y * ad.ctr;
    }

    private int relevanceScore(Ad ad, AdRequest req) {
        int score = 0;
        if (ad.getCategory().equalsIgnoreCase(req.getCategory())) score += 10;
        String[] adWords = ad.getContent().toLowerCase().split("\\s+");
        String[] keywords = req.getKeywords().toLowerCase().split("\\s+");
        for (String kw : keywords) {
            for (String aw : adWords) {
                if (kw.equals(aw)) score++;
            }
        }
        return score;
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        AdNetwork network = new AdNetwork();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));

        int k = Integer.parseInt(br.readLine().trim());

        if (k == 0) {
            network.readAds(br);
            network.placeAds(br, 1, pw);
        } else if (k == 1) {
            network.readAds(br);
            network.placeAds(br, 3, pw);
        } else {
            network.readAds(br);
            network.placeAds(br, 8, pw);
        }

        pw.flush();
    }
}
