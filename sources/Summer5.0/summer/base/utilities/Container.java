package summer.base.utilities;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Container<T> {

    private List<T> items = new CopyOnWriteArrayList<>();

    public void add(T item) {
        items.add(item);
    }

    @SafeVarargs
    public final void add(T... items) {
        Arrays.stream(items).forEach(this::add);
    }

    public void remove(T item) {
        items.remove(item);
    }

    public T get(int index) {
        try {
            return items.get(index);
        } catch (Exception e) {
            return items.get(0);
        }
    }

    public int indexOf(T item) {
        return items.indexOf(item);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }

    public boolean contains(T item){
        return items.contains(item);
    }

    public void forEach(Consumer<? super T> action) {
        items.forEach(action);
    }

    public Stream<T> reverseStream() {
        List<T> items = new CopyOnWriteArrayList<>(this.items);

        Collections.reverse(items);

        return items.stream();
    }

    public Stream<T> stream() {
        return items.stream();
    }

    public Stream<T> filter(Predicate<? super T> predicate) {
        return stream().filter(predicate);
    }

    public T find(Predicate<? super T> predicate) {
        return filter(predicate).findFirst().orElse(null);
    }

    @SuppressWarnings("hiding")
	public <T> T findByClass(Class<? extends T> aClass) {
        return (T) stream().filter(item -> item.getClass().equals(aClass)).findFirst().orElse(null);
    }

    public int size() {
        return items.size();
    }

}