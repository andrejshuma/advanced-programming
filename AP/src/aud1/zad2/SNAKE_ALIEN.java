package aud1.zad2;

public class SNAKE_ALIEN extends Alien{
    public SNAKE_ALIEN(int health, String name) {
        super(health, name);
    }

    @Override
    protected int getDamage() {
        return 10;
    }
}
