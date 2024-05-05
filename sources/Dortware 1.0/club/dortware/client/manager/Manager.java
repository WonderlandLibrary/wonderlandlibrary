package club.dortware.client.manager;

import java.util.List;
import java.util.NoSuchElementException;

public abstract class Manager<T> {

    private final List<T> list;

    protected Manager(List<T> list) {
        this.list = list;
        onCreated();
    }

    /**
     * Adds an object.
     * @param object - The {@code Object} to add
     */
    public final void add(T object) {
        list.add(object);
    }

    /**
     * Gets an object.
     * @param index - The index to get the {@code Object} from.
     * @return The {@code Object} at {@param index}
     */
    public final T get(int index) {
        return list.get(index);
    }

    public T get(Class<? extends T> clazz) {
        for (T t : list) {
            if (t.getClass() == clazz) {
                return t;
            }
        }
        throw new NoSuchElementException("Element belonging to class '" + clazz.getName() + "' not found");
    }

    public List<T> getList() {
        return list;
    }

    /**
     * Called by this {@code Manager}'s constructor in order to setup everything.
     */
    public abstract void onCreated();
}
