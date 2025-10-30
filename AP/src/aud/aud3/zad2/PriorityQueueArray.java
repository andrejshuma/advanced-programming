package aud.aud3.zad2;

import java.util.HashMap;
import java.util.Map;

public class PriorityQueueArray <T>{
    private Map<T, Integer> items;

    public PriorityQueueArray(Map<T, Integer> items) {
        items = new HashMap<>();
    }

    public void add(T item, int priority) {
        items.put(item, priority);
    }

    public T remove( ){
        if(items.isEmpty()){
            return null;
        }

        T highestPriorityItem = null;
        int highestPriority = Integer.MIN_VALUE;

        for(Map.Entry<T, Integer> entry : items.entrySet()) {
            if(entry.getValue() > highestPriority) {
                highestPriority = entry.getValue();
                highestPriorityItem = entry.getKey();
            }
        }
        items.remove(highestPriorityItem);
        return highestPriorityItem;
    }

    @Override
    public String toString() {
        return "PriorityQueueArray{" +
                "items=" + items +
                '}';
    }
}
