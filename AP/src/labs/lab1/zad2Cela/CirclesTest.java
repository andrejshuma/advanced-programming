package labs.lab1.zad2Cela;

import java.util.Scanner;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

interface Movable{
    void moveUp();
    void moveLeft();
    void moveRight();
    void moveDown();
    int getCurrentXPosition();
    int getCurrentYPosition();
    int getXSpeed();
    int getYSpeed();
    boolean doesFit(int x_MAX, int y_MAX);
}

class MovablePoint implements Movable{

    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() {
        y += ySpeed;
    }

    @Override
    public void moveLeft() {
        x -= xSpeed;
    }

    @Override
    public void moveRight() {
        x += xSpeed;
    }

    @Override
    public void moveDown() {
        y -= ySpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    @Override
    public int getXSpeed() {
        return xSpeed;
    }

    @Override
    public int getYSpeed() {
        return ySpeed;
    }

    @Override
    public boolean doesFit(int x_MAX, int y_MAX) {
        return x >= 0 && x <= x_MAX && y >= 0 && y <= y_MAX;
    }

    @Override
    public String toString(){
        return "Movable point with coordinates (" + x + "," + y + ")";
    }

}

class MovableCircle implements Movable{

    private int radius;
    private MovablePoint center;

    public MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
    }

    public int getRadius() {
        return radius;
    }

    public MovablePoint getCenter() {
        return center;
    }

    @Override
    public void moveUp() {
        center.moveUp();
    }

    @Override
    public void moveLeft() {
        center.moveLeft();
    }

    @Override
    public void moveRight() {
        center.moveRight();
    }

    @Override
    public void moveDown() {
        center.moveDown();
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

    @Override
    public int getXSpeed() {
        return center.getXSpeed();
    }

    @Override
    public int getYSpeed() {
        return center.getYSpeed();
    }

    @Override
    public boolean doesFit(int x_MAX, int y_MAX) {
        for(int i = center.getCurrentYPosition() - radius, j = center.getCurrentXPosition() - radius; i <= center.getCurrentYPosition() + radius; i++, j++){
            if(i < 0 || i > y_MAX || j < 0 || j > x_MAX)
                return false;
        }
        return true;
    }

    @Override
    public String toString(){
        return "Movable circle with center coordinates (" + center.getCurrentXPosition() + "," + center.getCurrentYPosition() + ") and radius " + radius;
    }

}

class MovablesCollection {
    private Movable[] movables;
    private int numMovables;
    private static int xMAX;
    private static int yMAX;

    public MovablesCollection(int x_MAX, int y_MAX) {
        this.xMAX = x_MAX;
        this.yMAX = y_MAX;
        this.movables = new Movable[200];
        this.numMovables = 0;
    }

    public static void setxMax(int i) {
        xMAX = i;
    }

    public static void setyMax(int i) {
        yMAX = i;
    }

    public void addMovableObject(Movable m) {
        try{
            if(!m.doesFit(xMAX, yMAX))
                throw new MovableObjectNotFittableException(m);
            movables[numMovables++] = m;
        }catch (MovableObjectNotFittableException e){
            System.out.println(e.getMessage());
        }

    }

    private void move(DIRECTION direction, Movable m) {
        if(direction == DIRECTION.UP)
            m.moveUp();
        else if(direction == DIRECTION.DOWN)
            m.moveDown();
        else if(direction == DIRECTION.LEFT)
            m.moveLeft();
        else
            m.moveRight();
    }

    public void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) {
        for(int i = 0; i < numMovables; i++){
            int newX = movables[i].getCurrentXPosition();
            int newY = movables[i].getCurrentYPosition();

            if(direction == DIRECTION.UP)
                newY += movables[i].getYSpeed();
            else if(direction == DIRECTION.DOWN)
                newY -= movables[i].getYSpeed();
            else if(direction == DIRECTION.LEFT)
                newX -= movables[i].getXSpeed();
            else
                newX += movables[i].getXSpeed();

            if(type == TYPE.POINT && movables[i] instanceof MovablePoint){
                MovablePoint mp = (MovablePoint) movables[i];
                try{
                    if(newX < 0 || newX > xMAX || newY < 0 || newY > yMAX)
                        throw new ObjectCanNotBeMovedException(newX, newY);
                    move(direction, movables[i]);
                }catch (ObjectCanNotBeMovedException e){
                    System.out.println(e.getMessage());
                }
            }else if(type == TYPE.CIRCLE && movables[i] instanceof MovableCircle){
                MovableCircle mc = (MovableCircle) movables[i];
                try{
                    if(newX < 0 || newX > xMAX || newY < 0 || newY > yMAX)
                        throw new ObjectCanNotBeMovedException(newX, newY);
                    move(direction, movables[i]);
                }catch (ObjectCanNotBeMovedException e){
                    System.out.println(e.getMessage());
                }
            }
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Collection of movable objects with size " + numMovables + ": \n");
        for(int i = 0; i < numMovables; i++) {
            sb.append(movables[i].toString() + "\n");
        }
        return sb.toString();
    }
}

class ObjectCanNotBeMovedException extends Exception{
    ObjectCanNotBeMovedException(int x, int y) {
        super("Point (" + x + "," + y + ") is out of bounds");
    }
}

class MovableObjectNotFittableException extends Exception {
    MovableObjectNotFittableException(Movable movable) {
        super(buildMessage(movable));
    }
    private static String buildMessage(Movable movable){
        if(movable instanceof MovableCircle){
            MovableCircle circle = (MovableCircle) movable;
            return "Movable circle with center (" + circle.getCurrentXPosition() + "," + circle.getCurrentYPosition() + ") and radius " + circle.getRadius() + " can not be fitted into the collection";
        }else{
            MovablePoint point = (MovablePoint) movable;
            return "umri";
        }
    }
}

public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());


    }


}

