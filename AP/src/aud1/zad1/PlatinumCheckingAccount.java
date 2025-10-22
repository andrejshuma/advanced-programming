package aud1.zad1;

public class PlatinumCheckingAccount extends InterestCheckingAccount{
    public PlatinumCheckingAccount(String name, double currentAmount) {
        super(name, currentAmount);
    }

    @Override
    public void addInterest() {
        addAmount(getCurrentAmount()*(Interest_Rate*2));
    }
}
