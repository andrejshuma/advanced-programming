package aud1.zad2;

public class OGRE_ALIEN extends Alien {
    public OGRE_ALIEN(int health, String name) {
        super(health, name);
    }

    @Override
    protected int getDamage() {
        return 6;
    }
}
