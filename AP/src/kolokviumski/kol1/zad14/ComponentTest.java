package kolokviumski.kol1.zad14;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Component implements Comparable<Component> {
    String color;
    int weight;
    List<Component> components;

    Component(String color, int weight){
        this.color = color;
        this.weight = weight;
        components = new ArrayList<>();
    }

    void addComponent(Component component){
        components.add(component);
        components.sort(Comparator
                .<Component>comparingInt(c -> c.weight)
                .thenComparing(c -> c.color));
    }

    @Override
    public int compareTo(Component other) {
        if (this.weight != other.weight) {
            return Integer.compare(this.weight, other.weight);
        }
        return this.color.compareTo(other.color);
    }

    public String getComponentString(int intend){
        StringBuilder sb = new StringBuilder();
        if (intend > 0) {
            sb.append("---".repeat(Math.max(0, intend)));
        }

        sb.append(String.format("%d:%s\n",weight,color));

        components.forEach(c->sb.append(c.getComponentString(intend + 1)));
        return sb.toString();
    }
}

class Window{
    String name;
    List<Component> components;

    Window(String name){
        this.name = name;
        components = new ArrayList<>();
    }

    void addComponent(int position, Component component){
        int index = position - 1;

        if (index < 0 || index > components.size()) {
            throw new InvalidPositionException(String.format("Invalid position %d, out of range", position));
        }

        if (index < components.size()){
            throw new InvalidPositionException(String.format("Invalid position %d, alredy taken!", position));
        }

        components.add(index, component);
    }

    private void changeComponentColor(Component component, int weight, String color) {
        if (component.weight < weight) {
            component.color = color;
        }
        for (Component c : component.components) {
            changeComponentColor(c, weight, color);
        }
    }

    void changeColor(int weight, String color){
        for(Component c: components){
            changeComponentColor(c, weight, color);
        }
    }

    void swichComponents(int pos1, int pos2){
        int index1 = pos1 - 1;
        int index2 = pos2 - 1;

        if (index1 < 0 || index1 >= components.size() || index2 < 0 || index2 >= components.size()) {
            return;
        }

        Component temp = components.get(index1);
        components.set(index1, components.get(index2));
        components.set(index2, temp);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("WINDOW %s\n", name));
        int position = 1;

        for (Component component : components) {
            String componentStr = component.getComponentString(0);

            if (componentStr.endsWith("\n")) {
                componentStr = componentStr.substring(0, componentStr.length() - 1);
            }

            sb.append(String.format("%d:%s\n", position++, componentStr));
        }
        return sb.toString().trim();
    }
}

class InvalidPositionException extends RuntimeException{
    public InvalidPositionException(String message){
        super(message);
    }
}

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;

        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }
            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                break;
            }

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            } else {
                break;
            }
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);

        if (scanner.hasNextInt()) {
            int weight = scanner.nextInt();
            scanner.nextLine();
            String color = scanner.nextLine();
            window.changeColor(weight, color);
            System.out.println();
            System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
            System.out.println(window);
        }

        if (scanner.hasNextInt()) {
            int pos1 = scanner.nextInt();
            int pos2 = scanner.nextInt();
            System.out.println();
            System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
            window.swichComponents(pos1, pos2);
            System.out.println(window);
        }
    }
}