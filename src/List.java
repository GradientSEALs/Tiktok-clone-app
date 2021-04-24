import org.jetbrains.annotations.NotNull;

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

    @NotNull
    public Iterator<T> iterator() {
        return null;
    }

    @NotNull
    public Object[] toArray() {
        return new Object[0];
    }

    @NotNull
    public <T> T[] toArray(@NotNull T[] a) {
        return null;
    }


    public boolean add(T broker) {
        return false;
    }


    public boolean remove(Object o) {
        return false;
    }


    public boolean containsAll(@NotNull Collection<?> c) {
        return false;
    }


    public boolean addAll(@NotNull Collection<? extends Broker> c) {
        return false;
    }

    
    public boolean addAll(int index, @NotNull Collection<? extends Broker> c) {
        return false;
    }

    
    public boolean removeAll(@NotNull Collection<?> c) {
        return false;
    }

    
    public boolean retainAll(@NotNull Collection<?> c) {
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

    @NotNull
    
    public ListIterator<Broker> listIterator() {
        return null;
    }

    @NotNull
    
    public ListIterator<Broker> listIterator(int index) {
        return null;
    }

    @NotNull
    
    public List<Broker> subList(int fromIndex, int toIndex) {
        return null;
    }
}

