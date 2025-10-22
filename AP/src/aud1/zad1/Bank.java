package aud1.zad1;

import java.util.ArrayList;
import java.util.List;

public class Bank {

    List<Account> accounts = new ArrayList<>();

    public Bank(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account){
        this.accounts.add(account);
    }

    public double totalAssets(){
        double sum=0;
        for (Account account : accounts) {
            sum+=account.getCurrentAmount();
        }
        return sum;
    }

    public void addInterest(){
        for (Account account : accounts) {
            if(account instanceof InterestBearingAccount){
                ((InterestBearingAccount) account).addInterest();
            }
        }
    }
}
