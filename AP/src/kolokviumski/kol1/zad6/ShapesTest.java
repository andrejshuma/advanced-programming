package kolokviumski.kol1.zad6;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

interface Scalable {
    void scale(float scaleFactor);
}

interface Stackable {
    float weight();
}

enum Color {
    RED, GREEN, BLUE
}

abstract class Shape implements Scalable, Stackable {
    String id;
    Color color;

    public Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    protected abstract String getTypeChar();

    @Override
    public String toString() {
        return String.format(
                "%s: %-5s %-10s %10.2f",
                getTypeChar(),
                id,
                color,
                weight()
        );
    }
}

class Circle extends Shape {
    private float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        this.radius *= scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (radius * radius * Math.PI);
    }

    @Override
    protected String getTypeChar() {
        return "C";
    }
}

class Rectange extends Shape {
    private float width;
    private float height;

    public Rectange(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }


    @Override
    public void scale(float scaleFactor) {
        this.width *= scaleFactor;
        this.height *= scaleFactor;
    }

    @Override
    public float weight() {
        return width * height;
    }

    @Override
    protected String getTypeChar() {
        return "R";
    }
}

class Canvas {
    List<Shape> shapes;

    public Canvas() {
        this.shapes = new ArrayList<>();
    }

    void add(String id, Color color, float radius){
        maintainOrder(new Circle(id,color,radius));
    }
    void add(String id, Color color, float width, float height){
        maintainOrder(new Rectange(id,color,width,height));
    }

    void scale(String id, float scaleFactor){
        Shape targetShape = null;
        for (Shape shape : shapes) {
            if(shape.id.equals(id)){
                targetShape=shape;
                shapes.remove(shape);
                break;
            }
        }
        if(targetShape ==null){
            return;
        }
        targetShape.scale(scaleFactor);
        maintainOrder(targetShape);

    }

    private void maintainOrder(Shape shape) {
        int index = 0;
        while (index < shapes.size() && shape.weight() <= shapes.get(index).weight()) {

            if (shape.weight() > shapes.get(index).weight()) {
                break;
            }
            index++;
        }
        shapes.add(index, shape);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Shape shape : shapes) {
            sb.append(shape.toString()).append("\n");
        }
        return sb.toString();
    }
}

public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}


