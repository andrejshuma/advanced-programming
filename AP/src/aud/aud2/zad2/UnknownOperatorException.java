package aud.aud2.zad2;

public class UnknownOperatorException extends Exception {
    public UnknownOperatorException(char operator) {
        super(String.format("%c is an unknown operation", operator));
    }
}
