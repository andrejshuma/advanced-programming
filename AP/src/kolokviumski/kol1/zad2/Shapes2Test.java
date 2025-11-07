package kolokviumski.kol1.zad2;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Shape{
    String type;
    Double size;

    public Shape(String type, Double size) {
        this.type = type;
        this.size = size;
    }

    public double getArea(){
        if(type.equals("S")){
            return size*size;
        }
        else{
            return size*size*Math.PI;
        }
    }
}
class Canvas implements Comparable<Canvas>{
    String id;
    List<Shape> shapes;

    public Canvas(String text) {
        String[] split = text.split("\\s+");
        this.shapes= new ArrayList<>();
        this.id = split[0];

        for (int i = 1; i <split.length-1 ; i+=2) {
            shapes.add(new Shape(split[i],Double.parseDouble(split[i+1])));
        }
    }
    public double sumArea(){
        return shapes.stream().mapToDouble(Shape::getArea).sum();
    }
    public double maxAreaOfAllShapes() {
        return shapes.stream().mapToDouble(Shape::getArea).max().orElse(0);
    }

    public double minAreaOfAllShapes() {
        return shapes.stream().mapToDouble(Shape::getArea).min().orElse(0);
    }

    public double avgAreaOfAllShapes() {
        return shapes.stream().mapToDouble(Shape::getArea).average().orElse(0);
    }

    public int totalCircles(){
        return (int) shapes.stream().filter(i -> i.type.equals("C")).count();
    }

    public int totalSquares(){
        return (int) shapes.stream().filter(i -> i.type.equals("S")).count();
    }

    @Override
    public int compareTo(Canvas o) {
        return Double.compare(sumArea(),o.sumArea());
    }
}
class ShapesApplication{
    List<Canvas> canvases;
    double maxArea;



    public ShapesApplication(int maxArea) {
        this.canvases = new ArrayList<>();
        this.maxArea = maxArea;
    }

    void readCanvases (InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Canvas c = new Canvas(line);
            try{
                if(c.maxAreaOfAllShapes() > maxArea){
                    throw new IrregularCanvasException(c,maxArea);
                }
                canvases.add(c);
            }catch (IrregularCanvasException ignored){
                }

        }
    }

    @SuppressWarnings({})
    void printCanvases (OutputStream os){
        canvases = canvases.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        for (Canvas canvase : canvases) {
            String output = String.format("%s %d %d %d %.2f %.2f %.2f\n",
                    canvase.id,
                    canvase.shapes.size(),
                    canvase.totalCircles(),
                    canvase.totalSquares(),
                    canvase.minAreaOfAllShapes(),
                    canvase.maxAreaOfAllShapes(),
                    canvase.avgAreaOfAllShapes());
            try {
                os.write(output.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class IrregularCanvasException extends Exception {
        public IrregularCanvasException(Canvas c,double maxArea) {
            System.out.println(String.format("Canvas %s has a shape with area larger than %.2f", c.id, maxArea));
        }
    }
}

public class Shapes2Test {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}
