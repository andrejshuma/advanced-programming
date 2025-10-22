package aud2;

interface Operation{
    float execute(int a, int b);
}


//traditional
class Addition implements Operation {
    @Override
    public float execute(int a, int b) {
        return a+b;
    }
}

interface MessageProvider{
    String getMessage();
}

class TraditionalMessageProvider implements MessageProvider{

    @Override
    public String getMessage() {
        return "Traditional Hello :)";
    }
}
public class Intro {
    public static void main(String[] args) {
        int x = 5, y = 6;
        Operation addition = new Addition();
        System.out.println(addition.execute(x, y));

        //2. anonymous class
        Operation subtraction = new Operation() {
            @Override
            public float execute(int a, int b) {
                return a - b;
            }
        };

        System.out.println(subtraction.execute(x, y));

        //3. lambda expression (only for functional interfaces (with one method))
        Operation multiplication = (a, b) -> a * b;
        System.out.println(multiplication.execute(x, y));

        MessageProvider tmp = new TraditionalMessageProvider();

        //2. anonim
        MessageProvider amp = new MessageProvider() {
            @Override
            public String getMessage() {
                return "Anonim";
            }
        };

        //3 lambda
        MessageProvider lmp = () -> "Lambda Hello";

        System.out.println(tmp.getMessage());
        System.out.println(amp.getMessage());
        System.out.println(lmp.getMessage());
    }
}
