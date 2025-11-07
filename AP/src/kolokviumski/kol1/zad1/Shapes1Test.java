package kolokviumski.kol1.zad1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.*;

class Canvas{
    String ID;
    List<Integer> sizes;

    public Canvas(String text) {
        String[] parts = text.split("\\s+");
        this.ID = parts[0];
        this.sizes = new ArrayList<>();

        for (int i = 1; i < parts.length; i++) {
            this.sizes.add(Integer.parseInt(parts[i]));
        }
    }

    public int getPerimeter() {
        return sizes.stream().mapToInt(i -> i*4).sum();
    }


}

class ShapesApplication {
    List<Canvas> canvases;

    public ShapesApplication() {
        this.canvases = new ArrayList<>();
    }


    int readCanvases (InputStream inputStream){
        int counter = 0;
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split("\\s+");
            counter += split.length-1;
            canvases.add(new Canvas(line));
        }
        return counter;
    }

    void printLargestCanvasTo(OutputStream outputStream) throws IOException {
        canvases.stream()
                .max(Comparator.comparingInt(Canvas::getPerimeter))
                .ifPresent(c -> {
                    String result = String.format("%s %d %d\n", c.ID, c.sizes.size(), c.getPerimeter());
                    try {
                        outputStream.write(result.getBytes());
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
    }
}
public class Shapes1Test {
    public static void main(String[] args) throws IOException {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }


}
