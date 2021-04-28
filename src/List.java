import java.util.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

public class List <T> {

    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean contains(Object o) {
        return false;
    }

    public Iterator<T> iterator() {
        return null;
    }

    public Object[] toArray() {
        return new Object[0];
    }

    public <T> T[] toArray( T[] a) {
        return null;
    }

    public boolean add(T broker) {
        return false;
    }

    public boolean remove(Object o) {
        return false;
    }

    public boolean containsAll( Collection<?> c) {
        return false;
    }

    public boolean addAll( Collection<? extends Broker> c) {
        return false;
    }

    public boolean addAll(int index,  Collection<? extends Broker> c) {
        return false;
    }

    public boolean removeAll( Collection<?> c) {
        return false;
    }

    public boolean retainAll( Collection<?> c) {
        return false;
    }

    public void clear() {

    }

    public Broker get(int index) {
        return null;
    }

    public Broker set(int index, Broker element) {
        return null;
    }

    public void add(int index, Broker element) {

    }

    public Broker remove(int index) {
        return null;
    }

    public int indexOf(Object o) {
        return 0;
    }

    public int lastIndexOf(Object o) {
        return 0;
    }

    public ListIterator<Broker> listIterator() {
        return null;
    }

    public ListIterator<Broker> listIterator(int index) {
        return null;
    }

    public List<Broker> subList(int fromIndex, int toIndex) {
        return null;
    }
}

