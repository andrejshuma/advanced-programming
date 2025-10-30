package aud.aud1.zad1;

public class InterestCheckingAccount extends Account implements InterestBearingAccount{
    public static final double Interest_Rate = 0.03;

    public InterestCheckingAccount(String name, double currentAmount) {
        super(name, currentAmount);
    }

    @Override
    public void addInterest() {
        addAmount(getCurrentAmount() * Interest_Rate);
    }
}
