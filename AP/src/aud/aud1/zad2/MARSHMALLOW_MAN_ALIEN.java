package aud.aud1.zad2;

public class MARSHMALLOW_MAN_ALIEN extends Alien{
    public MARSHMALLOW_MAN_ALIEN(int health, String name) {
        super(health, name);
    }

    @Override
    protected int getDamage() {
        return 1;
    }
}
