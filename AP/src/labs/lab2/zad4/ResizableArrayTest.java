package labs.lab2.zad4;


import java.util.*;

class ResizableArray<T> {
    private T[] array;
    private int size;

    @SuppressWarnings("unchecked")
    public ResizableArray() {
        this.size = 0;
        this.array = (T[]) new Object[10];
    }

    public int count() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public T elementAt(int index) throws ArrayIndexOutOfBoundsException {
        if (index < 0 || index >= size)
            throw new ArrayIndexOutOfBoundsException(index, size);
        return array[index];
    }

    public boolean contains(T element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(array[i], element))
                return true;
        }
        return false;
    }

    public Object[] toArray() {
        return Arrays.copyOf(array, size);
    }

    public void addElement(T element) {
        ensureCapacity();
        array[size++] = element;
    }

    public boolean removeElement(T element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(array[i], element)) {
                // shift elements left
                System.arraycopy(array, i + 1, array, i, size - i - 1);
                array[--size] = null;
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == array.length) {
            T[] newArray = (T[]) new Object[array.length * 2 + 1];
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src)
            throws ArrayIndexOutOfBoundsException {
        Object[] temp = src.toArray(); // snapshot of elements
        for (Object el : temp) {
            dest.addElement((T) el);
        }
    }
}

class IntegerArray extends ResizableArray<Integer> {

    public double sum() throws ArrayIndexOutOfBoundsException {
        double s = 0;
        for (int i = 0; i < count(); i++) {
            s += elementAt(i);
        }
        return s;
    }

    public double mean() throws ArrayIndexOutOfBoundsException {
        return count() == 0 ? 0 : sum() / count();
    }

    public int countNonZero() throws ArrayIndexOutOfBoundsException {
        int c = 0;
        for (int i = 0; i < count(); i++) {
            if (elementAt(i) != 0)
                c++;
        }
        return c;
    }

    public IntegerArray distinct() throws ArrayIndexOutOfBoundsException {
        IntegerArray result = new IntegerArray();
        HashSet<Integer> seen = new HashSet<>();

        for (int i = 0; i < count(); i++) {
            Integer el = elementAt(i);
            if (seen.add(el)) {
                result.addElement(el);
            }
        }
        return result;
    }

    public IntegerArray increment(int offset) {
        IntegerArray result = new IntegerArray();

        for (int i = 0; i < count(); i++) {
            try {
                result.addElement(elementAt(i) + offset);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }
}

class ArrayIndexOutOfBoundsException extends Exception {
    public ArrayIndexOutOfBoundsException(int index, int count) {
        super("Грешка: индексот " + index + " е надвор од границите! " +
                "Елементите во полето се наоѓаат на позиции [0, " + (count - 1) + "].");
    }
}

public class ResizableArrayTest {

    public static void main(String[] args) throws ArrayIndexOutOfBoundsException {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if ( test == 0 ) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while ( jin.hasNextInt() ) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if ( test == 1 ) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for ( int i = 0 ; i < 4 ; ++i ) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if ( test == 2 ) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while ( jin.hasNextInt() ) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if ( a.sum() > 100 )
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if ( test == 3 ) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for ( int w = 0 ; w < 500 ; ++w ) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k =  2000;
                int t =  1000;
                for ( int i = 0 ; i < k ; ++i ) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for ( int i = 0 ; i < t ; ++i ) {
                    a.removeElement(k-i-1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}

