package aud.aud3.zad3;

import java.util.ArrayList;

public class MyMatchClass {
    public static double standardDeviation(ArrayList<? extends Number> arrayList){
        double sum =0;
        for (Number n : arrayList){
            sum+= n.doubleValue();
        }

        double avg = sum / arrayList.size();
        sum =0;
        for(Number n : arrayList){
            sum+= (avg - n.doubleValue()) * (avg - n.doubleValue());

        }
        return Math.sqrt(sum/arrayList.size());

    }
    public static void main(String[] args) {

    }
}
