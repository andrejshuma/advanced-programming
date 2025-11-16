package kolokviumski.kol1.zad47;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(message);
    }
}


class Product {
    private String id;
    private String name;
    private LocalDateTime createdAt;
    private double price;

    private String category; // Додадена категорија
    private int soldQuantity; // Додадена продадена количина

    public Product(String id, String category, String name, LocalDateTime createdAt, double price) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
        this.category = category;
        this.soldQuantity = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCategory() {
        return category;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void incrementSoldQuantity(int quantity) {
        this.soldQuantity += quantity;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", quantitySold=" + soldQuantity  +
                '}';
    }
}


class OnlineShop {
    private Map<String, Product> productMap;

    OnlineShop() {
        productMap = new TreeMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price) {
        productMap.putIfAbsent(id, new Product(id, category, name, createdAt, price));
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException {
        Product product = productMap.get(id);
        if (product == null) {
            throw new ProductNotFoundException("Product with id "+id+" does not exist in the online shop!");
        } else {
            product.incrementSoldQuantity(quantity);
            return product.getPrice() * quantity;
        }
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<Product> filteredList = productMap.values().stream()
                .filter(p -> category == null || p.getCategory().equals(category))
                .collect(Collectors.toList());

        Comparator<Product> comparator = getComparator(comparatorType);
        filteredList.sort(comparator);

        List<List<Product>> result = new ArrayList<>();
        int totalProducts = filteredList.size();

        for (int i = 0; i < totalProducts; i += pageSize) {
            int endIndex = Math.min(i + pageSize, totalProducts);

            // Земање на дел од листата (страница)
            List<Product> page = filteredList.subList(i, endIndex);

            result.add(page);
        }
        return result;
    }

    private Comparator<Product> getComparator(COMPARATOR_TYPE comparatorType) {
        Comparator<Product> secondaryComparator = Comparator.comparing(Product::getId);
        if (comparatorType.equals(COMPARATOR_TYPE.NEWEST_FIRST) ){
            return Comparator.comparing(Product::getCreatedAt).reversed();
        }
        else if (comparatorType.equals(COMPARATOR_TYPE.OLDEST_FIRST)) {
            return Comparator.comparing(Product::getCreatedAt);
        }
        else if (comparatorType.equals(COMPARATOR_TYPE.LOWEST_PRICE_FIRST) ){
            return Comparator.comparing(Product::getPrice);
        }
        else if (comparatorType.equals(COMPARATOR_TYPE.HIGHEST_PRICE_FIRST) ){
            return Comparator.comparing(Product::getPrice).reversed();
        }
        else if (comparatorType.equals(COMPARATOR_TYPE.MOST_SOLD_FIRST)){
            return Comparator.comparing(Product::getSoldQuantity).reversed().thenComparing(secondaryComparator);
        }
        else {
            return Comparator.comparing(Product::getSoldQuantity).thenComparing(secondaryComparator);
        }
    }

}


public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category = null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}

