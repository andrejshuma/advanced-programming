package aud.aud3.zad1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Box<E> {

    private List<E> items;

    public Box() {
        items = new ArrayList<>();
    }

    public void add(E e){
        items.add(e);
    }

    public boolean isEmpty(){
        return items.isEmpty();
    }

    public E drawItem(){
        if(isEmpty())
            throw new IndexOutOfBoundsException();
        Random random = new Random();
        E item = items.get(random.nextInt(items.size()));
        items.remove(item);
        return item;
    }

    @Override
    public String toString() {
        return "Box{" +
                "items=" + items +
                '}';
    }

    public static void main(String[] args) {
        Box<String> box = new Box<String>();
        box.add("a");
        box.add("b");
        box.add("c");
        System.out.println(box);

        Box<Integer> box1 = new Box<Integer>();
        box1.add(1);
        box1.add(2);
        box1.add(3);
        box1.add(4);
        System.out.println(box1);

    }
}
