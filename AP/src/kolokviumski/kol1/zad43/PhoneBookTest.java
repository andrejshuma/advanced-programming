package kolokviumski.kol1.zad43;

import java.util.*;

class Contact implements Comparable<Contact>{
    private String name;
    private String number;

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }


    @Override
    public String toString() {
        return name + " " + number;
    }

    @Override
    public int compareTo(Contact o) {
        int nameComparison = this.name.compareTo(o.name);

        if(nameComparison == 0){
            return this.number.compareTo(o.number);
        }
        else return nameComparison;
    }
}
class PhoneBook{
    private Map<String, TreeSet<Contact>> contactMap;

    public PhoneBook() {
        this.contactMap = new TreeMap<>();
    }

    void addContact(String name, String number){
        contactMap.values().forEach(c-> c.forEach(
                cc ->{
                    if(cc.getNumber().equals(number)){
                        throw new DuplicateNumberException("Duplicate number: "+number);
                    }
                }
                ));

        contactMap.computeIfAbsent(name,k -> new TreeSet<>()).add(new Contact(name,number));
    }

    void contactsByNumber(String number){
        List<Contact> result = new ArrayList<>();

        contactMap.values().forEach(
                contactSet -> contactSet.forEach(
                        cc -> {
                            if(cc.getNumber().contains(number)){
                                result.add(cc);
                            }
                        }
                )
        );

        Collections.sort(result);

        if (result.isEmpty()) {
            System.out.println("NOT FOUND");
        } else {
            result.forEach(System.out::println);
        }
    }

    void contactsByName(String name){
        TreeSet<Contact> contacts = contactMap.get(name);
        if(contacts == null || contacts.isEmpty()){
            System.out.println("NOT FOUND");
        }

        else contactMap.get(name).forEach(System.out::println);
    }

}
class DuplicateNumberException extends RuntimeException{
    public DuplicateNumberException(String message) {
        super(message);
    }
}
public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}

// Вашиот код овде

