package aud.aud2.zad1;

public class StaticMessage implements MessageProvider{
    @Override
    public String getMessage() {
        return "Hello from a regular class!";
    }
}
