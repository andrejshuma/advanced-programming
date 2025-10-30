package labs.lab1.zad2;

import java.util.Scanner;

interface Movable{
    void moveUp() throws ObjectCanNotBeMovedException ;
    void moveDown()throws ObjectCanNotBeMovedException;
    void moveLeft()throws ObjectCanNotBeMovedException;
    void moveRight()throws ObjectCanNotBeMovedException;

    // checks if the object fits within [0, x_MAX] x [0, y_MAX]
    boolean doesFit(int x_MAX, int y_MAX);

    int getCurrentXPosition() ;
    int getCurrentYPosition();

    int getYSpeed();

    int getXSpeed();

    class ObjectCanNotBeMovedException extends Exception {
        ObjectCanNotBeMovedException(int x, int y) {
            super("Point (" + x + "," + y + ") is out of bounds");
        }
    }
}

class MovingPoint implements Movable{
    int x;
    int y;
    int xSpeed;
    int ySpeed;

    public MovingPoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public String toString() {
        return "Movable point with coordinates (" + x + "," + y+")";
    }

    @Override
    public boolean doesFit(int x_MAX, int y_MAX) {
        return x >= 0 && x <= x_MAX && y >= 0 && y <= y_MAX;
    }

    // Fixed: Must declare ObjectCanNotBeMovedException
    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        this.y += ySpeed;
    }

    // Fixed: Must declare ObjectCanNotBeMovedException
    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        this.y -= ySpeed;
    }

    // Fixed: Must declare ObjectCanNotBeMovedException
    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        this.x -= xSpeed;
    }

    // Fixed: Must declare ObjectCanNotBeMovedException
    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        this.x += xSpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return this.x;
    }

    @Override
    public int getCurrentYPosition() {
        return this.y;
    }

    @Override
    public int getYSpeed() {
        return this.ySpeed;
    }

    @Override
    public int getXSpeed() {
        return xSpeed;
    }
}

class MovingCircle implements Movable{
    int radius;
    MovingPoint center;

    public MovingCircle(int radius, MovingPoint center) {
        this.radius = radius;
        this.center = center;
    }

    // Fixed: Correct logic for a Circle to fit *entirely* into the space (for adding to collection)
    @Override
    public boolean doesFit(int x_MAX, int y_MAX) {
        int centerX = center.getCurrentXPosition();
        int centerY = center.getCurrentYPosition();

        // The entire circle fits if its bounding box corners are within the limits.
        return (centerX - radius >= 0) && (centerX + radius <= x_MAX) &&
                (centerY - radius >= 0) && (centerY + radius <= y_MAX);
    }

    // Fixed: Must declare ObjectCanNotBeMovedException
    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        this.center.moveUp();
    }

    // Fixed: Must declare ObjectCanNotBeMovedException
    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        this.center.moveDown();
    }

    // Fixed: Must declare ObjectCanNotBeMovedException
    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        this.center.moveLeft();
    }

    // Fixed: Must declare ObjectCanNotBeMovedException
    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        this.center.moveRight();
    }

    @Override
    public int getCurrentXPosition() {
        return this.center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return this.center.getCurrentYPosition();
    }

    // Fixed: Must return the center's speed
    @Override
    public int getYSpeed() {
        return this.center.getYSpeed();
    }

    // Fixed: Must return the center's speed
    @Override
    public int getXSpeed() {
        return this.center.getXSpeed();
    }

    @Override
    public String toString() {
        return "Movable circle with center coordinates ("+center.getCurrentXPosition()+","+center.getCurrentYPosition()+") and radius " +radius;
    }

    public int getRadius() {
        return this.radius;
    }
}

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

class MovablesCollection{
    Movable [] movable;
    private static int maximumX;
    private static int maximumY;
    int numMovables;

    // Fixed: Moved inside MovablesCollection and fixed the missing Point message
    public static class MovableObjectNotFittableException extends Exception {
        MovableObjectNotFittableException(Movable m){
            super(buildMessage(m));
        }
        private static String buildMessage(Movable movable) {
            if(movable instanceof MovingCircle){
                MovingCircle circle = (MovingCircle) movable;
                return "Movable circle with center (" + circle.getCurrentXPosition() + "," + circle.getCurrentYPosition() + ") and radius " + circle.getRadius() + " can not be fitted into the collection";
            }else{
                MovingPoint point = (MovingPoint) movable;
                return "Movable point with coordinates (" + point.getCurrentXPosition() + "," + point.getCurrentYPosition() + ") can not be fitted into the collection";
            }
        }
    }

    public MovablesCollection(int x_MAX, int y_MAX){
        maximumX = x_MAX;
        maximumY = y_MAX;
        this.movable = new Movable[200];
        this.numMovables=0;
    }
    public static void setxMax(int i) {
        maximumX = i;
    }

    public static void setyMax(int i) {
        maximumY = i;
    }

    private void move(DIRECTION direction, Movable m) throws Movable.ObjectCanNotBeMovedException {
        if(direction == DIRECTION.UP)
            m.moveUp();
        else if(direction == DIRECTION.DOWN)
            m.moveDown();
        else if(direction ==DIRECTION.LEFT)
            m.moveLeft();
        else
            m.moveRight();
    }

    public void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) {
        for(int i = 0; i < numMovables; i++){
            Movable currentMovable = movable[i];

            if ((type == TYPE.POINT && currentMovable instanceof MovingPoint) ||
                    (type == TYPE.CIRCLE && currentMovable instanceof MovingCircle)) {

                int currentX = currentMovable.getCurrentXPosition();
                int currentY = currentMovable.getCurrentYPosition();

                int newX = currentX;
                int newY = currentY;

                if(direction == DIRECTION.UP)
                    newY += currentMovable.getYSpeed();
                else if(direction == DIRECTION.DOWN)
                    newY -= currentMovable.getYSpeed();
                else if(direction == DIRECTION.LEFT)
                    newX -= currentMovable.getXSpeed();
                else // DIRECTION.RIGHT
                    newX += currentMovable.getXSpeed();

                boolean canMove = false;

                if (currentMovable instanceof MovingPoint) {
                    MovingPoint tempPoint = new MovingPoint(newX, newY, 0, 0);
                    canMove = tempPoint.doesFit(maximumX, maximumY);
                } else if (currentMovable instanceof MovingCircle) {
                    canMove = (newX >= 0 && newX <= maximumX && newY >= 0 && newY <= maximumY);
                }

                try {
                    if (!canMove) {
                        throw new Movable.ObjectCanNotBeMovedException(newX, newY);
                    }
                    move(direction, currentMovable);
                } catch (Movable.ObjectCanNotBeMovedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void addMovableObject(Movable m) {
        try{
            if(!m.doesFit(maximumX, maximumY))
                throw new MovableObjectNotFittableException(m);
            if (numMovables >= movable.length) {
                System.out.println("Collection is full. Cannot add more objects.");
            } else {
                movable[numMovables++] = m;
            }
        }catch (MovableObjectNotFittableException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Collection of movable objects with size " + numMovables + ":\n");
        for (int i = 0; i < numMovables; i++) {
            sb.append(movable[i].toString()).append("\n");
        }
        return sb.toString();
    }
}


public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        Scanner sc = new Scanner(System.in);

        // Read the first line which should contain the number of samples
        if (!sc.hasNextLine()) {
            sc.close();
            return;
        }
        int samples = Integer.parseInt(sc.nextLine());

        // Initialize collection after reading the first line
        MovablesCollection collection = new MovablesCollection(100, 100);

        for (int i = 0; i < samples; i++) {
            if (!sc.hasNextLine()) break;
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                collection.addMovableObject(new MovingPoint(x, y, xSpeed, ySpeed));
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                collection.addMovableObject(new MovingCircle(radius, new MovingPoint(x, y, xSpeed, ySpeed)));
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

        sc.close();
    }
}