package aud.aud1.zad2;

 abstract class Alien {
    private int health; // 0=dead, 100=full strength
    private String name;

    public Alien(int health, String name) {
        this.health = health;
        this.name = name;
    }

    protected abstract int getDamage();
}
