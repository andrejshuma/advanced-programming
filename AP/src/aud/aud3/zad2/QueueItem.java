package aud.aud3.zad2;

public class QueueItem <E> implements Comparable<QueueItem<E>> {
    private E item;
    private int priority;

    public QueueItem(E item, int priority) {
        this.item = item;
        this.priority = priority;
    }

    public E getItem() {
        return item;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "QueueItem{" +
                "item=" + item +
                ", priority=" + priority +
                '}';
    }

    @Override
    public int compareTo(QueueItem<E> other) {
        return Integer.compare(this.priority,other.priority);
    }
}
