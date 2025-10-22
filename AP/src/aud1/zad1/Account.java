package aud1.zad1;

abstract public class Account {
    private String name;

    private static int ACC_NUMBER = 0;
    private int accountNumber;
    private double currentAmount;

    public Account(String name, double currentAmount) {
        this.name = name;
        this.currentAmount = currentAmount;
        this.accountNumber = ++ACC_NUMBER;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void addAmount(double amount){
        currentAmount+=amount;
    }

    public void withdraw(int amount){
        if(currentAmount>=amount){
            currentAmount-=amount;
        }
    }



}
