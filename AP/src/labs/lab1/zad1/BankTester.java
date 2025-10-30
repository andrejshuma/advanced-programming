package labs.lab1.zad1;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// Utility class for formatting double to 2 decimal places
class DoubleFormatter {
    public static String format(double value) {
        return String.format("%.2f", value);
    }
}

abstract class Transaction {
    long fromId;
    long toId;
    protected String description;
    double amount; // Changed from BigDecimal to double

    public Transaction(long fromId, long toId, String description, double amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.description = description;
        this.amount = Math.round(amount * 100.0) / 100.0; // Round to 2 decimal places
    }

    public abstract double getProvision(); // Changed return type to double

    public long getToId() {
        return toId;
    }

    public String getDescription() {
        return description;
    }

    public long getFromId() {
        return fromId;
    }

    public double getAmount() {
        return amount;
    }
}

class FlatAmountProvisionTransaction extends Transaction {
    private double flatProvision; // Changed from BigDecimal to double

    public FlatAmountProvisionTransaction(long fromId, long toId, double amount, double flatProvision) {
        super(fromId, toId, "FlatAmount", amount);
        this.flatProvision = Math.round(flatProvision * 100.0) / 100.0; // Round to 2 decimal places
    }

    @Override
    public double getProvision() { // Changed return type to double
        return flatProvision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlatAmountProvisionTransaction)) return false;
        FlatAmountProvisionTransaction t = (FlatAmountProvisionTransaction) o;
        // Comparing doubles for equality is inherently problematic due to precision issues.
        // For a refactoring exercise, we'll use a small tolerance (epsilon) for amount comparison,
        // but for this specific exercise, the original code used BigDecimal.equals(),
        // which implies exact equality. We'll use strict equality here,
        // relying on the rounding in the constructor.
        return this.getFromId() == t.getFromId()
                && this.getToId() == t.getToId()
                && this.getAmount() == t.getAmount()
                && this.flatProvision == t.flatProvision
                && this.description.equals(t.getDescription());
    }
}

class FlatPercentProvisionTransaction extends Transaction {
    int centsPerDolar;

    public FlatPercentProvisionTransaction(long fromId, long toId, double amount, int centsPerDolar) {
        super(fromId, toId, "FlatPercent", amount);
        this.centsPerDolar = centsPerDolar;
    }

    @Override
    public double getProvision() { // Changed return type to double
        double provision = amount * centsPerDolar / 100.0;
        return Math.round(provision * 100.0) / 100.0; // Round the provision to 2 decimal places
    }

    public int getCentsPerDolar() {
        return centsPerDolar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlatPercentProvisionTransaction)) return false;
        FlatPercentProvisionTransaction that = (FlatPercentProvisionTransaction) o;
        return this.fromId == that.getFromId()
                && this.toId == that.getToId()
                && this.amount == that.amount // See note in FlatAmountProvisionTransaction.equals()
                && this.centsPerDolar == that.centsPerDolar
                && this.description.equals(that.getDescription());
    }
}

class Account {
    String name;
    long id;
    double balance; // Changed from BigDecimal to double

    public Account(String name, double balance) {
        this.name = name;
        this.id = (long) (Math.random() * 1000);
        this.balance = Math.round(balance * 100.0) / 100.0; // Round to 2 decimal places
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) { // Changed parameter type to double
        this.balance = Math.round(balance * 100.0) / 100.0; // Round to 2 decimal places
    }

    public void pay(Account other, double amount, double provision) { // Changed parameter types to double
        // from loses amount + provision
        this.balance = this.balance - amount - provision;
        this.balance = Math.round(this.balance * 100.0) / 100.0; // Re-round after subtraction

        // to gains only the amount
        other.setBalance(other.getBalance() + amount);
    }

    @Override
    public String toString() {
        // Use the new formatter
        return "Name: " + name + "\n"
                + "Balance: " + DoubleFormatter.format(balance) + "$" + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account a = (Account) o;
        return this.id == a.id;
    }
}

class Bank {
    public String name;
    Account[] accounts;
    private List<Transaction> transactions;

    public Bank(String name, Account[] accounts) {
        this.name = name;
        this.accounts = Arrays.copyOf(accounts, accounts.length);
        this.transactions = new ArrayList<>();
    }

    public Account findAccountById(long id) {
        for (Account acc : accounts) {
            if (acc.getId() == id)
                return acc;
        }
        return null;
    }

    public Account[] getAccounts() {
        return accounts;
    }

    public boolean makeTransaction(Transaction t) {
        Account fromAccount = findAccountById(t.getFromId());
        Account toAccount = findAccountById(t.getToId());
        if (fromAccount == null || toAccount == null) return false;

        double total = t.getAmount() + t.getProvision();
        // Compare doubles directly (not ideal, but required for this refactor)
        if (fromAccount.getBalance() < total)
            return false;

        fromAccount.pay(toAccount, t.getAmount(), t.getProvision());
        transactions.add(t);
        return true;
    }

    public double totalTransfers() { // Changed return type to double
        double total = transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
        return Math.round(total * 100.0) / 100.0; // Round the final sum
    }

    public double totalProvision() { // Changed return type to double
        double total = transactions.stream()
                .mapToDouble(Transaction::getProvision)
                .sum();
        return Math.round(total * 100.0) / 100.0; // Round the final sum
    }

    public void forEachConditional(Predicate<Account> predicate, Consumer<Account> consumer) {
        for (Account account : accounts) {
            if (predicate.test(account)) {
                consumer.accept(account);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name).append("\n\n");
        for (Account account : accounts) {
            sb.append(account.toString());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bank)) return false;
        Bank b = (Bank) o;

        if (!this.name.equals(b.name)) return false;
        if (this.accounts.length != b.accounts.length) return false;
        if (this.transactions.size() != b.transactions.size()) return false;

        for (int i = 0; i < accounts.length; i++) {
            if (!this.accounts[i].equals(b.accounts[i]))
                return false;
        }

        // Transaction equals method relies on double comparison, which is less reliable.
        for (int i = 0; i < transactions.size(); i++) {
            if (!this.transactions.get(i).equals(b.transactions.get(i)))
                return false;
        }

        return true;
    }
}

public class BankTester {
    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();


        Account[] testAccounts = {
                new Account("Alice", 12000),
                new Account("Bob", 8000),
                new Account("Charlie", 15000)
        };

        Bank testBank = new Bank("Loyalty Bank", testAccounts);


        // Replaced BigDecimal with double
        testBank.forEachConditional(
                acc -> acc.getBalance() > 10000.0,
                acc -> acc.setBalance(acc.getBalance() + 100.0)
        );

        System.out.println("After loyalty bonus:\n" + testBank);

    }

    private static double parseAmount(String amount) {
        return Double.parseDouble(amount.replace("$", ""));
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", 20.0);
        Account a2 = new Account("Andrej", 20.0);
        Account a3 = new Account("Andrej", 30.0);
        Account a4 = new Account("Gajduk", 20.0);
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1) && !a1.equals(a2) && !a2.equals(a1) && !a3.equals(a1)
                && !a4.equals(a1) && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account[] accounts = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), parseAmount(jin.nextLine()));
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String description = jin.nextLine();
                    double amount = parseAmount(jin.nextLine());
                    double parameter = parseAmount(jin.nextLine());
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(description, from_idx, to_idx, amount, parameter, bank);
                    // Use DoubleFormatter for printing
                    System.out.println("Transaction amount: " + DoubleFormatter.format(t.getAmount()) + "$");
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    // Use DoubleFormatter for printing
                    System.out.println("Total provisions: " + DoubleFormatter.format(bank.totalProvision()) + "$");
                    System.out.println("Total transfers: " + DoubleFormatter.format(bank.totalTransfers()) + "$");
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, double amount, double o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, (int) o);
        }
        return null;
    }
}