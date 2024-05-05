// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.misc;

import java.util.function.Predicate;
import java.util.Collections;
import java.util.Collection;
import java.util.stream.Stream;
import java.util.function.Consumer;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class ContainerUtils<T>
{
    private List<T> items;
    
    public ContainerUtils() {
        this.items = new CopyOnWriteArrayList<T>();
    }
    
    public void add(final T item) {
        this.items.add(item);
    }
    
    @SafeVarargs
    public final void add(final T... items) {
        Arrays.stream(items).forEach(this::add);
    }
    
    public void remove(final T item) {
        this.items.remove(item);
    }
    
    public T get(final int index) {
        try {
            return this.items.get(index);
        }
        catch (Exception e) {
            return this.items.get(0);
        }
    }
    
    public int indexOf(final T item) {
        return this.items.indexOf(item);
    }
    
    public boolean isEmpty() {
        return this.items.isEmpty();
    }
    
    public void clear() {
        this.items.clear();
    }
    
    public boolean contains(final T item) {
        return this.items.contains(item);
    }
    
    public void forEach(final Consumer<? super T> action) {
        this.items.forEach(action);
    }
    
    public Stream<T> reverseStream() {
        final List<T> items = new CopyOnWriteArrayList<T>((Collection<? extends T>)this.items);
        Collections.reverse(items);
        return items.stream();
    }
    
    public Stream<T> stream() {
        return this.items.stream();
    }
    
    public Stream<T> filter(final Predicate<? super T> predicate) {
        return this.stream().filter(predicate);
    }
    
    public T find(final Predicate<? super T> predicate) {
        return this.filter(predicate).findFirst().orElse(null);
    }
    
    public <T> T findByClass(final Class<? extends T> aClass) {
        return this.stream().filter(item -> item.getClass().equals(aClass)).findFirst().orElse(null);
    }
    
    public int size() {
        return this.items.size();
    }
}
