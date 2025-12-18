package labs.lab7;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class BankBalance {

    public static class BankAccount {
        private int balance;
        private final Lock lock = new ReentrantLock();

        public BankAccount(int initialBalance) {
            this.balance = initialBalance;
        }

        public boolean deposit(int amount) {
            lock.lock();
            try {
                balance += amount;
                return true;
            } finally {
                lock.unlock();
            }
        }

        public boolean withdraw(int amount) {
            lock.lock();
            try {
                if (balance >= amount) {
                    balance -= amount;
                    return true;
                }
                return false;
            } finally {
                lock.unlock();
            }
        }

        public int getBalance() {
            lock.lock();
            try {
                return balance;
            } finally {
                lock.unlock();
            }
        }
    }

    public static class OperationResult {
        public final int operationId;
        public final boolean success;

        public OperationResult(int operationId, boolean success) {
            this.operationId = operationId;
            this.success = success;
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        int initialBalance = sc.nextInt();
        int n = sc.nextInt();

        BankAccount account = new BankAccount(initialBalance);

        List<Callable<OperationResult>> tasks = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String type = sc.next();
            int amount = sc.nextInt();
            int operationId = i + 1;

            tasks.add(() -> {
                Thread.sleep(3000);
                boolean success;
                if (type.equals("deposit")) {
                    success = account.deposit(amount);
                } else { // withdraw
                    success = account.withdraw(amount);
                }
                return new OperationResult(operationId, success);
            });
        }

        ExecutorService executor =
                Executors.newFixedThreadPool(4);

        List<Future<OperationResult>> futures = executor.invokeAll(tasks);

        for (Future<OperationResult> f : futures) {
            f.get();
        }

        executor.shutdown();

        System.out.println("FINAL_BALANCE " + account.getBalance());
    }
}
