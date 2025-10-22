package aud1.zad2;

import java.util.List;

public class AlienPack {
    private List<Alien> aliens;

    public AlienPack(List<Alien> aliens) {
        this.aliens = aliens;
    }

    public void addAlien(Alien alien){
        aliens.add(alien);
    }

    public List<Alien> getAliens() {
        return aliens;
    }

    public double getDamage(){
        double sum = 0;
        for (Alien alien : aliens) {
            sum+=alien.getDamage();
        }
        return sum;
    }

}
