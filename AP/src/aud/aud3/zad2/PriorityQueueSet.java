package aud.aud3.zad2;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

public class PriorityQueueSet<E> {
    private Set<QueueItem<E>> items;

    public PriorityQueueSet() {
        items = new TreeSet<>();
    }

    public void add(QueueItem<E> e){
        items.add(e);
    }

    public QueueItem<E> peek(){
        return items.iterator().next();
    }

    public QueueItem<E> remove(){
        if(items.isEmpty()){
            throw new NoSuchElementException();
        }
        QueueItem<E> e = items.iterator().next();
        items.remove(e);
        return e;
    }
    @Override
    public String toString() {
        return "PriorityQueueSet{" +
                "items=" + items +
                '}';
    }

    public static void main(String[] args) {
        PriorityQueueSet<String> pq = new PriorityQueueSet<String>();
        pq.add(new QueueItem<>("Dimi",100));
        pq.add(new QueueItem<>("Andrej",2));
        pq.add(new QueueItem<>("Finki",300));
        pq.add(new QueueItem<>("BAba",5));

        System.out.println(pq);
    }
}
